package com.empresa.lotes.dto;

import com.empresa.lotes.model.StatusLote;

import java.time.LocalDateTime;
import java.util.List;

public class LoteResponse {

    private Long id;
    private String operador;
    private String processo;
    private StatusLote status;
    private LocalDateTime dataCriacao;
    private List<DocumentoResponse> documentos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOperador() { return operador; }
    public void setOperador(String operador) { this.operador = operador; }

    public String getProcesso() { return processo; }
    public void setProcesso(String processo) { this.processo = processo; }

    public StatusLote getStatus() { return status; }
    public void setStatus(StatusLote status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public List<DocumentoResponse> getDocumentos() { return documentos; }
    public void setDocumentos(List<DocumentoResponse> documentos) { this.documentos = documentos; }
}
