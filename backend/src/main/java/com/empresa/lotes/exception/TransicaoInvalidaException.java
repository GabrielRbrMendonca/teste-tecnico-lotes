package com.empresa.lotes.exception;

import com.empresa.lotes.model.StatusLote;

public class TransicaoInvalidaException extends RuntimeException {

    public TransicaoInvalidaException(StatusLote atual, StatusLote destino) {
        super("Transição inválida: " + atual + " -> " + destino);
    }
}
