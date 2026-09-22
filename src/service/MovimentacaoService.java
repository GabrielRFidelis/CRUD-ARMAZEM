package service;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import model.Movimentacao;
import model.Produto;

import java.sql.SQLException;
import java.util.List;

/**
 * Regras de negócio para movimentações de estoque.
 * Garante que toda alteração de quantidade seja acompanhada de um registro de movimentação.
 */
public class MovimentacaoService {

    private final MovimentacaoDAO movimentacaoDAO;
    private final ProdutoDAO produtoDAO;

    public MovimentacaoService() {
        this.movimentacaoDAO = new MovimentacaoDAO();
        this.produtoDAO = new ProdutoDAO();
    }

    /**
     * Registra uma entrada de estoque: incrementa a quantidade do produto
     * e cria o registro de movimentação.
     */
    public void registrarEntrada(int produtoId, int quantidade) throws SQLException {
        Produto produto = validarProdutoExistente(produtoId);
        validarQuantidade(quantidade);

        int novaQuantidade = produto.getQuantidade() + quantidade;
        produtoDAO.atualizarQuantidade(produtoId, novaQuantidade);

        Movimentacao movimentacao = new Movimentacao(produtoId, "ENTRADA", quantidade);
        movimentacaoDAO.registrar(movimentacao);
    }

    /**
     * Registra uma saída de estoque: decrementa a quantidade do produto
     * e cria o registro de movimentação.
     * Impede que o estoque fique negativo.
     */
    public void registrarSaida(int produtoId, int quantidade) throws SQLException {
        Produto produto = validarProdutoExistente(produtoId);
        validarQuantidade(quantidade);

        int novaQuantidade = produto.getQuantidade() - quantidade;
        if (novaQuantidade < 0) {
            throw new IllegalStateException(
                "Estoque insuficiente para o produto '" + produto.getNome()
                + "'. Estoque atual: " + produto.getQuantidade()
                + ". Quantidade solicitada: " + quantidade + "."
            );
        }

        produtoDAO.atualizarQuantidade(produtoId, novaQuantidade);

        Movimentacao movimentacao = new Movimentacao(produtoId, "SAIDA", quantidade);
        movimentacaoDAO.registrar(movimentacao);
    }

    public List<Movimentacao> listarMovimentacoes() throws SQLException {
        return movimentacaoDAO.listarTodas();
    }

    public List<Movimentacao> listarMovimentacoesPorProduto(int produtoId) throws SQLException {
        validarProdutoExistente(produtoId);
        return movimentacaoDAO.listarPorProduto(produtoId);
    }

    // ---------- Validações ----------

    private Produto validarProdutoExistente(int produtoId) throws SQLException {
        Produto produto = produtoDAO.buscarPorId(produtoId);
        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID " + produtoId + " não encontrado.");
        }
        return produto;
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade da movimentação deve ser maior que zero.");
        }
    }
}
