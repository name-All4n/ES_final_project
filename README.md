# ES Clinic Project - Backend

API REST para gerenciamento de uma clínica médica, desenvolvida como projeto final da disciplina de Engenharia de Software na UFAL.

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 4.0.6 |
| Banco de dados | PostgreSQL 16 |
| Migrações | Flyway |
| Segurança | Spring Security + JWT (Auth0 4.4.0) |
| Documentação | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven |
| Containerização | Docker + Docker Compose |

---

## Pré-requisitos

Você precisa ter instalado **apenas uma** das duas opções abaixo:

**Opção A: Docker (recomendada)**
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (Windows/macOS) ou Docker Engine + Docker Compose (Linux)

**Opção B: Execução local sem Docker**
- Java 17+ ([Temurin](https://adoptium.net/))
- Maven 3.9+ (ou use o `mvnw` incluído no projeto)
- PostgreSQL 16 instalado e rodando localmente

---

## Como rodar o projeto

### Opção A: Docker Compose (mais simples)

Sobe o banco de dados e a aplicação juntos, sem precisar instalar nada além do Docker.

```bash
# 1. Clone o repositório e entre na pasta do backend
cd ES_final_project-main/backend

# 2. Suba tudo com um único comando
docker compose up --build
```

Aguarde as mensagens de inicialização. Quando aparecer `Started EsClinicProjectApplication`, a API estará disponível em:

- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html

Para parar:
```bash
docker compose down
```

Para parar e apagar os dados do banco:
```bash
docker compose down -v
```

---

### Opção B: Execução local (PostgreSQL já instalado)

**1. Crie o banco de dados e o usuário no PostgreSQL:**

```sql
CREATE USER user_clinica WITH PASSWORD 'papaestc';
CREATE DATABASE "ES_clinica_bd" OWNER user_clinica;
```

> Você pode executar esses comandos pelo `psql`, pelo pgAdmin ou pela ferramenta de sua preferência.

**2. Verifique o `application.properties`:**

O arquivo `src/main/resources/application.properties` já vem configurado para conectar localmente:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ES_clinica_bd
spring.datasource.username=user_clinica
spring.datasource.password=papaestc
api.security.token.secret=clinica-medica-secret-dev
```

Se o seu PostgreSQL roda em outra porta ou com credenciais diferentes, ajuste este arquivo antes de subir a aplicação.

**3. Compile e execute:**

```bash
# Com o Maven Wrapper incluído no projeto (não precisa ter Maven instalado)
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows
```

A API estará disponível em http://localhost:8080.

---

## Migrações do banco de dados (Flyway)

As tabelas são criadas **automaticamente** pelo Flyway ao iniciar a aplicação. Não é necessário rodar nenhum script SQL manualmente. A ordem das migrações é:

| Arquivo | O que cria |
|---|---|
| `V1` | Tabela `medicos` |
| `V2` | Tabela `pacientes` |
| `V3` | Tabela `usuarios` |
| `V4` | Tabela `consultas` |
| `V5` | Coluna `medico_id` em `usuarios` (vínculo médico ↔ usuário) |

> **Atenção:** nunca edite ou renomeie arquivos de migração já aplicados. O Flyway valida o checksum de cada arquivo e vai recusar a inicialização se detectar alteração.

---

## Autenticação

A API usa **JWT stateless**. Para acessar qualquer endpoint (exceto `/auth/login` e o Swagger), é necessário incluir o token no header:

```
Authorization: Bearer <token>
```

**Login:**

```http
POST /auth/login
Content-Type: application/json

{
  "login": "seu_usuario",
  "senha": "sua_senha"
}
```

A resposta retorna o token JWT que deve ser usado nas demais requisições.

**Criando o primeiro usuário administrador:**

Como o endpoint `POST /usuarios` exige papel `ADMINISTRADOR`, o primeiro usuário precisa ser inserido diretamente no banco:

```sql
-- Gere o hash BCrypt de uma senha antes de inserir.
-- Você pode usar: https://bcrypt-generator.com/ (12 rounds)

INSERT INTO usuarios (login, senha, papel)
VALUES ('admin', '$2a$12$SEU_HASH_AQUI', 'ADMINISTRADOR');
```

---

## Papéis e permissões

| Papel | Descrição |
|---|---|
| `ADMINISTRADOR` | Acesso total, incluindo criação de usuários |
| `RECEPCIONISTA` | Cadastro e gestão de pacientes e consultas |
| `MEDICO` | Vinculado a um registro de médico, acesso às próprias consultas |

Os endpoints utilizam `@Secured` do Spring Security. Tentativas de acesso sem o papel adequado retornam `403 Forbidden`.

---

## Endpoints principais

Todos os endpoints estão documentados interativamente no Swagger UI em `/swagger-ui.html`. Um resumo:

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/auth/login` | Autenticação (público) |
| `GET` | `/usuarios/me` | Dados do usuário autenticado |
| `POST` | `/usuarios` | Cadastrar usuário (ADMINISTRADOR) |
| `GET/POST` | `/medicos` | Listar / cadastrar médicos |
| `PUT/DELETE` | `/medicos/{id}` | Atualizar / inativar médico |
| `GET/POST` | `/pacientes` | Listar / cadastrar pacientes |
| `PUT/DELETE` | `/pacientes/{id}` | Atualizar / inativar paciente |
| `POST` | `/consultas` | Agendar consulta |
| `GET` | `/dashboard` | Dados resumidos do dashboard |

---

## Validações de agendamento

Ao agendar uma consulta (`POST /consultas`), o sistema aplica automaticamente as seguintes regras:

- **Horário de funcionamento:** consultas somente de segunda a sábado, das 07h às 19h
- **Paciente/médico ativos:** nenhum dos dois pode estar inativo no sistema
- **Conflito de médico:** o mesmo médico não pode ter duas consultas no mesmo horário
- **Consulta duplicada:** o mesmo paciente não pode ter duas consultas no mesmo dia

Se nenhum `medicoId` for informado, a especialidade é obrigatória e o sistema escolhe automaticamente um médico disponível.

---

## CORS

O backend aceita requisições de:
- `http://localhost:3000` (React)
- `http://localhost:5173` (Vite/Next.js dev)

Se o frontend rodar em outra porta, adicione a origem em `CorsConfig.java`:

```java
.allowedOrigins(
    "http://localhost:3000",
    "http://localhost:5173",
    "http://localhost:OUTRA_PORTA"  // adicione aqui
)
```

---

## Problemas comuns

**Porta 5432 já em uso**

O PostgreSQL local pode estar rodando e conflitando com o container Docker. Pare o serviço local antes de subir o Docker Compose:

```bash
# Linux
sudo service postgresql stop

# macOS (Homebrew)
brew services stop postgresql
```

**Erro de checksum no Flyway**

Ocorre quando um arquivo de migração já aplicado foi modificado. Solução: apague os dados do banco e deixe o Flyway recriar tudo do zero.

```bash
docker compose down -v   # apaga o volume do banco
docker compose up --build
```

**`application.properties` não encontrado**

Certifique-se de estar executando os comandos a partir da pasta `backend/`, não da raiz do repositório.

**Swagger retorna 401**

Por padrão o Swagger UI já está liberado sem autenticação. Se isso mudar, verifique `SecurityConfigurations.java` e confirme que os paths `/swagger-ui/**` e `/v3/api-docs/**` estão no `permitAll()`.
