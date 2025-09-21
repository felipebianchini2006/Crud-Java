# 🚀 Guia de Instalação - Biblioteca Online

## 📋 Pré-requisitos

Antes de executar a aplicação, certifique-se de ter instalado:

- **Java 17 ou superior**
- **Maven 3.6+**
- **PostgreSQL 12+**
- **Git** (para clonar o repositório)

## 🗄️ Configuração do Banco de Dados

### 1. Instalar PostgreSQL

#### Windows:
1. Baixe o PostgreSQL em: https://www.postgresql.org/download/windows/
2. Execute o instalador e siga as instruções
3. Anote a senha do usuário `postgres`

#### Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo -u postgres psql
\password postgres
# Digite: 123456
\q
```

#### macOS:
```bash
brew install postgresql
brew services start postgresql
psql postgres
\password postgres
# Digite: 123456
\q
```

### 2. Criar o Banco de Dados

```sql
-- Conecte-se ao PostgreSQL como usuário postgres
psql -U postgres -h localhost

-- Crie o banco de dados
CREATE DATABASE crudjava;

-- Verifique se foi criado
\l

-- Saia do PostgreSQL
\q
```

## ⚙️ Configuração da Aplicação

### 1. Clonar o Repositório

```bash
git clone <url-do-repositorio>
cd Crud-Java
```

### 2. Verificar Configurações

O arquivo `application.properties` já está configurado com:

```properties
# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/crudjava
spring.datasource.username=postgres
spring.datasource.password=123456

# Se sua senha for diferente, altere aqui
```

### 3. Compilar e Executar

```bash
# Limpar e compilar
mvn clean compile

# Executar a aplicação
mvn spring-boot:run
```

## 🌐 Acessando o Sistema

### URLs Principais:
- **Aplicação**: http://localhost:8080
- **API REST**: http://localhost:8080/api/

### 👥 Usuários Padrão:

| Tipo | Email | Senha | Descrição |
|------|-------|-------|-----------|
| **Admin** | admin@biblioteca.com | password123 | Controle total do sistema |
| **Autor** | joao.silva@email.com | password123 | Pode publicar livros |
| **Leitor** | maria.santos@email.com | password123 | Pode emprestar livros |

## 📁 Estrutura de Diretórios

A aplicação criará automaticamente os seguintes diretórios:

```
projeto-raiz/
├── uploads/
│   ├── profiles/    # Fotos de perfil dos usuários
│   └── covers/      # Capas dos livros
└── logs/           # Logs da aplicação (se configurado)
```

## 🔧 Solução de Problemas

### Erro de Conexão com Banco

```
Error: Connection refused
```

**Solução:**
1. Verifique se o PostgreSQL está rodando:
   ```bash
   # Windows
   services.msc (procure por PostgreSQL)
   
   # Linux
   sudo systemctl status postgresql
   
   # macOS
   brew services list | grep postgresql
   ```

2. Teste a conexão:
   ```bash
   psql -U postgres -h localhost -d crudjava
   ```

### Erro de Porta em Uso

```
Error: Port 8080 already in use
```

**Solução:**
1. Altere a porta no `application.properties`:
   ```properties
   server.port=8081
   ```

2. Ou mate o processo na porta 8080:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   
   # Linux/macOS
   lsof -ti:8080 | xargs kill -9
   ```

### Erro de Permissão de Upload

```
Error: Cannot create directory uploads/
```

**Solução:**
1. Crie os diretórios manualmente:
   ```bash
   mkdir -p uploads/profiles uploads/covers
   ```

2. Ajuste as permissões (Linux/macOS):
   ```bash
   chmod 755 uploads/
   chmod 755 uploads/profiles/
   chmod 755 uploads/covers/
   ```

## 🧪 Testando a Instalação

### 1. Verificar se a aplicação iniciou:
```bash
curl http://localhost:8080
# Deve retornar a página inicial
```

### 2. Testar API:
```bash
curl http://localhost:8080/api/books
# Deve retornar lista de livros (pode estar vazia)
```

### 3. Testar login:
1. Acesse: http://localhost:8080/login
2. Use: admin@biblioteca.com / password123
3. Deve redirecionar para o dashboard administrativo

## 📊 Monitoramento

### Logs da Aplicação:
```bash
# Ver logs em tempo real
tail -f logs/application.log

# Ou via Maven
mvn spring-boot:run | grep -E "(ERROR|WARN|INFO)"
```

### Verificar Banco de Dados:
```sql
-- Conectar ao banco
psql -U postgres -h localhost -d crudjava

-- Ver tabelas criadas
\dt

-- Ver usuários cadastrados
SELECT id, name, email, role FROM users;

-- Ver livros cadastrados
SELECT id, title, author, available FROM book;
```

## 🔄 Atualizações

Para atualizar a aplicação:

```bash
# Parar a aplicação (Ctrl+C)

# Atualizar código
git pull origin main

# Recompilar
mvn clean compile

# Executar novamente
mvn spring-boot:run
```

## 📞 Suporte

Se encontrar problemas:

1. **Verifique os logs** da aplicação
2. **Confirme as configurações** do banco de dados
3. **Teste a conectividade** com o PostgreSQL
4. **Verifique as permissões** de arquivo

---

**✅ Instalação concluída com sucesso!**

Agora você pode acessar http://localhost:8080 e começar a usar a Biblioteca Online! 🎉
