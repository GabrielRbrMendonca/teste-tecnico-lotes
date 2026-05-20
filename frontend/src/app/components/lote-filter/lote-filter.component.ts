import { Component, EventEmitter, Output } from '@angular/core';
import { StatusLote } from '../../models/lote.model';

@Component({
  selector: 'app-lote-filter',
  template: `
    <div class="filter-bar">
      <label for="status-filter">Filtrar por status:</label>
      <select id="status-filter" [(ngModel)]="selected" (change)="onChange()">
        <option value="">Todos</option>
        <option value="PENDENTE">Pendente</option>
        <option value="EXPORTADO">Exportado</option>
        <option value="REJEITADO">Rejeitado</option>
      </select>
    </div>
  `,
  styles: [`
    .filter-bar { display: flex; align-items: center; gap: 8px; margin-bottom: 16px; }
    select { padding: 6px 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 14px; }
  `]
})
export class LoteFilterComponent {
  @Output() filterChange = new EventEmitter<StatusLote | undefined>();

  selected: string = '';

  onChange(): void {
    this.filterChange.emit(this.selected ? (this.selected as StatusLote) : undefined);
  }
}
