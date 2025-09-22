# Gundam Collection Administrator

<div align="center">
  <img src="./gundam.png" alt="Gundam Collection Administrator" width="400" />
</div>

[![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-6DB33F?logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-336791?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Flyway](https://img.shields.io/badge/Flyway-11.x-CC0200?logo=flyway&logoColor=white)](https://flywaydb.org/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle&logoColor=white)](https://gradle.org/)
[![Docker Compose](https://img.shields.io/badge/Docker%20Compose-OK-2496ED?logo=docker&logoColor=white)](https://docs.docker.com/compose/)

Gestor completo de coleção de Gunpla (Gundam), com cadastro de kits, fotos, filtros, catálogos fixos e relatórios simples.  
UI com tema inspirado no RX‑78‑2, i18n (PT/EN/JA) e uploads estáticos.

---

## Índice

- [Dados do Projeto](#dados-do-projeto)
- [Arquitetura](#arquitetura)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Programação (Java Spring)](#programação-java-spring)
- [Funcionamento da Parte Web](#funcionamento-da-parte-web)
- [Funcionalidades](#funcionalidades)
- [Modelagem de Domínio](#modelagem-de-domínio)
- [Requisitos e Setup](#requisitos-e-setup)
- [Execução](#execução)
- [Rotas Principais](#rotas-principais)
- [Camadas e Pacotes](#camadas-e-pacotes)
- [Migrações de Banco](#migrações-de-banco)
- [Configurações](#configurações)
- [Troubleshooting](#troubleshooting)
- [Roadmap](#roadmap)
- [Licença](#licença)

---

## Dados do Projeto

- Nome: **Gundam Collection Administrator**
- Stack: Spring Boot 3.5, Java 17, Thymeleaf, Spring Data JPA, Flyway, PostgreSQL, Gradle
- Porta padrão: `8080`
- Banco de dados (dev): `postgres:latest` via Docker Compose, porta `5432` (db/name/user/pass `gundam`)
- Diretório de uploads: `uploads/` (servido em `/uploads/**`)
- Internacionalização (i18n): mensagens em `messages.properties` (pt-BR), `messages_en.properties`, `messages_ja.properties` com alternância via parâmetro `?lang=`

---

## Arquitetura

```mermaid
flowchart TB

%% Camada Web
subgraph Web["Web UI (Thymeleaf)"]
  UI1[Home]
  UI2["Kits List/Filters"]
  UI3["Form Novo/Editar"]
  UI4[Detalhes]
  UI5["Recursos Estáticos (CSS/Uploads)"]
end

%% Camada MVC
subgraph MVC["Spring MVC"]
  C1[Controllers]
  S1[Services]
  V1[Specifications]
end

%% Camada Data
subgraph Data["Data Layer"]
  R1["JPA Repositories"]
  DB[(PostgreSQL)]
end

%% Ligações
UI1 --> C1
UI2 --> C1
UI3 --> C1
UI4 --> C1
C1 --> S1 --> R1 --> DB
S1 --> V1

%% Infra
subgraph Infra["Infra"]
  F1[Flyway]
  Cache[Spring Cache]
  Dk[Docker Compose]
end

F1 --> DB
Cache --> S1
Dk --> DB
```

---

## Estrutura do Projeto

- `src/main/java/br/com/gundam`
  - `GundamApplication.java` — bootstrap da aplicação
  - `config/` — `WebConfig` (recursos estáticos, i18n, locale), `CacheConfig`
  - `controller/` — `HomeController`, `GundamKitController`
  - `service/` — `GundamKitService`, `FileStorageService`
  - `repository/` — repositórios JPA (inclui queries de relatório)
  - `spec/` — Specifications para filtros dinâmicos
  - `model/` — entidades JPA (GundamKit, Grade, Escala, AlturaPadrao, Universo)
- `src/main/resources`
  - `templates/` — views Thymeleaf (`layout.html`, `home.html`, `kits/*`, `sobre.html`, `relatorios.html`)
  - `static/css/` — estilos (`global.css`)
  - `db/migration/` — migrações Flyway `V1..V5`
  - `application.yml` — configuração da aplicação
- `compose.yaml` — serviço `postgres`
- `build.gradle` — dependências e plugins

---

## Programação (Java Spring)

- **Controllers (Spring MVC)**: tratam rotas, populam `Model` e retornam nomes de templates.
  - `GundamKitController` cobre CRUD e filtros com paginação.
  - `HomeController` entrega páginas estáticas e o relatório financeiro.
- **Services**: concentram regras de negócio e cache.
  - `GundamKitService` usa `@Cacheable`/`@CacheEvict` para catálogos e itens; monta `Specification` para busca; agrega dados para relatório.
  - `FileStorageService` padroniza salvamento de imagens e nomes de arquivos, sob `app.storage.root`.
- **Repositories (Spring Data JPA)**: CRUD + `JpaSpecificationExecutor` + queries JPQL para relatório (`SUM`, `AVG`, `MAX/MIN`).
- **Specifications**: composição dinâmica para filtros (`modelo like`, `gradeId`, `universoId`, `dataCompra between`).
- **Configurações**:
  - `CacheConfig` com `ConcurrentMapCacheManager`.
  - `WebConfig` mapeia `/uploads/**`, resolve locale via cookie e permite troca com `?lang=`.
- **Validações**: Bean Validation em entidades (ex.: `@NotBlank`, `@DecimalMin`, `@Digits`).
- **Migrações**: Flyway habilitado; `ddl-auto: validate` garante schema via SQL versionado.

---

## Funcionamento da Parte Web

- **Templates Thymeleaf**: layout base em `layout.html` com fragmentos `head`, `header`, `footer`. Páginas herdam via `th:replace`.
- **Navegação**: Home (`/`), Gerenciador (`/kits`), Relatórios (`/relatorios`), Sobre (`/sobre`). Destaque de link ativo via `requestURI`.
- **Listagem com filtros**: formulário GET em `/kits` que envia `modelo`, `gradeId`, `universoId`, `de`, `ate`, além de `page/size` para paginação. Tabela mostra contagem e paginação.
- **Formulário de Kit**: create/update no mesmo template, com selects preenchidos pelos catálogos e suporte a upload de imagens (caixa/montagem). Campos validados pelo Bean Validation.
- **Uploads**: imagens salvas em `uploads/` e expostas em `/uploads/**`. URLs gravadas nos campos do modelo e utilizadas nas views.
- **I18n**: textos extraídos de `messages*.properties`. Troca de idioma com `?lang=pt-BR|en|ja` persistida em cookie.
- **Estática**: tema visual em `static/css/global.css` e imagem `static/images/gundam.png`.

---

## Funcionalidades

- Cadastro completo de Kits (modelo, fabricante, preço, data, horas, urls, fotos de capa/caixa/montagem)
- Catálogos fixos: Grades, Escalas, Alturas Padrão
- Universo/Linha do Tempo (UC, CE, AC, etc.) e Observações longas
- Filtros na listagem:
  - Modelo (like), Grade, Universo, Período de Compra (de/até – opcionais), Paginação
- Upload de imagens (salvas em `uploads/` e servidas em `/uploads/**`)
- Páginas: Home, Listagem, Formulário (novo/editar), Detalhes, Sobre, Relatórios (placeholder)
- Migrações (Flyway) e dados seed
- Cache simples para listas de catálogos

---

## Modelagem de Domínio

Entidades principais:

- `GundamKit` (kit da coleção)
  - relaciona-se com `Grade`, `Escala`, `AlturaPadrao` e `Universo`
  - campos: `modelo`, `fabricante`, `preco`, `dataCompra`, `horasMontagem`, URLs/fotos, `observacao`
- `Grade` (MG, HG, RG, …)
- `Escala` (1/144, 1/100…)
- `AlturaPadrao` (faixas de altura)
- `Universo` (UC, CE, AC… com `sigla`, `principaisSeries`, `descricao`)

Validações (Bean Validation) em `GundamKit` para garantir integridade (ex.: `@NotBlank`, `@Digits`, `@DecimalMin`).

---

## Requisitos e Setup

- Java 17+
- Gradle Wrapper (já incluso)
- Docker + Docker Compose (para o PostgreSQL)

Iniciar banco (Docker Compose):

```bash
docker compose up -d
```

Config app (`src/main/resources/application.yml`):

- Datasource: `jdbc:postgresql://localhost:5432/gundam` (user/pass `gundam`)
- Flyway: habilitado e apontando para `classpath:db/migration`

---

## Execução

Via Gradle (recomendado):

```bash
# Windows
.\gradlew.bat clean build -x test
.\gradlew.bat bootRun
```

Dica: a primeira execução cria schema e aplica V1..V5.

> Se você alterou arquivos de migração já aplicados e recebeu erro de checksum, execute uma vez:

```bash
.\gradlew.bat bootRun --args="--flyway.repair=true"
```

> Caso a sua IDE use `bin/` no classpath e esteja conflitando com `build/`, apague `bin/` e configure para usar Gradle como builder/classpath.

---

## Rotas Principais

- Home: `GET /`
- Kits:
  - Listagem com filtros: `GET /kits?modelo=&gradeId=&universoId=&de=&ate=&page=&size=`
  - Novo: `GET /kits/novo`
  - Salvar: `POST /kits` (multipart para fotos)
  - Detalhes: `GET /kits/{id}`
  - Editar (form): `GET /kits/{id}/editar`
  - Atualizar: `POST /kits/{id}`
  - Excluir: `POST /kits/{id}/deletar`
- Páginas utilitárias: `/sobre`, `/relatorios`

---

## Camadas e Pacotes

- `br.com.gundam.controller` – MVC controllers (Kits, Home)
- `br.com.gundam.service` – regras de negócio, cache
- `br.com.gundam.repository` – repositórios JPA
- `br.com.gundam.model` – entidades JPA
- `br.com.gundam.spec` – Specifications para filtros dinâmicos
- `br.com.gundam.config` – Cache e Web config (static uploads)
- `resources/templates` – Thymeleaf (layout + páginas)
- `resources/static/css` – estilos do tema RX‑78‑2

---

## Migrações de Banco

- `V1__create_tables.sql` – tabelas base (grade, escala, altura_padrao, gundam_kits)
- `V2__seed_reference_data.sql` – seeds para catálogos
- `V4__universo_and_observacao.sql` – tabela `universo`, colunas `universo_id` e `observacao` em `gundam_kits`
- `V5__seed_universos.sql` – seeds dos universos (UC, CE, AC… AS)

> Observação: não edite migrações já aplicadas em produção. Crie uma nova `Vx__...sql` para cada mudança.

---

## Configurações

- Cache simples com `ConcurrentMapCacheManager`:
  - caches: `grades_all`, `escalas_all`, `alturas_all`, `universos_all`, `kit_by_id`
- Uploads:
  - `FileStorageService` salva em diretório configurável (`app.storage.root`, por padrão `uploads/`)
  - Servido em `/uploads/**` via `WebConfig`

---

## Troubleshooting

- Erro Flyway checksum: rode uma vez `--flyway.repair=true` ou resete o schema (ambiente local) e suba novamente.
- IDE executando com `bin/` (artefatos antigos): apague `bin/`, configure para usar Gradle como builder/classpath.
- LazyInitialization em views: relacionamentos usados na view estão como `EAGER` em `GundamKit`.

---

## Roadmap

- Máscara de moeda e mensagens de erro amigáveis no formulário
- Catálogo/CRUD visual de Universos
- Exportar lista (CSV/Excel)
- Ordenação multi-coluna e favoritos

---

## Licença

MIT (se aplicável).

---

Made with ❤️ using Spring Boot + Thymeleaf.
