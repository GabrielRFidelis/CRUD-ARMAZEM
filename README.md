# 📦 Sistema de Controle de Estoque (CRUD em Java + PostgreSQL)

Um sistema robusto para gerenciamento e controle de estoque desenvolvido em **Java** com **JDBC** e banco de dados relacional **PostgreSQL**, estruturado segundo o padrão de arquitetura em camadas (**Model-DAO-Service-Controller**).

---

## 📌 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Regras de Negócio](#-regras-de-negócio)
- [Arquitetura e Padrões](#-arquitetura-e-padrões)
- [Estrutura do Banco de Dados](#-estrutura-do-banco-de-dados)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura de Pastas](#-estrutura-de-pastas)
- [Pré-requisitos](#-pré-requisitos)
- [Como Configurar e Executar](#-como-configurar-e-executar)
- [Parametrização e Segurança](#-parametrização-e-segurança)
- [Boas Práticas Implementadas](#-boas-práticas-implementadas)

---

## 📖 Sobre o Projeto

A aplicação resolve o desafio de controle operacional de estoque em depósitos e comércios. Ela permite gerenciar categorias de itens, cadastrar e manter produtos atualizados e auditar todas as entradas e saídas de mercadorias com histórico detalhado e data/hora das transações.

---

## ✨ Funcionalidades

### 1. 📂 Gestão de Categorias
- Cadastrar nova categoria (com verificação de nomes duplicados).
- Listar todas as categorias cadastradas.
- Atualizar nome de categoria existente.
- Excluir categoria (com validação de integridade referencial).

### 2. 🏷️ Gestão de Produtos
- Cadastrar produto associando a uma categoria existente.
- Listar produtos exibindo ID, nome, descrição, preço, quantidade em estoque e categoria.
- Buscar produto por ID.
- Buscar produtos por aproximação de nome (`ILIKE`).
- Atualizar dados de produtos existentes.
- Excluir produto (com proteção contra exclusão de produtos com movimentações).

### 3. 🔄 Movimentações de Estoque
- **Entrada de Estoque**: Incrementa o estoque atual do produto e gera registro de auditoria.
- **Saída de Estoque**: Baixa no estoque com validação para impedir estoque negativo.
- **Auditoria de Movimentações**:
  - Listagem geral de todas as movimentações realizadas.
  - Histórico filtrado de movimentações por produto.

---

## 🛡️ Regras de Negócio

- **Prevenção de Estoque Negativo**: Uma saída só é autorizada caso haja saldo suficiente disponível em estoque.
- **Integridade Referencial**:
  - Uma categoria não pode ser excluída se houver produtos vinculados a ela.
  - Um produto não pode ser excluído se possuir histórico de movimentações.
- **Nomes Únicos**: Categorias não permitem nomes duplicados (insensível a maiúsculas/minúsculas).
- **Validações Obrigatórias**:
  - Preço e quantidade inicial de produtos não podem ser negativos.
  - A quantidade informada para qualquer movimentação deve ser estritamente maior que zero.

---

## 🏛️ Arquitetura e Padrões

O projeto foi construído separando responsabilidades em camadas desacopladas:

```
[ Usuário / Console ]
         │
         ▼
     [ Menu ] (Interface CLI)
         │
         ▼
   [ Controller ] (Captura e formatação de dados de entrada/saída)
         │
         ▼
    [ Service ] (Validações e regras de negócio)
         │
         ▼
      [ DAO ] (Data Access Object - persistência via JDBC)
         │
         ▼
[ PostgreSQL Database ]
```

- **Model**: Representação das entidades de domínio (`Produto`, `Categoria`, `Movimentacao`).
- **DAO (Data Access Object)**: Execução de consultas e comandos SQL via `PreparedStatement`.
- **Service**: Concentração das regras de negócio, validações e integridade de dados.
- **Controller**: Mediação entre a camada de apresentação (`Menu`) e os serviços.
- **ConnectionFactory**: Padrão *Factory* para centralizar a conexão e parametrização dinâmica.

---

## 🗄️ Estrutura do Banco de Dados

O banco de dados é composto por 3 tabelas relacionais com chaves estrangeiras e restrições (`CHECK` e `UNIQUE`):

```sql
categorias (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL CHECK (preco >= 0),
    quantidade INT NOT NULL DEFAULT 0 CHECK (quantidade >= 0),
    categoria_id INT NOT NULL REFERENCES categorias(id)
);

movimentacoes (
    id SERIAL PRIMARY KEY,
    produto_id INT NOT NULL REFERENCES produtos(id),
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
    quantidade INT NOT NULL CHECK (quantidade > 0),
    data_movimentacao TIMESTAMP NOT NULL DEFAULT NOW()
);
```

---

## 🛠️ Tecnologias Utilizadas

- **Java JDK 17+**
- **PostgreSQL**
- **Driver JDBC PostgreSQL** (`postgresql.jar`)
- **Git**

---

## 📁 Estrutura de Pastas

```plaintext
CRUD/
├── lib/
│   └── postgresql.jar           # Driver JDBC do PostgreSQL
├── src/
│   ├── connection/
│   │   └── ConnectionFactory.java  # Gerenciador de conexões parametrizadas
│   ├── controller/
│   │   ├── CategoriaController.java
│   │   ├── MovimentacaoController.java
│   │   └── ProdutoController.java
│   ├── dao/
│   │   ├── CategoriaDAO.java
│   │   ├── MovimentacaoDAO.java
│   │   └── ProdutoDAO.java
│   ├── model/
│   │   ├── Categoria.java
│   │   ├── Movimentacao.java
│   │   └── Produto.java
│   ├── service/
│   │   ├── CategoriaService.java
│   │   ├── MovimentacaoService.java
│   │   └── ProdutoService.java
│   ├── util/
│   │   └── Menu.java               # Menus interativos no console (CLI)
│   └── Main.java                   # Ponto de entrada da aplicação
├── .gitignore                      # Protege arquivos de compilação e senhas
├── compile.bat                     # Script de compilação e execução rápida (Windows)
├── db.properties.example           # Modelo de configuração de banco de dados
├── db.properties                   # Configurações locais (ignorado no Git)
├── init.sql                        # Script DDL de criação das tabelas
└── README.md                       # Documentação do projeto
```

---

## 🔐 Parametrização e Segurança

Nenhuma senha ou porta sensível fica exposta no código-fonte. A classe `ConnectionFactory` busca os parâmetros na seguinte ordem de prioridade:

1. **Variáveis de Ambiente**: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
2. **Arquivo `db.properties`**: lido na raiz ou no classpath da aplicação.
3. **Valores padrão (fallback)**: `localhost`, porta `5432`, banco `estoque_db`, usuário `postgres`.

### Configurando o `db.properties`:
Copie o modelo `db.properties.example` para `db.properties`:
```properties
db.host=localhost
db.port=5432
db.name=estoque_db
db.user=postgres
db.password=sua_senha_aqui
```
> O arquivo `db.properties` já está listado no `.gitignore` para evitar vazamentos acidentais no GitHub.

---

## 🚀 Como Configurar e Executar

### 1. Clonar o repositório
```bash
git clone https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git
cd SEU_REPOSITORIO
```

### 2. Configurar o Banco de Dados
1. Inicie seu serviço do PostgreSQL.
2. Crie a base de dados:
   ```sql
   CREATE DATABASE estoque_db;
   ```
3. Conecte-se à base `estoque_db` e execute o script `init.sql`.

### 3. Configurar Credenciais
Crie o arquivo `db.properties` na raiz do projeto com base em `db.properties.example` e informe suas credenciais locais.

### 4. Driver JDBC
Certifique-se de que o arquivo `.jar` do driver do PostgreSQL esteja presente na pasta `lib/` com o nome `postgresql.jar`. Caso não esteja, baixe em:
👉 [PostgreSQL JDBC Driver Download](https://jdbc.postgresql.org/download/)

### 5. Compilação e Execução

#### Opção A: Via script `compile.bat` (Windows)
Execute diretamente no terminal:
```cmd
compile.bat
```

#### Opção B: Manualmente via Terminal
```bash
# Criar diretório de saída
mkdir out

# Compilar
javac -encoding UTF-8 -cp "lib/postgresql.jar" -d out src/Main.java src/connection/ConnectionFactory.java src/model/*.java src/dao/*.java src/service/*.java src/controller/*.java src/util/Menu.java

# Executar
java -cp "out;lib/postgresql.jar" Main
```

---

## 🔒 Boas Práticas Adotadas

- **Proteção contra SQL Injection**: Uso exclusivo de `PreparedStatement` parametrizado em todas as operações SQL.
- **Isolamento de Conexões**: Fechamento seguro de `Connection`, `PreparedStatement` e `ResultSet` garantindo liberação imediata de recursos.
- **Fail-Fast & Validações**: Regras de negócio tratadas na camada de `Service`, evitando sobrecarga no banco.
- **Separação de Preocupações (SoC)**: Camadas desacopladas permitindo fácil migração para interfaces visuais ou APIs REST.

---

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE).

