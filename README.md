# Order Payment Processing System

API de pedidos desenvolvida para estudar a construção de sistemas distribuídos com Java e Spring Boot. O projeto implementa a criação e a persistência de pedidos e serve como base para um fluxo assíncrono de estoque, pagamentos e notificações.

## Funcionalidades

- Criação de pedidos com um ou mais itens
- Validação dos dados de entrada
- Cálculo automático do subtotal dos itens e do valor total do pedido
- Persistência de pedidos, itens e histórico de status no PostgreSQL
- Controle de concorrência otimista com versionamento da entidade
- Versionamento do banco de dados com Flyway
- Health check com Spring Boot Actuator
- Estrutura preparada para mensageria com RabbitMQ e Spring AMQP
- Imagem Docker multi-stage executada por um usuário sem privilégios

## Tecnologias

| Tecnologia | Versão/finalidade |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Spring Web MVC | API REST |
| Spring Data JPA / Hibernate | Persistência |
| PostgreSQL | 17 |
| Flyway | Migrations do banco de dados |
| Bean Validation | Validação das requisições |
| Spring AMQP | Base para mensageria com RabbitMQ |
| Spring Boot Actuator | Health check e informações da aplicação |
| Maven | Build e gerenciamento de dependências |
| Docker / Docker Compose | Empacotamento e infraestrutura local |

## Pré-requisitos

Para executar a aplicação localmente:

- Java 21
- Docker e Docker Compose

Não é necessário instalar o Maven: o projeto inclui o Maven Wrapper.

## Como executar

### 1. Inicie o PostgreSQL

```bash
docker compose up -d
```

O banco ficará disponível em `localhost:5433`. O volume `order_postgres_data` mantém os dados entre reinicializações.

### 2. Inicie a aplicação

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API será iniciada em `http://localhost:8081`. Na inicialização, o Flyway cria ou atualiza automaticamente o schema do banco.

### Executar os testes

Com o PostgreSQL disponível:

```bash
./mvnw test
```

No Windows, use `.\mvnw.cmd test`.

## API

### Criar pedido

`POST /orders`

Exemplo de requisição:

```bash
curl --request POST http://localhost:8081/orders \
  --header "Content-Type: application/json" \
  --data '{
    "customerId": "3cf569bc-628e-423e-a72a-86f7939ac474",
    "items": [
      {
        "productId": "57e23421-0df4-462d-826c-3b94cbd6323b",
        "productName": "MacBook Air M2",
        "unitPrice": 10000.00,
        "quantity": 1
      },
      {
        "productId": "d4a35006-5908-4cd1-a24a-2d44bcd1ff80",
        "productName": "AirPods 2",
        "unitPrice": 1000.00,
        "quantity": 2
      }
    ]
  }'
```

Em caso de sucesso, a API responde com `201 Created` e o pedido criado. O status inicial é `CREATED` e, no exemplo acima, o valor total será `12000.00`.

Principais validações:

- `customerId` e `productId` devem ser UUIDs válidos;
- o pedido deve conter pelo menos um item;
- `productName` não pode estar vazio;
- `unitPrice` deve ser maior ou igual a zero;
- `quantity` deve ser maior que zero.

Uma coleção pronta para importação no Postman está disponível em [`docs/postman/Order-Service.postman_collection.json`](docs/postman/Order-Service.postman_collection.json).

### Health check

```text
GET http://localhost:8081/actuator/health
```

## Configuração

As configurações padrão ficam em `src/main/resources/application.properties` e podem ser sobrescritas pelas seguintes variáveis de ambiente:

| Variável | Valor padrão |
|---|---|
| `ORDER_DATABASE_URL` | `jdbc:postgresql://localhost:5433/order_db` |
| `ORDER_DATABASE_USERNAME` | `order_user` |
| `ORDER_DATABASE_PASSWORD` | `order_password` |

## Executar com Docker

Primeiro, construa a imagem:

```bash
docker build -t order-service .
```

Com o PostgreSQL do `compose.yaml` em execução, inicie a aplicação apontando para o host:

```bash
docker run --rm -p 8081:8081 \
  -e ORDER_DATABASE_URL=jdbc:postgresql://host.docker.internal:5433/order_db \
  -e ORDER_DATABASE_USERNAME=order_user \
  -e ORDER_DATABASE_PASSWORD=order_password \
  order-service
```

## Estrutura do projeto

```text
src/main/java/com/felipe/orderservice/
├── OrderServiceApplication.java
└── order/
    ├── controller/   # Endpoints REST
    ├── domain/       # Entidades e regras de domínio
    ├── dto/          # Contratos de entrada e saída
    ├── repository/   # Acesso ao banco de dados
    └── service/      # Casos de uso

src/main/resources/
├── application.properties
└── db/migration/     # Migrations do Flyway
```

## Próximos passos

- [ ] Implementar os serviços de pagamento e notificação
- [ ] Publicar e consumir eventos com RabbitMQ
- [ ] Adicionar testes unitários e de integração
- [ ] Documentar os endpoints com OpenAPI/Swagger
- [ ] Adicionar tratamento global e padronização das respostas de erro
- [ ] Orquestrar aplicação, PostgreSQL e RabbitMQ no Docker Compose

---

<p align="center">Desenvolvido por <strong>Felipe Souza Moreira</strong></p>
