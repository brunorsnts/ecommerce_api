# 🛒 Ecommerce API (DSCatalog)

Uma aplicação de catálogo de produtos (e-commerce) desenvolvida em **Java com Spring Boot**. Este projeto implementa boas práticas de arquitetura em camadas e disponibiliza uma API REST robusta para a gestão de produtos, categorias e usuários, protegida com autenticação baseada em tokens.

## ⚙️ Estrutura do Repositório

O projeto está organizado num formato de *monorepo* que inclui:
- 📁 **`backend/`**: A API REST principal desenvolvida em Java, com CRUD completo já implementado.
- 📁 **`frontend/`**: Interface de usuário da aplicação (atualmente conta apenas com a **tela de login**).

## ✨ Funcionalidades Principais (Backend)

- **CRUD Completo:** Gestão integral de Produtos, Categorias e Usuários via API.
- **Autenticação e Autorização:** Segurança implementada com Spring Security, OAuth2 e JWT (JSON Web Tokens).
- **Paginação:** Resultados de listagens paginados para otimização de desempenho e consumo de dados.
- **Arquitetura Limpa:** Estrutura organizada em camadas lógicas (`Controller` → `Service` → `Repository`).

## 🛠 Tecnologias Utilizadas

| Categoria | Tecnologia |
| :--- | :--- |
| **Linguagem** | Java 17+ e JavaScript/TypeScript |
| **Framework Backend** | Spring Boot |
| **Segurança** | Spring Security, OAuth2, JWT |
| **Persistência de Dados**| JPA / Hibernate |
| **Banco de Dados** | MySQL (Executado via Docker Compose) |
| **Testes** | JUnit, Mockito |
| **Gerenciador de Dependências**| Maven (Backend) e NPM/Yarn (Frontend) |
| **Infraestrutura** | Docker e Docker Compose |

## 📋 Pré-requisitos

Antes de executar o projeto localmente, certifique-se de ter instalado na sua máquina:
- **Java 17** (ou versão superior)
- **Node.js** e **npm** (para rodar o frontend)
- **Maven**
- **Docker** e **Docker Compose**
- Uma IDE da sua preferência (IntelliJ IDEA, VS Code, Eclipse, etc.)

## 🚀 Como Executar o Projeto (Backend)

1. Faça o clone deste repositório:
```bash
git clone [https://github.com/brunorsnts/ecommerce_api.git](https://github.com/brunorsnts/ecommerce_api.git)
```

2. Acesse a pasta do backend:
```bash
cd ecommerce_api/backend
```

3. Suba o container do MySQL utilizando o Docker Compose:
```bash
docker-compose up -d
```

4. Compile e inicie o servidor Spring Boot:
```bash
./mvnw spring-boot:run
```
*(Em alternativa, após subir o banco de dados com o Docker, você pode importar o projeto na sua IDE e executar a classe principal do Spring Boot).*

## 🖥️ Como Executar o Projeto (Frontend)

1. Abra um novo terminal e acesse a pasta do frontend:
```bash
cd ecommerce_api/frontend
```

2. Instale as dependências do projeto:
```bash
npm install
```

3. Inicie o servidor de desenvolvimento:
```bash
npm run dev
```
*(O frontend com a tela de login estará disponível no seu navegador, geralmente no endereço fornecido no terminal, como `http://localhost:3000` ou `http://localhost:5173`).*

## 🔐 Credenciais de Acesso (Padrão)

A aplicação já sobe com um usuário administrador pré-configurado no banco de dados para facilitar os testes (tanto via API quanto na tela de login do front). Utilize as credenciais abaixo:

- **Usuário (E-mail):** `admin`
- **Senha:** `12345678`

## 🔌 Testando a API

Para testar os demais *endpoints* da aplicação que ainda não possuem tela, recomenda-se a utilização de ferramentas como o **Postman** ou o **Insomnia**.

**Exemplos de Endpoints:**
- `GET /products` → Lista todos os produtos de forma paginada (rota pública).
- `POST /categories` → Cria uma nova categoria *(requer o token JWT do usuário admin)*.

## 🎯 Ideias para Próximas Melhorias

- [ ] Desenvolver as demais telas do frontend (produtos, categorias, carrinho, etc.).
- [ ] Documentação interativa da API através do Swagger/OpenAPI.
- [ ] *Deploy* automatizado utilizando rotinas de CI/CD.
- [ ] Evoluir e distribuir o sistema em uma arquitetura de microsserviços.

## 🤝 Como Contribuir

As contribuições são muito bem-vindas! Se deseja melhorar este projeto, siga os passos abaixo:

1. Faça um *Fork* do projeto.
2. Crie uma *branch* com a sua nova funcionalidade (`git checkout -b feature/nova-funcionalidade`).
3. Faça *commits* claros e descritivos das suas alterações (`git commit -m 'Adiciona a funcionalidade X'`).
4. Envie as alterações para o seu *fork* (`git push origin feature/nova-funcionalidade`).
5. Abra um *Pull Request* neste repositório.