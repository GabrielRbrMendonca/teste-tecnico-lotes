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


-- =============================================================
-- PARTE B — Query de relatório
-- Por operador, nos últimos 30 dias:
--   total de lotes, total de documentos, quantidade por status
--   e média de documentos por lote.
-- =============================================================

SELECT
    l.operador,
    COUNT(DISTINCT l.id)                                                AS total_lotes,
    COUNT(d.id)                                                         AS total_documentos,
    SUM(CASE WHEN l.status = 'PENDENTE'   THEN 1 ELSE 0 END)           AS pendentes,
    SUM(CASE WHEN l.status = 'EXPORTADO'  THEN 1 ELSE 0 END)           AS exportados,
    SUM(CASE WHEN l.status = 'REJEITADO'  THEN 1 ELSE 0 END)           AS rejeitados,
    ROUND(
        COUNT(d.id)::NUMERIC / NULLIF(COUNT(DISTINCT l.id), 0), 2
    )                                                                   AS media_docs_por_lote
FROM lote l
LEFT JOIN documento d ON d.lote_id = l.id
WHERE l.data_criacao > NOW() - INTERVAL '30 days'
GROUP BY l.operador
ORDER BY total_lotes DESC;


-- =============================================================
-- PARTE C — Diagnóstico de performance
--
-- Query original com lentidão em produção (tabela documento: 8M registros):
--
--   SELECT l.operador, COUNT(d.id) as total_docs
--   FROM lote l
--   JOIN documento d ON d.lote_id = l.id
--   WHERE d.tipo = 'RG'
--     AND l.status = 'PENDENTE'
--     AND l.data_criacao > NOW() - INTERVAL '90 days'
--   GROUP BY l.operador
--   ORDER BY total_docs DESC;
--
-- CAUSAS DA LENTIDÃO:
--   1. Sem índice em documento(tipo, lote_id): seq scan em 8M registros a cada execução.
--   2. Sem índice em lote(status, data_criacao): seq scan adicional na tabela lote.
--   3. O JOIN acontece sobre o dataset completo antes de filtrar por tipo,
--      trazendo todos os documentos para só então descartar os não-RG.
--
-- ÍNDICES QUE RESOLVEM (já criados na Parte A):
--   - idx_documento_tipo_lote ON documento(tipo, lote_id)
--   - idx_lote_status_data    ON lote(status, data_criacao DESC)
--
-- QUERY REESCRITA — filtra lote primeiro via CTE, reduzindo o dataset
-- do JOIN de 8M para apenas os lotes PENDENTES dos últimos 90 dias:
-- =============================================================

WITH lotes_filtrados AS (
    SELECT l.id, l.operador
    FROM lote l
    WHERE l.status = 'PENDENTE'
      AND l.data_criacao > NOW() - INTERVAL '90 days'
)
SELECT
    l.operador,
    COUNT(d.id) AS total_docs
FROM lotes_filtrados l
JOIN documento d ON d.lote_id = l.id
WHERE d.tipo = 'RG'
GROUP BY l.operador
ORDER BY total_docs DESC;
