# CRUD Java com Spring Boot

Aplicação Java com Spring Boot que expõe uma API REST completa para gerenciar produtos, incluindo uma interface web estática em `index.html` para consumir o backend diretamente pelo navegador.

## Tecnologias
- Java 17
- Spring Boot 3 (Web, Data JPA, Validation)
- Banco em memória H2
- Maven como gerenciador de dependências
- HTML/CSS/JavaScript puro para a interface web

## Requisitos
- JDK 17 instalado (`java -version`)
- Maven 3.9+ (`mvn -version`)

## Como executar
1. Instale as dependências e gere o artefato:
   ```bash
   mvn clean package
   ```
2. Inicie a aplicação:
   ```bash
   mvn spring-boot:run
   ```
3. Acesse a interface web em [http://localhost:8080/](http://localhost:8080/).

Para encerrar, pressione `Ctrl + C` no terminal em execução.

## Endpoints principais
| Método | Caminho                 | Descrição                      |
|--------|-------------------------|--------------------------------|
| GET    | `/api/products`         | Lista todos os produtos        |
| POST   | `/api/products`         | Cria um novo produto           |
| GET    | `/api/products/{id}`    | Obtém um produto por ID        |
| PUT    | `/api/products/{id}`    | Atualiza um produto existente  |
| DELETE | `/api/products/{id}`    | Remove um produto              |

> **Exemplo de criação (JSON):**
> ```json
> {
>   "name": "Mouse Gamer",
>   "description": "Mouse com 6 botões programáveis",
>   "price": 199.90,
>   "stock": 12
> }
> ```

## Interface Web
A página `src/main/resources/static/index.html` funciona como painel administrativo:
- Formulário para cadastrar produtos.
- Listagem com opções de editar e excluir.
- Formulário de edição preenchido automaticamente ao selecionar "Editar".

Tudo é feito via `fetch` contra a API REST, sem frameworks adicionais.

## Banco de dados H2
- Console acessível em [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- URL JDBC: `jdbc:h2:mem:cruddb`
- Usuário: `sa`
- Senha: *(vazia)*

## Estrutura do projeto
```
src/
 └─ main/
     ├─ java/org/example
     │   ├─ CrudJavaApplication.java
     │   ├─ common/                # Handler global de erros
     │   └─ product/               # Entidade, DTOs, controller e serviço
     └─ resources/
         ├─ application.properties # Configurações Spring/H2
         └─ static/index.html      # Interface web do CRUD
```

## Testes
Atualmente não há testes automatizados. Recomenda-se adicionar testes de integração com Spring Boot Test cobrindo os cenários principais do CRUD.