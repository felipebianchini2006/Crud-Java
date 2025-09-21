# 📁 Configuração de Upload

## Pastas Necessárias

Para o funcionamento correto do upload de imagens, crie as seguintes pastas na raiz do projeto:

```
uploads/
├── covers/     # Capas de livros
└── profiles/   # Fotos de perfil
```

## Comandos para Criar as Pastas

### Windows (PowerShell):
```powershell
New-Item -ItemType Directory -Force -Path "uploads\covers"
New-Item -ItemType Directory -Force -Path "uploads\profiles"
```

### Windows (CMD):
```cmd
mkdir uploads\covers
mkdir uploads\profiles
```

### Linux/Mac:
```bash
mkdir -p uploads/covers
mkdir -p uploads/profiles
```

## Configuração

As pastas `uploads/` estão no .gitignore para não versionar arquivos enviados pelos usuários.

O sistema criará automaticamente as pastas se não existirem durante o primeiro upload.
