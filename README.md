
# CotacaoSeguros - API de Cotação

## Descrição

Este projeto implementa uma **API REST** para cotação de seguros como parte de um **teste técnico** para o ITAU. A aplicação permite realizar cotações e interagir com os dados de cotação através de endpoints RESTful.

## Funcionalidades

- Receber dados de cotação via POST e validar as informações.
- Listar todas as cotações e exibir cotações específicas por ID.
- Validação de coberturas, assistências e prêmio mensal.
- Conexão com banco de dados **MySQL** e **RabbitMQ** para mensageria.

## Tecnologias

- **Spring Boot** (Versão 3.4.2)
- **Java 17**
- **MySQL** (versão 8.0)
- **RabbitMQ**
- **MockServer** para testes
- **JUNIT**
- **JPA** (Spring Data)

## Pré-requisitos

1. **Java 17** ou superior.
2. **Docker** e **Docker Compose** (para rodar o banco MySQL e RabbitMQ em containers).
3. A API depende do **MySQL** e **RabbitMQ** estarem em funcionamento para persistência e mensageria.

## Como rodar a aplicação

### 1. Subindo os containers com Docker

Para rodar o banco de dados e o RabbitMQ com Docker, execute:

```bash
docker-compose up -d
```

Isso irá iniciar o **MySQL** e o **RabbitMQ** com as configurações pré-definidas.

### 2. Executando a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em [http://localhost:8080](http://localhost:8080).

### 3. Configuração do banco de dados

A configuração do banco de dados está definida no arquivo `application.properties`:

```properties
spring.application.name=cotacaoSeguros
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/cotacao_db
spring.datasource.username=cotacao_user
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update

spring.rabbitmq.host=172.19.0.2
spring.rabbitmq.port=15672
spring.rabbitmq.username=user

spring.config.import=optional:classpath:application-secrets.properties
```

### 4. Arquivo `docker-compose.yml`

O projeto também inclui um arquivo `docker-compose.yml` que define os serviços do **MySQL**:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: mysql_cotacao
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: cotacao_db
      MYSQL_USER: cotacao_user
      MYSQL_PASSWORD: cotacao_password
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - cotacao-network
    restart: always

volumes:
  mysql-data:

networks:
  cotacao-network:
    driver: bridge
```

### 5. Configuração de Secrets

As secrets como credenciais do banco de dados e do RabbitMQ devem ser configurados em um arquivo separado: `application-secrets.properties`.

## Endpoints da API

### 1. **POST /api/v1/cotacao**

Recebe os dados de cotação.

#### Exemplo de Request:

```json
{
  "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
  "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
  "category": "HOME",
  "total_monthly_premium_amount": 75.25,
  "total_coverage_amount": 825000.00,
  "coverages": {
    "Incêndio": 250000.00,
    "Desastres naturais": 500000.00,
    "Responsabiliadade civil": 75000.00
  },
  "assistances": [
    "Encanador",
    "Eletricista",
    "Chaveiro 24h"
  ],
  "customer": {
    "document_number": "36205578900",
    "name": "John Wick",
    "type": "NATURAL",
    "gender": "MALE",
    "date_of_birth": "1973-05-02",
    "email": "johnwick@gmail.com",
    "phone_number": 11950503030
  }
}
```

#### Comando `curl` para enviar dados de cotação:

```bash
curl --location 'http://localhost:8080/api/v1/cotacao' --header 'Content-Type: application/json' --data-raw '{
    "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
    "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
    "category": "HOME",
    "total_monthly_premium_amount": 75.25,
    "total_coverage_amount": 825000.00,
    "coverages": {
        "Incêndio": 250000.00,
        "Desastres naturais": 500000.00,
        "Responsabiliadade civil": 75000.00
    },
    "assistances": [
        "Encanador",
        "Eletricista",
        "Chaveiro 24h"
    ],
    "customer": {
        "document_number": "36205578900",
        "name": "John Wick",
        "type": "NATURAL",
        "gender": "MALE",
        "date_of_birth": "1973-05-02",
        "email": "johnwick@gmail.com",
        "phone_number": 11950503030
    }
}'
```

#### Resposta:

- `201 Created`: Dados de cotação recebidos com sucesso.
- `400 Bad Request`: Erro nos dados da cotação.

### 2. **GET /api/v1/cotacao/listar**

Lista todas as cotações.

#### Comando `curl` para listar cotações:

```bash
curl --location 'http://localhost:8080/api/v1/cotacao/listar'
```

#### Resposta:

```json
[
  {
    "id": 1,
    "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
    "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
    "category": "HOME",
    "total_monthly_premium_amount": 75.25,
    "total_coverage_amount": 825000.00,
    "coverages": {
      "Incêndio": 250000.00,
      "Desastres naturais": 500000.00,
      "Responsabiliadade civil": 75000.00
    },
    "assistances": [
      "Encanador",
      "Eletricista",
      "Chaveiro 24h"
    ]
  }
]
```

### 3. **GET /api/v1/cotacao/{id}**

Retorna os detalhes de uma cotação específica.

#### Comando `curl` para obter cotação por ID:

```bash
curl --location 'http://localhost:8080/api/v1/cotacao/1'
```

#### Resposta:

- `200 OK`: Dados da cotação.
- `404 Not Found`: Cotação não encontrada.

## Testes

Os testes do projeto utilizam o **MockServer** para simular respostas de serviços externos e o **JUnit** para garantir o correto funcionamento da API.

Execute os testes com o seguinte comando:

```bash
./mvnw test
```

Ou, se estiver usando **Maven** globalmente:

```bash
mvn test
```
