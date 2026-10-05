# Flash Cards Language — API

API REST da aplicação **Flash Cards Language**, uma plataforma para aprender idiomas com flashcards. Ela gerencia usuários, decks, flashcards, sessões de estudo e estatísticas de evolução, com autenticação via JWT.

> Esta API é consumida pelo [frontend web](https://github.com/Renan-Silva235/Flashcards_Web) (React) e pelo [app mobile](https://github.com/Renan-Silva235/Frontend-FlashCards-Mobile) (React Native/Expo).

---

## ✨ Funcionalidades

- **Autenticação**
  - Cadastro com verificação do e-mail por código
  - Login com JWT, enviado em cookie `HttpOnly` (web) e no corpo da resposta (mobile)
  - Restauração da sessão (`/auth/me`) e logout
  - Redefinição de senha por código enviado por e-mail, sem revelar quais e-mails estão cadastrados
- **Decks:** criar, listar, buscar, favoritar e excluir
- **Flashcards:** criar, editar, listar por deck, pesquisar e excluir. Cada card tem palavra, tradução, tempos verbais e frases de exemplo
- **Sessões de estudo:** registro de cada resposta (fácil, médio ou difícil), que atualiza a dificuldade do card
- **Estatísticas:** total de cards e distribuição por dificuldade, com filtro por idioma
- **Perfil:** totais de decks, flashcards e favoritos do usuário

---

## 🔒 Segurança

- **JWT** validado em todas as rotas protegidas, lido do cookie (web) ou do header `Authorization: Bearer` (mobile)
- **Controle de acesso por dono do recurso:** o usuário é sempre identificado pelo token, nunca por um id enviado pelo cliente. Decks, cards e sessões de outro usuário respondem `404`, como se não existissem
- **Cookie de login** `HttpOnly`, `Secure` por padrão e `SameSite` configurável
- **Senhas** armazenadas com BCrypt
- **CORS** restrito às origens configuradas

---

## 🛠️ Tecnologias

| Categoria | Ferramentas |
| --- | --- |
| Linguagem | Java 25 |
| Framework | Spring Boot 3.5 (Web, Data JPA, Security, Validation, Mail) |
| Autenticação | JWT (`java-jwt`) |
| Banco de dados | PostgreSQL |
| Migrações | Flyway |
| Documentação | Springdoc OpenAPI |
| Outros | Lombok, dotenv-java |
| Deploy | Docker + Render |

---

## 📁 Estrutura

```
src/main/java/com/flashcards/api/
├── config/         # Segurança, CORS e carregamento do .env
├── controllers/    # Endpoints REST
├── dtos/           # Objetos de entrada (request) e saída (response)
├── entities/       # Entidades JPA
├── enums/          # Dificuldade, status do card, resultado da revisão...
├── exceptions/     # Exceções e tratamento global de erros
├── repositories/   # Acesso ao banco (Spring Data JPA)
├── security/       # Filtro JWT, usuário logado e UserDetails
└── services/       # Regras de negócio

src/main/resources/
├── application.properties
└── db/migration/   # Scripts do Flyway (V1, V2, ...)
```

---

## 🔗 Endpoints

As rotas marcadas com 🔓 são públicas. As demais exigem login.

### Autenticação — `/auth`

| Método | Rota | Descrição |
| --- | --- | --- |
| POST 🔓 | `/auth/register/send-code` | Envia o código de verificação para o e-mail do cadastro |
| POST 🔓 | `/auth/register/verify-code` | Valida o código de cadastro |
| POST 🔓 | `/auth/register` | Cria a conta (exige o código válido) |
| POST 🔓 | `/auth/login` | Faz login e devolve o JWT |
| POST 🔓 | `/auth/logout` | Apaga o cookie de login |
| GET | `/auth/me` | Retorna o usuário logado |
| GET | `/auth/profile/{userId}` | Perfil com os totais do usuário logado |
| POST 🔓 | `/auth/password/send-code` | Envia o código de redefinição de senha |
| POST 🔓 | `/auth/password/verify-code` | Valida o código de redefinição |
| POST 🔓 | `/auth/password/change` | Define a nova senha (exige o código válido) |

### Decks — `/decks`

| Método | Rota | Descrição |
| --- | --- | --- |
| POST | `/decks` | Cria um deck para o usuário logado |
| GET | `/decks/user/{userId}` | Lista os decks do usuário logado (favoritos primeiro) |
| GET | `/decks/{id}` | Detalhes de um deck |
| PATCH | `/decks/{id}/favorite` | Marca ou desmarca como favorito |
| DELETE | `/decks/{id}` | Exclui o deck, com seus cards e histórico |

### Flashcards — `/flashcards`

| Método | Rota | Descrição |
| --- | --- | --- |
| POST | `/flashcards` | Cria um card em um deck |
| PUT | `/flashcards/{id}` | Edita um card |
| GET | `/flashcards/deck/{deckId}` | Lista os cards de um deck (os mais difíceis primeiro) |
| GET | `/flashcards/search?q=` | Pesquisa por palavra ou tradução nos cards do usuário |
| DELETE | `/flashcards/{id}` | Exclui um card |

### Sessões de estudo — `/study-sessions`

| Método | Rota | Descrição |
| --- | --- | --- |
| POST | `/study-sessions/start` | Inicia uma sessão de estudo em um deck |
| POST | `/study-sessions/{id}/review` | Registra a resposta de um card (`MISTAKE`, `DIFFICULT` ou `HIT`) |
| PUT | `/study-sessions/{id}/end` | Encerra a sessão |

### Estatísticas — `/statistics`

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/statistics?language=` | Total de cards e quantidade por dificuldade (idioma opcional) |

A especificação OpenAPI fica disponível em `/v3/api-docs`.

---

## 🚀 Como rodar localmente

### Pré-requisitos

- [Java 25](https://adoptium.net)
- [PostgreSQL](https://www.postgresql.org) rodando localmente
- Uma conta de envio de e-mail (SMTP/Brevo) para os códigos de verificação

O Maven não precisa ser instalado: o projeto usa o Maven Wrapper (`./mvnw`).

### Passo a passo

```bash
# 1. Clone o repositório
git clone https://github.com/Renan-Silva235/Api-Flash-Cards.git
cd Api-Flash-Cards

# 2. Crie o banco de dados no PostgreSQL
createdb flashcards

# 3. Crie o arquivo .env na raiz do projeto (veja as variáveis abaixo)

# 4. Inicie a API
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. As tabelas são criadas automaticamente pelo **Flyway** na primeira execução.

### Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto (ele está no `.gitignore` e não vai para o GitHub):

```env
# Banco de dados
DB_URL=jdbc:postgresql://localhost:5432/flashcards
DB_USERNAME=postgres
DB_PASSWORD=sua_senha

# JWT
JWT_SECRET=uma_chave_secreta_longa_e_aleatoria

# E-mail (códigos de verificação)
SMTP_MAIL_HOST=smtp.seu-provedor.com
SMTP_MAIL_PORT=587
SMTP_MAIL_USERNAME=seu_usuario
SMTP_MAIL_PASSWORD=sua_senha
SMTP_MAIL_EMAIL_CORPORATION=remetente@seu-dominio.com
BREVO_API_KEY=sua_chave_brevo

# Desenvolvimento local em HTTP (em produção não defina: o padrão é true)
COOKIE_SECURE=false
```

| Variável | Obrigatória | Descrição |
| --- | --- | --- |
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | Sim | Conexão com o PostgreSQL |
| `JWT_SECRET` | Sim | Chave usada para assinar os tokens |
| `SMTP_MAIL_*`, `BREVO_API_KEY` | Sim | Envio dos e-mails com os códigos |
| `COOKIE_SECURE` | Não | Cookie só por HTTPS. Padrão: `true` |
| `COOKIE_SAME_SITE` | Não | Atributo `SameSite` do cookie. Padrão: `Lax` |
| `CORS_ALLOWED_ORIGINS` | Não | Origens liberadas, separadas por vírgula. Padrão: `http://localhost:5173,http://localhost:3000` |

---

## 🐳 Docker

```bash
docker build -t flashcards-api .
docker run -p 8080:8080 --env-file .env flashcards-api
```

---

## ☁️ Deploy

A API está publicada no **Render** usando o `Dockerfile` do projeto. As variáveis de ambiente são cadastradas no painel do Render.

No plano gratuito, o servidor desliga após 15 minutos sem uso, e a primeira requisição seguinte pode levar de 40 segundos a 1 minuto para responder.

---

## 👤 Autor

Desenvolvido por **Renan Silva** — [GitHub](https://github.com/Renan-Silva235)
