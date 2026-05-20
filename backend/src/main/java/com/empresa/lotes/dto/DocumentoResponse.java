package com.empresa.lotes.dto;

public class DocumentoResponse {

    private Long id;
    private String tipo;
    private String nome;

    public DocumentoResponse() {}

    public DocumentoResponse(Long id, String tipo, String nome) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
    }

    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public String getNome() { return nome; }
}
