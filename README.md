# 🛒 Ecommerce API (DSCatalog)

Uma aplicação de catálogo de produtos (e-commerce) desenvolvida em **Java com Spring Boot**. Este projeto implementa boas práticas de arquitetura em camadas e disponibiliza uma API REST robusta para a gestão de produtos, categorias e utilizadores, totalmente protegida com autenticação baseada em tokens.

## ⚙️ Estrutura do Repositório

O projeto está organizado num formato de *monorepo* que inclui:
- 📁 **`backend/`**: A API REST principal desenvolvida em Java.
- 📁 **`frontend/`**: Interface de utilizador da aplicação.

## ✨ Funcionalidades Principais

- **CRUD Completo:** Gestão integral de Produtos, Categorias e Utilizadores.
- **Autenticação e Autorização:** Segurança implementada com Spring Security, OAuth2 e JWT (JSON Web Tokens).
- **Paginação:** Resultados de listagens paginados para otimização de desempenho e consumo de dados.
- **Arquitetura Limpa:** Estrutura organizada em camadas lógicas (`Controller` → `Service` → `Repository`).

## 🛠 Tecnologias Utilizadas

| Categoria | Tecnologia |
| :--- | :--- |
| **Linguagem** | Java 17+ |
| **Framework Backend** | Spring Boot |
| **Segurança** | Spring Security, OAuth2, JWT |
| **Persistência de Dados**| JPA / Hibernate |
| **Base de Dados** | MySQL (Executado via Docker Compose) |
| **Testes** | JUnit, Mockito |
| **Gestor de Dependências**| Maven |
| **Infraestrutura** | Docker e Docker Compose |

## 📋 Pré-requisitos

Antes de executar o projeto localmente, certifique-se de que tem instalado na sua máquina:
- **Java 17** (ou versão superior)
- **Maven**
- **Docker** e **Docker Compose**
- Uma IDE da sua preferência (IntelliJ IDEA, VS Code, Eclipse, etc.)

## 🚀 Como Executar o Projeto (Backend)

1. Faça o clone deste repositório:
```bash
git clone [https://github.com/brunorsnts/ecommerce_api.git](https://github.com/brunorsnts/ecommerce_api.git)
```

2. Aceda à pasta do backend:
```bash
cd ecommerce_api/backend
```

3. Inicie o contentor do MySQL utilizando o Docker Compose:
```bash
docker-compose up -d
```

4. Compile e inicie o servidor Spring Boot:
```bash
./mvnw spring-boot:run
```
*(Em alternativa, após iniciar a base de dados com o Docker, pode importar o projeto na sua IDE e executar a classe principal do Spring Boot).*

## 🔐 Credenciais de Acesso (Padrão)

A aplicação já inicia com um utilizador administrador pré-configurado na base de dados para facilitar os testes. Utilize as credenciais abaixo para gerar o seu token de acesso:

- **Utilizador:** `admin`
- **Password:** `12345678`

## 🔌 Testar a API

Para testar os *endpoints* da aplicação, recomenda-se a utilização de ferramentas como o **Postman** ou o **Insomnia**.

**Exemplos de Endpoints:**
- `GET /products` → Lista todos os produtos de forma paginada (rota pública).
- `POST /categories` → Cria uma nova categoria *(requer o token JWT do utilizador admin)*.

## 🎯 Ideias para Próximas Melhorias

- [ ] Documentação interativa da API através do Swagger/OpenAPI.
- [ ] Conclusão e integração total com o frontend (React, Angular ou Vue).
- [ ] *Deploy* automatizado utilizando rotinas de CI/CD.
- [ ] Evoluir e distribuir o sistema numa arquitetura de microsserviços.

## 🤝 Como Contribuir

As contribuições são muito bem-vindas! Se deseja melhorar este projeto, siga os passos abaixo:

1. Faça um *Fork* do projeto.
2. Crie uma *branch* com a sua nova funcionalidade (`git checkout -b feature/nova-funcionalidade`).
3. Faça *commits* claros e descritivos das suas alterações (`git commit -m 'Adiciona a funcionalidade X'`).
4. Envie as alterações para o seu *fork* (`git push origin feature/nova-funcionalidade`).
5. Abra um *Pull Request* neste repositório.