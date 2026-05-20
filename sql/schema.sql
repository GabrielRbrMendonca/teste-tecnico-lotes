-- =============================================================
-- PARTE A — DDL
-- Schema de produção (PostgreSQL).
-- O backend usa H2 em memória; este arquivo documenta o DDL
-- equivalente para ambientes reais.
-- =============================================================

CREATE TYPE status_lote AS ENUM ('PENDENTE', 'EXPORTADO', 'REJEITADO');

CREATE TABLE lote (
    id           BIGSERIAL    PRIMARY KEY,
    operador     VARCHAR(100) NOT NULL,
    processo     VARCHAR(100) NOT NULL,
    status       status_lote  NOT NULL DEFAULT 'PENDENTE',
    data_criacao TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE documento (
    id      BIGSERIAL    PRIMARY KEY,
    lote_id BIGINT       NOT NULL REFERENCES lote(id) ON DELETE CASCADE,
    tipo    VARCHAR(50)  NOT NULL,
    nome    VARCHAR(255) NOT NULL
);

-- Índice 1: acelera filtros por status e ordenação por data_criacao.
-- As consultas mais frequentes em produção filtram por status (PENDENTE, EXPORTADO, REJEITADO)
-- e ordenam por data decrescente; o índice composto cobre ambas as colunas em um único scan.
CREATE INDEX idx_lote_status_data ON lote(status, data_criacao DESC);

-- Índice 2: acelera o JOIN documento → lote.
-- Com 8M+ de registros em documento, cada JOIN sem índice em lote_id força um seq scan completo.
CREATE INDEX idx_documento_lote_id ON documento(lote_id);

-- Índice 3: acelera filtros por tipo dentro de um lote (ex.: WHERE d.tipo = 'RG').
-- Cobre o filtro da query lenta da Parte C sem precisar varrer toda a tabela documento.
CREATE INDEX idx_documento_tipo_lote ON documento(tipo, lote_id);
