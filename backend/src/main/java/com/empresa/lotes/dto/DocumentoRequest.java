package com.empresa.lotes.dto;

import javax.validation.constraints.NotBlank;

public class DocumentoRequest {

    @NotBlank(message = "tipo é obrigatório")
    private String tipo;

    @NotBlank(message = "nome é obrigatório")
    private String nome;

    public DocumentoRequest() {}

    public DocumentoRequest(String tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
