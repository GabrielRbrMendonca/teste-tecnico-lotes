import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { LoteResponse, PageResponse, StatusLote } from '../models/lote.model';

@Injectable({ providedIn: 'root' })
export class LoteService {

  private readonly API = '/api/lotes';

  private loadingSubject = new BehaviorSubject<boolean>(false);
  isLoading$ = this.loadingSubject.asObservable();

  constructor(private http: HttpClient) {}

  getLotes(status?: StatusLote, page = 0, size = 10): Observable<PageResponse> {
    this.loadingSubject.next(true);
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<PageResponse>(this.API, { params })
      .pipe(finalize(() => this.loadingSubject.next(false)));
  }

  atualizarStatus(id: number, status: StatusLote): Observable<LoteResponse> {
    return this.http.patch<LoteResponse>(`${this.API}/${id}/status`, { status });
  }
}
