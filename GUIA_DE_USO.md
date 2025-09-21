# 📚 GUIA DE USO - SISTEMA DE BIBLIOTECA ONLINE

## 🚀 Como Usar o Sistema

### 🔐 **1. ACESSO AO SISTEMA**

**URL**: http://localhost:8080

**Credenciais de Teste**:
- **👤 Leitor**: maria.santos@email.com / password123
- **✍️ Autor**: joao.silva@email.com / password123  
- **🛡️ Admin**: admin@biblioteca.com / password123

---

## 👤 **FUNCIONALIDADES PARA LEITORES**

### 📖 **Dashboard do Leitor**
- Acesse: `/reader/dashboard`
- Visualize suas estatísticas de empréstimos
- Veja empréstimos ativos com botões de devolução
- Acesse links rápidos para catálogo e histórico

### 📚 **Emprestar Livros**
1. Acesse o catálogo: `/books`
2. Clique em um livro disponível
3. Na página de detalhes, clique em **"Emprestar Livro"**
4. Confirme o empréstimo (14 dias automáticos)
5. O livro aparecerá no seu dashboard

### 🔄 **Devolver Livros**
1. No dashboard, clique em **"Devolver"** ao lado do livro
2. Ou acesse "Meus Empréstimos": `/loans/my-loans`
3. Confirme a devolução
4. O livro ficará disponível novamente

### 📋 **Histórico de Empréstimos**
- Acesse: `/loans/my-loans`
- Veja todos os empréstimos (ativos e devolvidos)
- Monitore datas de vencimento
- Visualize estatísticas completas

---

## ✍️ **FUNCIONALIDADES PARA AUTORES**

### 📊 **Dashboard do Autor**
- Acesse: `/author/dashboard`
- Visualize estatísticas dos seus livros
- Monitore empréstimos e disponibilidade
- Acesse gerenciamento de livros

### 📖 **Gerenciar Livros**
- **Listar livros**: `/author/books`
- **Adicionar novo**: `/author/books/new`
- **Ver detalhes**: `/author/books/{id}`
- **Editar**: `/author/books/{id}/edit`

### 🖼️ **Upload de Capas**
1. Acesse os detalhes do seu livro
2. Clique em **"Adicionar/Alterar Capa"**
3. Selecione uma imagem (JPG, PNG, GIF - máx 5MB)
4. Confirme o upload
5. A capa aparecerá no catálogo

### 📝 **Publicar Livros**
1. Acesse: `/author/books/new`
2. Preencha: título, autor, gênero, descrição
3. Clique em **"Publicar"**
4. Adicione uma capa posteriormente

---

## 🛡️ **FUNCIONALIDADES PARA ADMINISTRADORES**

### 🎛️ **Dashboard Administrativo**
- Acesse: `/admin/dashboard`
- Visualize estatísticas gerais do sistema
- Monitore usuários, livros e empréstimos
- Acesse todas as seções administrativas

### 👥 **Gerenciar Usuários**
- Acesse: `/admin/users`
- Visualize todos os usuários cadastrados
- Monitore tipos de usuário e status
- Controle ativação/desativação

### 📚 **Gerenciar Livros**
- Acesse: `/admin/books`
- Controle todo o catálogo
- Visualize informações completas
- Gerencie disponibilidade

### 🤝 **Gerenciar Empréstimos**
- Acesse: `/admin/loans`
- Monitore todos os empréstimos do sistema
- Force devoluções quando necessário
- Visualize empréstimos em atraso

### 📊 **Relatórios**
- Acesse: `/admin/reports`
- Gere relatórios de usuários, livros e empréstimos
- Analise estatísticas do sistema
- Monitore performance da biblioteca

---

## 🔧 **FUNCIONALIDADES GERAIS**

### 👤 **Gerenciar Perfil**
- Acesse: `/profile`
- Atualize informações pessoais
- Faça upload de foto de perfil
- Edite biografia e contatos

### 🔍 **Catálogo de Livros**
- Acesse: `/books`
- Navegue por todos os livros
- Use filtros de busca
- Visualize detalhes completos

### 🔐 **Autenticação**
- **Login**: `/login`
- **Cadastro**: `/register`
- **Logout**: Link no menu superior

---

## 🎯 **FLUXO TÍPICO DE USO**

### **Para um Leitor**:
1. Faça login → Dashboard
2. Explore catálogo → Escolha livro
3. Empreste livro → Confirme
4. Leia por 14 dias
5. Devolva no dashboard

### **Para um Autor**:
1. Faça login → Dashboard
2. Publique novo livro
3. Adicione capa
4. Monitore empréstimos
5. Edite conforme necessário

### **Para um Admin**:
1. Faça login → Dashboard
2. Monitore sistema
3. Gerencie usuários/livros
4. Controle empréstimos
5. Gere relatórios

---

## ⚡ **DICAS IMPORTANTES**

- ✅ Empréstimos são automáticos por 14 dias
- ✅ Apenas leitores e admins podem emprestar
- ✅ Autores podem gerenciar apenas seus livros
- ✅ Upload de imagens: máx 5MB
- ✅ Sistema responsivo (funciona em mobile)
- ✅ Todas as ações têm confirmação
- ✅ Mensagens de sucesso/erro são exibidas

---

## 🆘 **RESOLUÇÃO DE PROBLEMAS**

**Erro ao emprestar**: Verifique se o livro está disponível
**Upload falha**: Confirme formato (JPG/PNG/GIF) e tamanho (<5MB)
**Acesso negado**: Verifique se está logado com o tipo correto de usuário
**Página não carrega**: Verifique se a aplicação está rodando na porta 8080

---

## 📞 **SUPORTE**

Para dúvidas ou problemas:
- Verifique este guia primeiro
- Consulte o README.md para configurações técnicas
- Teste com as credenciais fornecidas

**🎉 Aproveite seu Sistema de Biblioteca Online! 🎉**
