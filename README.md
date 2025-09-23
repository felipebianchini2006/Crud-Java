# Biblioteca Online

Projeto pessoal feito para aprender mais sobre Java e Spring Boot, criando um sistema simples de cadastro de livros, leitores e empréstimos.

## Como usar

1. Instale o Java 17 e o Maven.
2. No terminal, rode:
   ```bash
   mvn clean package
   mvn spring-boot:run
   ```
3. Acesse [http://localhost:8080/](http://localhost:8080/) no navegador.

## O que tem aqui

- Cadastro e consulta de livros, leitores e empréstimos.
- Interface web simples (HTML/JS).
- Banco de dados em memória (H2), sem precisar instalar nada.

## Observações

- Console do banco: `/h2-console` (usuário: `sa`, senha: em branco).
- Projeto em andamento, pode conter melhorias a serem feitas.

# 📚 Biblioteca Online - Sistema Completo de Gerenciamento

Sistema moderno de gerenciamento de biblioteca desenvolvido com Spring Boot, featuring autenticação, diferentes tipos de usuários e interface responsiva.

## ✨ Funcionalidades Principais

### 🔐 Sistema de Autenticação
- **Login/Cadastro** com validação de dados
- **Três tipos de usuários**: Leitor, Autor e Administrador
- **Perfis personalizáveis** com foto de perfil e biografia
- **Segurança** com Spring Security e criptografia de senhas

### 👥 Gestão de Usuários
- **Leitores**: Podem visualizar e emprestar livros
- **Autores**: Podem publicar e gerenciar seus próprios livros
- **Administradores**: Controle total do sistema

### 📖 Gerenciamento de Livros
- **Catálogo completo** com busca e filtros
- **Upload de capas** para os livros
- **Informações detalhadas**: título, autor, gênero, descrição
- **Sistema de disponibilidade** automático

### 🤝 Sistema de Empréstimos
- **Empréstimos automáticos** com controle de datas
- **Histórico completo** de empréstimos
- **Notificações** de vencimento
- **Controle de devoluções**

### 🎨 Interface Moderna
- **Design responsivo** com Bootstrap 5
- **Dashboards específicos** para cada tipo de usuário
- **Navegação intuitiva** e experiência otimizada
- **Temas visuais** diferenciados por perfil

## 🛠️ Tecnologias Utilizadas

- **Backend**: Java 17, Spring Boot 3.3.3
- **Segurança**: Spring Security 6
- **Banco de Dados**: PostgreSQL
- **ORM**: Spring Data JPA
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Upload**: Multipart File Support
- **Validação**: Bean Validation

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- PostgreSQL
- Maven

### Configuração do Banco de Dados
1. Instale o PostgreSQL
2. Crie um banco chamado `crudjava`
3. Configure usuário `postgres` com senha `123456`

### Executando a Aplicação
```bash
# Clone o repositório
git clone <repository-url>
cd Crud-Java

# Execute a aplicação
mvn spring-boot:run
```

### Acesso ao Sistema
- **URL**: http://localhost:8080
- **Admin padrão**: admin@biblioteca.com / password123
- **Autor exemplo**: joao.silva@email.com / password123
- **Leitor exemplo**: maria.santos@email.com / password123

## 📱 Páginas e Funcionalidades

### 🏠 Página Inicial
- Apresentação do sistema
- Livros em destaque
- Estatísticas gerais
- Acesso rápido ao catálogo

### 🔍 Catálogo de Livros
- **Busca avançada** por título, autor ou gênero
- **Filtros dinâmicos** por categoria
- **Paginação** otimizada
- **Detalhes completos** de cada livro

### 👤 Dashboards Personalizados

#### 📚 Dashboard do Leitor
- Empréstimos ativos
- Histórico de leituras
- Recomendações personalizadas
- Acesso rápido ao catálogo

#### ✍️ Dashboard do Autor
- Gerenciamento de livros publicados
- Estatísticas de empréstimos
- Upload de capas
- Controle de disponibilidade

#### 🛡️ Dashboard do Administrador
- Visão geral do sistema
- Gerenciamento de usuários
- Controle total de livros
- Relatórios e estatísticas

## 🔧 Configurações Avançadas

### Upload de Arquivos
- **Fotos de perfil**: até 5MB
- **Capas de livros**: até 5MB
- **Formatos suportados**: JPG, PNG, GIF
- **Armazenamento**: Sistema de arquivos local

### Segurança
- **Criptografia**: BCrypt para senhas
- **Autorização**: Role-based access control
- **Sessões**: Spring Security session management
- **CSRF**: Proteção habilitada

### Banco de Dados
- **Migrations**: Hibernate DDL auto-update
- **Dados iniciais**: Script SQL com usuários e livros exemplo
- **Relacionamentos**: JPA com lazy loading otimizado

## 📊 Estrutura do Projeto

```
src/main/java/org/example/library/
├── book/           # Entidades e lógica de livros
├── loan/           # Sistema de empréstimos
├── user/           # Gerenciamento de usuários
├── security/       # Configurações de segurança
├── web/            # Controladores web
├── common/         # Utilitários e exceções
└── config/         # Configurações da aplicação

src/main/resources/
├── templates/      # Templates Thymeleaf
│   ├── auth/       # Páginas de autenticação
│   ├── books/      # Páginas de livros
│   ├── user/       # Páginas de usuário
│   ├── reader/     # Dashboard do leitor
│   ├── author/     # Dashboard do autor
│   └── admin/      # Dashboard do administrador
└── static/         # Recursos estáticos
```

## 🎯 Próximas Funcionalidades

- [ ] Sistema de avaliações e comentários
- [ ] Notificações por email
- [ ] Relatórios avançados
- [ ] API REST completa
- [ ] Integração com redes sociais
- [ ] Sistema de reservas
- [ ] Chat entre usuários
