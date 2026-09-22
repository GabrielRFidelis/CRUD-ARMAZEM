package controller;

import model.Categoria;
import service.CategoriaService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Recebe dados do usuário via console e delega ao CategoriaService.
 * Nunca acessa o banco diretamente.
 */
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final Scanner scanner;

    public CategoriaController(Scanner scanner) {
        this.categoriaService = new CategoriaService();
        this.scanner = scanner;
    }

    public void cadastrarCategoria() {
        System.out.println("\n===== CADASTRAR CATEGORIA =====");
        System.out.print("Nome da categoria: ");
        String nome = scanner.nextLine().trim();

        try {
            Categoria categoria = new Categoria(nome);
            categoriaService.cadastrarCategoria(categoria);
            System.out.println("Categoria cadastrada com sucesso! ID: " + categoria.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao cadastrar categoria: " + e.getMessage());
        }
    }

    public void listarCategorias() {
        System.out.println("\n===== LISTA DE CATEGORIAS =====");

        try {
            List<Categoria> categorias = categoriaService.listarCategorias();

            if (categorias.isEmpty()) {
                System.out.println("Nenhuma categoria cadastrada.");
                return;
            }

            for (Categoria categoria : categorias) {
                System.out.println(categoria);
            }
            System.out.println("Total: " + categorias.size() + " categoria(s).");
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao listar categorias: " + e.getMessage());
        }
    }

    public void atualizarCategoria() {
        System.out.println("\n===== ATUALIZAR CATEGORIA =====");

        int id = lerInteiro("ID da categoria: ");
        if (id == -1) return;

        try {
            Categoria existente = categoriaService.buscarCategoriaPorId(id);
            if (existente == null) {
                System.out.println("[ERRO] Categoria com ID " + id + " não encontrada.");
                return;
            }

            System.out.println("Categoria atual: " + existente);
            System.out.print("Novo nome (pressione ENTER para manter '" + existente.getNome() + "'): ");
            String novoNome = scanner.nextLine().trim();

            if (novoNome.isEmpty()) {
                novoNome = existente.getNome();
            }

            Categoria categoria = new Categoria(id, novoNome);
            categoriaService.atualizarCategoria(categoria);
            System.out.println("Categoria atualizada com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao atualizar categoria: " + e.getMessage());
        }
    }

    public void excluirCategoria() {
        System.out.println("\n===== EXCLUIR CATEGORIA =====");

        int id = lerInteiro("ID da categoria: ");
        if (id == -1) return;

        try {
            Categoria existente = categoriaService.buscarCategoriaPorId(id);
            if (existente == null) {
                System.out.println("[ERRO] Categoria com ID " + id + " não encontrada.");
                return;
            }

            System.out.println("Categoria a ser excluída: " + existente);
            System.out.print("Confirma exclusão? (S/N): ");
            String confirmacao = scanner.nextLine().trim();

            if (confirmacao.equalsIgnoreCase("S")) {
                categoriaService.excluirCategoria(id);
                System.out.println("Categoria excluída com sucesso!");
            } else {
                System.out.println("Exclusão cancelada.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao excluir categoria: " + e.getMessage());
        }
    }

    private int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] Valor inválido. Informe um número inteiro.");
            return -1;
        }
    }
}
