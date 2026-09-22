package service;

import dao.ProdutoDAO;
import dao.CategoriaDAO;
import model.Produto;
import model.Categoria;

import java.sql.SQLException;
import java.util.List;

/**
 * Regras de negócio para produtos.
 * Valida dados e integridade antes de delegar ao DAO.
 */
public class ProdutoService {

    private final ProdutoDAO produtoDAO;
    private final CategoriaDAO categoriaDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
        this.categoriaDAO = new CategoriaDAO();
    }

    public void cadastrarProduto(Produto produto) throws SQLException {
        validarCamposObrigatorios(produto);
        validarCategoriaExistente(produto.getCategoriaId());
        produtoDAO.inserir(produto);
    }

    public List<Produto> listarProdutos() throws SQLException {
        return produtoDAO.listar();
    }

    public Produto buscarProdutoPorId(int id) throws SQLException {
        return produtoDAO.buscarPorId(id);
    }

    public List<Produto> buscarProdutosPorNome(String nome) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O termo de busca não pode ser vazio.");
        }
        return produtoDAO.buscarPorNome(nome);
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        validarCamposObrigatorios(produto);

        Produto existente = produtoDAO.buscarPorId(produto.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Produto com ID " + produto.getId() + " não encontrado.");
        }

        validarCategoriaExistente(produto.getCategoriaId());
        produtoDAO.atualizar(produto);
    }

    public void excluirProduto(int id) throws SQLException {
        Produto existente = produtoDAO.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Produto com ID " + id + " não encontrado.");
        }

        if (produtoDAO.existeMovimentacaoVinculada(id)) {
            throw new IllegalStateException(
                "Não é possível excluir o produto '" + existente.getNome()
                + "'. Existem movimentações registradas para ele."
            );
        }

        produtoDAO.excluir(id);
    }

    // ---------- Validações ----------

    private void validarCamposObrigatorios(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }
        if (produto.getPreco() < 0) {
            throw new IllegalArgumentException("O preço do produto não pode ser negativo.");
        }
        if (produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("A quantidade do produto não pode ser negativa.");
        }
    }

    private void validarCategoriaExistente(int categoriaId) throws SQLException {
        Categoria categoria = categoriaDAO.buscarPorId(categoriaId);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria com ID " + categoriaId + " não encontrada.");
        }
    }
}
