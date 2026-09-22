package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma movimentação de estoque (entrada ou saída).
 * Apenas dados — sem lógica de negócio.
 */
public class Movimentacao {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private int id;
    private int produtoId;
    private String produtoNome;
    private String tipo; // "ENTRADA" ou "SAIDA"
    private int quantidade;
    private LocalDateTime dataMovimentacao;

    public Movimentacao() {
    }

    public Movimentacao(int produtoId, String tipo, int quantidade) {
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
    }

    public Movimentacao(int id, int produtoId, String produtoNome, String tipo,
                        int quantidade, LocalDateTime dataMovimentacao) {
        this.id = id;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataMovimentacao = dataMovimentacao;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    @Override
    public String toString() {
        String dataFormatada = (dataMovimentacao != null) ? dataMovimentacao.format(FORMATTER) : "-";
        return "| ID: " + id
             + " | Produto: " + (produtoNome != null ? produtoNome : String.valueOf(produtoId))
             + " | Tipo: " + tipo
             + " | Qtd: " + quantidade
             + " | Data: " + dataFormatada
             + " |";
    }
}
