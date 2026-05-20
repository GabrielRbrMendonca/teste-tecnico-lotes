package com.empresa.lotes.model;

public enum StatusLote {

    PENDENTE {
        @Override
        public boolean canTransitionTo(StatusLote destino) {
            return destino == EXPORTADO || destino == REJEITADO;
        }
    },
    EXPORTADO {
        @Override
        public boolean canTransitionTo(StatusLote destino) {
            return false;
        }
    },
    REJEITADO {
        @Override
        public boolean canTransitionTo(StatusLote destino) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(StatusLote destino);
}
