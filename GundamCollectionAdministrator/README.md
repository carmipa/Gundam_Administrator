# Gundam Collection Administrator

<div align="center">
  <img src="./gundam.png" alt="Gundam Collection Administrator" width="400" />
</div>

---

[![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-6DB33F?logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-336791?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Flyway](https://img.shields.io/badge/Flyway-11.x-CC0200?logo=flyway&logoColor=white)](https://flywaydb.org/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle&logoColor=white)](https://gradle.org/)
[![Docker Compose](https://img.shields.io/badge/Docker%20Compose-OK-2496ED?logo=docker&logoColor=white)](https://docs.docker.com/compose/)

Gestor completo de coleção de Gunpla (Gundam), com cadastro de kits, fotos, filtros, catálogos fixos e relatórios simples. UI com tema inspirado no RX-78-2, i18n (PT/EN/JA) e uploads estáticos.

---

## Índice

- [Gundam Collection Administrator](#gundam-collection-administrator)
  - [Índice](#índice)
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

---

## Dados do Projeto

- Nome: Gundam Collection Administrator
- Stack: Spring Boot 3.5, Java 17, Thymeleaf, Spring Data JPA, Flyway, PostgreSQL, Gradle
- Porta padrão: `8080`
- Banco de dados (dev): `postgres:latest` via Docker Compose, porta `5432` (db/name/user/pass `gundam`)
- Diretório de uploads: `uploads/` (servido em `/uploads/**`)
- Internacionalização (i18n): mensagens em `messages.properties` (pt-BR), `messages_en.properties`, `messages_ja.properties` com alternância via parâmetro `?lang=`

---

## Arquitetura

```mermaid
flowchart TB
    subgraph "Interface do Usuário (Web UI com Thymeleaf)"
        UI1["Página Home"]
        UI2["Lista de Kits / Filtros"]
        UI3["Formulário (Novo/Editar)"]
        UI4["Página de Detalhes"]
        UI5["Recursos Estáticos (CSS / Uploads)"]
    end

    subgraph "Aplicação (Spring MVC)"
        C1["Controllers"]
        S1["Services (Regras de Negócio)"]
        V1["Specifications (Filtros)"]
    end

    subgraph "Persistência (Data)"
        R1["JPA Repositories"]
        DB[(PostgreSQL)]
    end

    subgraph "Infraestrutura (Infra)"
        F1["Flyway (Migrations)"]
        Cache["Spring Cache"]
        Dk["Docker Compose"]
    end

    %% Conexões do Fluxo Principal
    UI1 --> C1
    UI2 --> C1
    UI3 --> C1
    UI4 --> C1

    C1 --"Chama"--> S1
    S1 --"Utiliza"--> R1
    S1 --"Cria"--> V1
    V1 --"Usado por"--> R1
    R1 --"Acessa"--> DB

    %% Conexões da Infraestrutura
    F1 --"Gerencia schema do"--> DB
    Cache -.-> S1
    Dk --"Orquestra container do"--> DB
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

*(restante do README continua igual, sem alterações)*
