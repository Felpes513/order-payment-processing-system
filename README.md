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

### 1. Configure as credenciais e inicie a infraestrutura

Edite o `.env` na raiz do projeto. Em um clone novo, copie `.env.example` para `.env` e preencha as três senhas antes de iniciar.

```bash
docker compose up -d
```

O banco ficará disponível em `localhost:5433`. O volume `order_postgres_data` mantém os dados entre reinicializações.

### 2. Inicie a aplicação

Linux/macOS:

```bash
cd order-service
./mvnw spring-boot:run
```

Windows:

```powershell
cd order-service
.\mvnw.cmd spring-boot:run
```

A API será iniciada em `http://localhost:8081`. Na inicialização, o Flyway cria ou atualiza automaticamente o schema do banco.

### Executar os testes

Na pasta `order-service`:

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

## Contratos de código com JML

As principais regras das entidades de domínio estão documentadas como
pré-condições, pós-condições e invariantes JML. Consulte o
[`docs/JML.md`](docs/JML.md) para aprender a ler os contratos e entender como
validá-los com OpenJML.

### Health check

```text
GET http://localhost:8081/actuator/health
```

## Configuração

As credenciais ficam no `.env` da raiz, ignorado pelo Git. O `.env.example` é o modelo versionável, sem senhas. O arquivo local inicial mantém os valores de desenvolvimento anteriores; substitua-os pelos seus valores.

| Variável de senha | Utilização |
|---|---|
| `ORDER_DATABASE_PASSWORD` | PostgreSQL de pedidos |
| `INVENTORY_DATABASE_PASSWORD` | PostgreSQL de estoque |
| `RABBITMQ_PASSWORD` | RabbitMQ dos dois serviços e painel de gerenciamento |

O arquivo também contém usuários, URLs dos bancos e host/porta do RabbitMQ. Os bancos do Compose mantêm os nomes `order_db` e `inventory_db` e as portas locais 5433 e 5434.

O Docker Compose lê o `.env` automaticamente. Os dois serviços importam o arquivo como properties ao executar pela raiz do projeto ou pela pasta do serviço, inclusive pela IDE: configure o diretório de trabalho para uma dessas pastas. Variáveis de ambiente podem sobrescrever os valores. Para iniciar o estoque, execute `./mvnw spring-boot:run` na pasta `inventory-service`.

Use `CHAVE=valor`, sem aspas, espaços, `$`, `#` ou barras invertidas, para manter a compatibilidade dos leitores do arquivo; prefira senhas longas alfanuméricas. Não use `source .env`, pois o arquivo é carregado diretamente pelas aplicações.

**Volumes existentes:** alterar o `.env` não muda as credenciais já gravadas no PostgreSQL ou RabbitMQ. Nesses casos, altere também a senha no serviço correspondente antes de reiniciar as aplicações. Não apague os volumes para trocar senhas se precisar preservar os dados.

## Executar com Docker

Primeiro, construa a imagem:

```bash
docker build -t order-service ./order-service
```

Com o PostgreSQL do `compose.yaml` em execução, inicie a aplicação apontando para o host:

```bash
docker run --rm -p 8081:8081 --env-file .env \
  -e ORDER_DATABASE_URL=jdbc:postgresql://host.docker.internal:5433/order_db \
  -e RABBITMQ_HOST=host.docker.internal \
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
