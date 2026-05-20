# Teste Técnico — Gerenciamento de Lotes de Documentos

Sistema de gestão de lotes de documentos com backend em Java/Spring Boot, frontend em Angular e banco H2 em memória.

## Pré-requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java | 8 |
| Maven | 3.6+ |
| Node.js | 18+ |
| Angular CLI | 15+ |
| Docker (opcional) | 20+ |

## Subindo o Backend

```bash
cd backend
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

**H2 Console** (banco em memória):
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:lotesdb`
- Usuário: `sa`
- Senha: *(vazio)*

### Rodando os testes

```bash
cd backend
mvn test
```

## Subindo o Frontend

```bash
cd frontend
npm install
ng serve --proxy-config proxy.conf.json
```

A aplicação sobe em `http://localhost:4200`.

O `proxy.conf.json` redireciona chamadas `/api/*` para `localhost:8080`, eliminando problemas de CORS em desenvolvimento.

## Rodando com Docker

```bash
docker-compose up --build
```

- Backend: `http://localhost:8080`
- Frontend: `http://localhost:4200`

## Banco de dados

O backend usa **H2 em memória** por padrão — nenhuma instalação necessária. O schema é criado automaticamente pelo Hibernate no startup.

O arquivo `sql/schema.sql` contém o DDL equivalente para **PostgreSQL** (produção), incluindo índices e a query de relatório gerencial.

## Endpoints da API

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/lotes` | Cria um novo lote |
| `GET` | `/api/lotes` | Lista lotes com filtro e paginação |
| `PATCH` | `/api/lotes/{id}/status` | Atualiza o status de um lote |

### POST /api/lotes

```json
{
  "operador": "joao.silva",
  "processo": "ABERTURA_CONTA",
  "documentos": [
    { "tipo": "RG", "nome": "rg_frente.jpg" },
    { "tipo": "CPF", "nome": "cpf.pdf" }
  ]
}
```

Retorna `201 Created` com o lote criado (status inicial: `PENDENTE`).

### GET /api/lotes

Query params opcionais: `status`, `operador`, `page` (default 0), `size` (default 10).

### PATCH /api/lotes/{id}/status

```json
{ "status": "EXPORTADO" }
```

Status válidos: `PENDENTE`, `EXPORTADO`, `REJEITADO`. Lote `EXPORTADO` não pode ser alterado (retorna `422`).

## Decisões técnicas

- **Java 8 + Spring Boot 2.7.18**: última versão do Spring Boot com suporte oficial a Java 8. Spring Boot 3.x exige Java 17+.
- **H2 em memória**: elimina dependência de PostgreSQL local para rodar o projeto. O DDL de produção está documentado em `sql/schema.sql`.
- **TDD**: testes foram escritos antes da implementação (commits RED antes dos commits GREEN). O histórico Git evidencia o ciclo.
- **`StatusLote.canTransitionTo()`**: regra de transição de status isolada no enum de domínio, testável sem Spring context.
- **`proxy.conf.json`**: redireciona `/api` → `localhost:8080` no Angular CLI. Sem hardcode de URL no código frontend.
- **Docker multi-stage**: imagem final usa `eclipse-temurin:8-jre-alpine` (~100 MB) em vez do JDK completo (~400 MB).
- **Sem autenticação / sem testes E2E**: fora do escopo do enunciado.

## Estrutura do repositório

```
/
├── README.md
├── docker-compose.yml
├── .github/
│   └── workflows/
│       └── ci.yml
├── backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/empresa/lotes/
│       │   ├── config/         CorsConfig.java
│       │   ├── controller/     LoteController.java
│       │   ├── service/        LoteService.java
│       │   ├── repository/     LoteRepository.java
│       │   ├── model/          Lote.java, Documento.java, StatusLote.java
│       │   ├── dto/            LoteRequest.java, LoteResponse.java, StatusRequest.java
│       │   └── exception/      GlobalExceptionHandler.java, ...
│       └── test/java/com/empresa/lotes/
│           ├── LoteServiceTest.java
│           └── LoteControllerTest.java
├── frontend/
│   ├── Dockerfile
│   ├── proxy.conf.json
│   ├── angular.json
│   └── src/app/
│       ├── components/
│       │   ├── lote-list/
│       │   ├── lote-table/
│       │   ├── lote-filter/
│       │   ├── status-badge/
│       │   └── status-modal/
│       └── services/
│           └── lote.service.ts
└── sql/
    └── schema.sql
```
