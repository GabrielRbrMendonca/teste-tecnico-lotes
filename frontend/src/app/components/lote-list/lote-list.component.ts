import { Component, OnInit } from '@angular/core';
import { LoteResponse, PageResponse, StatusLote } from '../../models/lote.model';
import { LoteService } from '../../services/lote.service';

@Component({
  selector: 'app-lote-list',
  template: `
    <div class="container">
      <h1>Gerenciamento de Lotes</h1>

      <app-lote-filter (filterChange)="onFilterChange($event)"></app-lote-filter>

      <div *ngIf="isLoading" class="loading">Carregando...</div>

      <div *ngIf="erro" class="erro">{{ erro }}</div>

      <ng-container *ngIf="!isLoading">
        <app-lote-table
          [lotes]="lotes"
          (statusChange)="abrirModal($event)">
        </app-lote-table>

        <div class="pagination" *ngIf="totalPages > 1">
          <button (click)="irParaPagina(paginaAtual - 1)" [disabled]="paginaAtual === 0">Anterior</button>
          <span>Página {{ paginaAtual + 1 }} de {{ totalPages }}</span>
          <button (click)="irParaPagina(paginaAtual + 1)" [disabled]="paginaAtual >= totalPages - 1">Próxima</button>
        </div>
      </ng-container>

      <app-status-modal
        *ngIf="loteSelecionado"
        [lote]="loteSelecionado"
        (confirmed)="confirmarStatus($event)"
        (cancelled)="fecharModal()">
      </app-status-modal>
    </div>
  `,
  styles: [`
    .container { max-width:1100px; margin:32px auto; padding:0 16px; }
    h1 { font-size:22px; margin-bottom:20px; color:#222; }
    .loading { padding:24px; text-align:center; color:#666; }
    .erro { padding:12px 16px; background:#FCEBEB; color:#791F1F; border-radius:4px; margin-bottom:16px; }
    .pagination { display:flex; align-items:center; justify-content:center; gap:16px; margin-top:16px; }
    .pagination button { padding:6px 14px; border:1px solid #ccc; border-radius:4px; cursor:pointer; background:#fff; }
    .pagination button:disabled { color:#aaa; cursor:not-allowed; }
  `]
})
export class LoteListComponent implements OnInit {

  lotes: LoteResponse[] = [];
  isLoading = false;
  erro: string | null = null;
  loteSelecionado: LoteResponse | null = null;

  paginaAtual = 0;
  totalPages = 0;
  private statusFiltro?: StatusLote;

  constructor(private loteService: LoteService) {}

  ngOnInit(): void {
    this.loteService.isLoading$.subscribe(v => this.isLoading = v);
    this.carregarLotes();
  }

  carregarLotes(): void {
    this.erro = null;
    this.loteService.getLotes(this.statusFiltro, this.paginaAtual).subscribe({
      next: (page: PageResponse) => {
        this.lotes = page.content;
        this.totalPages = page.totalPages;
      },
      error: () => {
        this.erro = 'Erro ao carregar lotes. Verifique se o backend está rodando.';
      }
    });
  }

  onFilterChange(status?: StatusLote): void {
    this.statusFiltro = status;
    this.paginaAtual = 0;
    this.carregarLotes();
  }

  irParaPagina(pagina: number): void {
    this.paginaAtual = pagina;
    this.carregarLotes();
  }

  abrirModal(lote: LoteResponse): void {
    this.loteSelecionado = lote;
  }

  fecharModal(): void {
    this.loteSelecionado = null;
  }

  confirmarStatus(novoStatus: StatusLote): void {
    if (!this.loteSelecionado) return;
    this.loteService.atualizarStatus(this.loteSelecionado.id, novoStatus).subscribe({
      next: (atualizado: LoteResponse) => {
        this.lotes = this.lotes.map(l => l.id === atualizado.id ? atualizado : l);
        this.fecharModal();
      },
      error: () => {
        this.erro = 'Erro ao atualizar status. Verifique se a transição é válida.';
        this.fecharModal();
      }
    });
  }
}
