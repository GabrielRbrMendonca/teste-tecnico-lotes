export type StatusLote = 'PENDENTE' | 'EXPORTADO' | 'REJEITADO';

export interface DocumentoResponse {
  id: number;
  tipo: string;
  nome: string;
}

export interface LoteResponse {
  id: number;
  operador: string;
  processo: string;
  status: StatusLote;
  dataCriacao: string;
  documentos: DocumentoResponse[];
}

export interface PageResponse {
  content: LoteResponse[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}
