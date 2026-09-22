-- =============================================================
-- Script de inicialização do banco de dados - Sistema de Estoque
-- Execute este script no PostgreSQL antes de rodar a aplicação
-- =============================================================

-- Criar o banco (execute separadamente se necessário)
-- CREATE DATABASE estoque_db;

-- Conectar ao banco estoque_db antes de executar o restante

CREATE TABLE IF NOT EXISTS categorias (
    id    SERIAL       PRIMARY KEY,
    nome  VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS produtos (
    id            SERIAL        PRIMARY KEY,
    nome          VARCHAR(150)  NOT NULL,
    descricao     TEXT,
    preco         DECIMAL(10,2) NOT NULL CHECK (preco >= 0),
    quantidade    INT           NOT NULL DEFAULT 0 CHECK (quantidade >= 0),
    categoria_id  INT           NOT NULL,
    CONSTRAINT fk_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE IF NOT EXISTS movimentacoes (
    id                  SERIAL      PRIMARY KEY,
    produto_id          INT         NOT NULL,
    tipo                VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
    quantidade          INT         NOT NULL CHECK (quantidade > 0),
    data_movimentacao   TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);