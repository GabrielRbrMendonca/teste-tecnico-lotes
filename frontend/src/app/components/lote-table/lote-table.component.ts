import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LoteResponse } from '../../models/lote.model';

@Component({
  selector: 'app-lote-table',
  template: `
    <div class="table-wrapper">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Operador</th>
            <th>Processo</th>
            <th>Status</th>
            <th>Data de Criação</th>
            <th>Ação</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let lote of lotes">
            <td>{{ lote.id }}</td>
            <td>{{ lote.operador }}</td>
            <td>{{ lote.processo }}</td>
            <td><app-status-badge [status]="lote.status"></app-status-badge></td>
            <td>{{ lote.dataCriacao | date:'dd/MM/yyyy HH:mm' }}</td>
            <td>
              <button class="btn-edit" (click)="statusChange.emit(lote)">Alterar</button>
            </td>
          </tr>
          <tr *ngIf="lotes.length === 0">
            <td colspan="6" class="empty">Nenhum lote encontrado.</td>
          </tr>
        </tbody>
      </table>
    </div>
  `,
  styles: [`
    .table-wrapper { overflow-x: auto; }
    table { width:100%; border-collapse:collapse; background:#fff; border-radius:6px; overflow:hidden; box-shadow:0 1px 3px rgba(0,0,0,.1); }
    th { background:#f0f0f0; padding:10px 14px; text-align:left; font-size:12px; text-transform:uppercase; letter-spacing:.5px; color:#555; }
    td { padding:10px 14px; border-bottom:1px solid #f0f0f0; }
    tr:last-child td { border-bottom:none; }
    tr:hover td { background:#fafafa; }
    .empty { text-align:center; color:#888; padding:24px; }
    .btn-edit { padding:4px 12px; background:#1a73e8; color:#fff; border:none; border-radius:4px; cursor:pointer; font-size:12px; }
    .btn-edit:hover { background:#1557b0; }
  `]
})
export class LoteTableComponent {
  @Input() lotes: LoteResponse[] = [];
  @Output() statusChange = new EventEmitter<LoteResponse>();
}
