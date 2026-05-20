import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LoteResponse, StatusLote } from '../../models/lote.model';

@Component({
  selector: 'app-status-modal',
  template: `
    <div class="overlay" (click)="onCancel()">
      <div class="modal" (click)="$event.stopPropagation()">
        <h3>Alterar Status</h3>
        <p>Lote <strong>#{{ lote.id }}</strong> — {{ lote.operador }}</p>
        <p>Status atual: <app-status-badge [status]="lote.status"></app-status-badge></p>

        <div class="field">
          <label for="new-status">Novo status:</label>
          <select id="new-status" [(ngModel)]="novoStatus">
            <option value="PENDENTE">Pendente</option>
            <option value="EXPORTADO">Exportado</option>
            <option value="REJEITADO">Rejeitado</option>
          </select>
        </div>

        <div class="actions">
          <button class="btn-cancel" (click)="onCancel()">Cancelar</button>
          <button class="btn-confirm" (click)="onConfirm()" [disabled]="novoStatus === lote.status">
            Confirmar
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .overlay { position:fixed; inset:0; background:rgba(0,0,0,.4); display:flex; align-items:center; justify-content:center; z-index:100; }
    .modal { background:#fff; border-radius:8px; padding:24px; min-width:320px; display:flex; flex-direction:column; gap:12px; }
    h3 { margin:0; font-size:16px; }
    .field { display:flex; flex-direction:column; gap:4px; }
    select { padding:6px 10px; border:1px solid #ccc; border-radius:4px; }
    .actions { display:flex; justify-content:flex-end; gap:8px; margin-top:8px; }
    .btn-cancel { padding:7px 16px; border:1px solid #ccc; background:#fff; border-radius:4px; cursor:pointer; }
    .btn-confirm { padding:7px 16px; background:#1a73e8; color:#fff; border:none; border-radius:4px; cursor:pointer; }
    .btn-confirm:disabled { background:#aaa; cursor:not-allowed; }
  `]
})
export class StatusModalComponent {
  @Input() lote!: LoteResponse;
  @Output() confirmed = new EventEmitter<StatusLote>();
  @Output() cancelled = new EventEmitter<void>();

  novoStatus!: StatusLote;

  ngOnInit(): void {
    this.novoStatus = this.lote.status;
  }

  onConfirm(): void {
    this.confirmed.emit(this.novoStatus);
  }

  onCancel(): void {
    this.cancelled.emit();
  }
}
