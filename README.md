# Order Payment Processing System

> Projeto de estudo para entender como desenvolver sistemas utilizando microservices.

---

## O que e o Order Payment Processing System?

O **Order Payment Processing System** e um projeto criado para estudar o desenvolvimento de sistemas distribuidos com Java e Spring Boot.

A proposta e simular uma base de processamento de pedidos e pagamentos, explorando conceitos importantes de microservices, persistencia, mensageria e separacao de responsabilidades entre servicos.

---

## Como funciona?

1. **O servico de pedidos recebe e processa informacoes de ordem**, centralizando a regra de negocio relacionada ao ciclo de vida do pedido.

2. **Os dados sao persistidos em PostgreSQL**, com versionamento de schema usando Flyway.

3. **A comunicacao assincrona e preparada com RabbitMQ**, permitindo evoluir o fluxo para eventos entre servicos.

4. **O Actuator expoe endpoints de saude**, ajudando a observar a aplicacao durante o desenvolvimento.

---

## Funcionalidades

- Base de um servico de pedidos com Spring Boot
- Persistencia relacional com PostgreSQL
- Controle de migrations com Flyway
- Integracao com RabbitMQ via Spring AMQP
- Validacao de dados com Bean Validation
- Health check com Spring Boot Actuator
- Ambiente local com Docker Compose para o banco de dados

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 4 |
| Build | Maven |
| Banco de dados | PostgreSQL |
| Migrations | Flyway |
| Mensageria | RabbitMQ / Spring AMQP |
| Persistencia | Spring Data JPA / Hibernate |
| Observabilidade | Spring Boot Actuator |
| Infraestrutura | Docker Compose |

---

## Por que foi desenvolvido?

Este projeto foi criado para aprender como funciona o desenvolvimento de sistemas utilizando **microservices**.

O objetivo e praticar conceitos como servicos independentes, comunicacao assincrona, banco de dados por contexto, migrations e organizacao de uma aplicacao backend moderna.

---

## Proximos passos

Algumas evolucoes possiveis:

- [ ] Separar novos servicos para pagamento e notificacao
- [ ] Publicar eventos de pedido criado e pagamento processado
- [ ] Adicionar testes de integracao com PostgreSQL e RabbitMQ
- [ ] Criar documentacao OpenAPI para os endpoints
- [ ] Adicionar Dockerfile para empacotar a aplicacao

---

## Rodando o projeto

### Subindo dependencias

```bash
docker compose up -d
```

O Docker Compose sobe o PostgreSQL na porta `5433`, com as credenciais configuradas em `compose.yaml`.

### Executando a aplicacao

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A API fica disponivel em `http://localhost:8081`.

---

## Configuracao

As principais configuracoes estao em `src/main/resources/application.properties`.

Variaveis aceitas pela aplicacao:

- `ORDER_DATABASE_URL`
- `ORDER_DATABASE_USERNAME`
- `ORDER_DATABASE_PASSWORD`

---

<p align="center">Desenvolvido por <strong>Felipe Souza Moreira</strong></p>
