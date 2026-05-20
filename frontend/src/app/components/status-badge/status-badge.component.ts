import { Component, Input } from '@angular/core';
import { StatusLote } from '../../models/lote.model';

@Component({
  selector: 'app-status-badge',
  template: `<span class="badge" [ngClass]="cssClass">{{ status }}</span>`
})
export class StatusBadgeComponent {
  @Input() status!: StatusLote;

  get cssClass(): string {
    return 'badge-' + this.status.toLowerCase();
  }
}
