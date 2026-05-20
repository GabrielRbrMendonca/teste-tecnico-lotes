package com.empresa.lotes.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class LoteRequest {

    @NotBlank(message = "operador é obrigatório")
    private String operador;

    @NotBlank(message = "processo é obrigatório")
    private String processo;

    @NotNull(message = "documentos é obrigatório")
    @Size(min = 1, message = "documentos deve conter ao menos 1 item")
    @Valid
    private List<DocumentoRequest> documentos;

    public LoteRequest() {}

    public LoteRequest(String operador, String processo, List<DocumentoRequest> documentos) {
        this.operador = operador;
        this.processo = processo;
        this.documentos = documentos;
    }

    public String getOperador() { return operador; }
    public void setOperador(String operador) { this.operador = operador; }

    public String getProcesso() { return processo; }
    public void setProcesso(String processo) { this.processo = processo; }

    public List<DocumentoRequest> getDocumentos() { return documentos; }
    public void setDocumentos(List<DocumentoRequest> documentos) { this.documentos = documentos; }
}
