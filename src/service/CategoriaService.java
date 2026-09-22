package service;

import dao.CategoriaDAO;
import model.Categoria;

import java.sql.SQLException;
import java.util.List;

/**
 * Regras de negócio para categorias.
 * Valida dados antes de delegar ao DAO.
 */
public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
    }

    public void cadastrarCategoria(Categoria categoria) throws SQLException {
        validarNome(categoria.getNome());
        validarNomeDuplicado(categoria.getNome());
        categoriaDAO.inserir(categoria);
    }

    public List<Categoria> listarCategorias() throws SQLException {
        return categoriaDAO.listar();
    }

    public Categoria buscarCategoriaPorId(int id) throws SQLException {
        return categoriaDAO.buscarPorId(id);
    }

    public void atualizarCategoria(Categoria categoria) throws SQLException {
        validarNome(categoria.getNome());

        Categoria existente = categoriaDAO.buscarPorId(categoria.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Categoria com ID " + categoria.getId() + " não encontrada.");
        }

        // Verifica duplicidade apenas se o nome mudou
        if (!existente.getNome().equalsIgnoreCase(categoria.getNome())) {
            validarNomeDuplicado(categoria.getNome());
        }

        categoriaDAO.atualizar(categoria);
    }

    public void excluirCategoria(int id) throws SQLException {
        Categoria existente = categoriaDAO.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Categoria com ID " + id + " não encontrada.");
        }

        if (categoriaDAO.existeProdutoVinculado(id)) {
            throw new IllegalStateException(
                "Não é possível excluir a categoria '" + existente.getNome()
                + "'. Existem produtos vinculados a ela."
            );
        }

        categoriaDAO.excluir(id);
    }

    // ---------- Validações ----------

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria não pode ser vazio.");
        }
    }

    private void validarNomeDuplicado(String nome) throws SQLException {
        Categoria existente = categoriaDAO.buscarPorNome(nome);
        if (existente != null) {
            throw new IllegalArgumentException("Já existe uma categoria com o nome '" + nome + "'.");
        }
    }
}
