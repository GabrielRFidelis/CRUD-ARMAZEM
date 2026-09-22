package model;

/**
 * Representa um produto do estoque.
 * Apenas dados — sem lógica de negócio.
 */
public class Produto {

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidade;
    private int categoriaId;
    private String categoriaNome;

    public Produto() {
    }

    public Produto(String nome, String descricao, double preco, int quantidade, int categoriaId) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoriaId = categoriaId;
    }

    public Produto(int id, String nome, String descricao, double preco, int quantidade,
                   int categoriaId, String categoriaNome) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    @Override
    public String toString() {
        return "| ID: " + id
             + " | Nome: " + nome
             + " | Descrição: " + (descricao != null ? descricao : "-")
             + " | Preço: R$ " + String.format("%.2f", preco)
             + " | Qtd: " + quantidade
             + " | Categoria: " + (categoriaNome != null ? categoriaNome : String.valueOf(categoriaId))
             + " |";
    }
}
