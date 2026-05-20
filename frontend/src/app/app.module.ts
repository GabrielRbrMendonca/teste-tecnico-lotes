import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { LoteListComponent } from './components/lote-list/lote-list.component';
import { LoteTableComponent } from './components/lote-table/lote-table.component';
import { LoteFilterComponent } from './components/lote-filter/lote-filter.component';
import { StatusBadgeComponent } from './components/status-badge/status-badge.component';
import { StatusModalComponent } from './components/status-modal/status-modal.component';

@NgModule({
  declarations: [
    AppComponent,
    LoteListComponent,
    LoteTableComponent,
    LoteFilterComponent,
    StatusBadgeComponent,
    StatusModalComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
