package com.empresa.lotes.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lote")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String operador;

    @Column(nullable = false, length = 100)
    private String processo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLote status = StatusLote.PENDENTE;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Documento> documentos = new ArrayList<>();

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

    public List<Documento> getDocumentos() { return documentos; }
    public void setDocumentos(List<Documento> documentos) { this.documentos = documentos; }
}
