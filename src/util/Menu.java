package util;

import java.util.Scanner;

import controller.CategoriaController;
import controller.MovimentacaoController;
import controller.ProdutoController;

/**
 * Menu interativo do sistema via console.
 * Orquestra a navegação entre os submenus de Produtos, Categorias e
 * Movimentações.
 */
public class Menu {

    private final Scanner scanner;
    private final ProdutoController produtoController;
    private final CategoriaController categoriaController;
    private final MovimentacaoController movimentacaoController;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.produtoController = new ProdutoController(scanner);
        this.categoriaController = new CategoriaController(scanner);
        this.movimentacaoController = new MovimentacaoController(scanner);
    }

    public void exibirMenuPrincipal() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println();
            System.out.println("===========================================");
            System.out.println("         SISTEMA DE CONTROLE DE ESTOQUE    ");
            System.out.println("===========================================");
            System.out.println("  1 - Produtos");
            System.out.println("  2 - Categorias");
            System.out.println("  3 - Movimentações");
            System.out.println("  0 - Sair");
            System.out.println("===========================================");

            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> exibirMenuProdutos();
                case 2 -> exibirMenuCategorias();
                case 3 -> exibirMenuMovimentacoes();
                case 0 -> System.out.println("\nEncerrando o sistema. Até logo!");
                default -> System.out.println("[ERRO] Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    // ---------- Submenu de Produtos ----------

    private void exibirMenuProdutos() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println();
            System.out.println("===========================================");
            System.out.println("              MENU - PRODUTOS              ");
            System.out.println("===========================================");
            System.out.println("  1 - Cadastrar produto");
            System.out.println("  2 - Listar produtos");
            System.out.println("  3 - Buscar por ID");
            System.out.println("  4 - Buscar por nome");
            System.out.println("  5 - Atualizar produto");
            System.out.println("  6 - Excluir produto");
            System.out.println("  0 - Voltar");
            System.out.println("===========================================");

            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> produtoController.cadastrarProduto();
                case 2 -> produtoController.listarProdutos();
                case 3 -> produtoController.buscarProdutoPorId();
                case 4 -> produtoController.buscarProdutoPorNome();
                case 5 -> produtoController.atualizarProduto();
                case 6 -> produtoController.excluirProduto();
                case 0 -> {
                    /* Volta ao menu principal */ }
                default -> System.out.println("[ERRO] Opção inválida. Tente novamente.");
            }
        }
    }

    // ---------- Submenu de Categorias ----------

    private void exibirMenuCategorias() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println();
            System.out.println("===========================================");
            System.out.println("            MENU - CATEGORIAS              ");
            System.out.println("===========================================");
            System.out.println("  1 - Cadastrar categoria");
            System.out.println("  2 - Listar categorias");
            System.out.println("  3 - Atualizar categoria");
            System.out.println("  4 - Excluir categoria");
            System.out.println("  0 - Voltar");
            System.out.println("===========================================");

            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> categoriaController.cadastrarCategoria();
                case 2 -> categoriaController.listarCategorias();
                case 3 -> categoriaController.atualizarCategoria();
                case 4 -> categoriaController.excluirCategoria();
                case 0 -> {
                    /* Volta ao menu principal */ }
                default -> System.out.println("[ERRO] Opção inválida. Tente novamente.");
            }
        }
    }

    // ---------- Submenu de Movimentações ----------

    private void exibirMenuMovimentacoes() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println();
            System.out.println("===========================================");
            System.out.println("          MENU - MOVIMENTAÇÕES             ");
            System.out.println("===========================================");
            System.out.println("  1 - Registrar entrada");
            System.out.println("  2 - Registrar saída");
            System.out.println("  3 - Listar todas as movimentações");
            System.out.println("  4 - Listar movimentações por produto");
            System.out.println("  0 - Voltar");
            System.out.println("===========================================");

            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> movimentacaoController.registrarEntrada();
                case 2 -> movimentacaoController.registrarSaida();
                case 3 -> movimentacaoController.listarMovimentacoes();
                case 4 -> movimentacaoController.listarMovimentacoesPorProduto();
                case 0 -> {
                    /* Volta ao menu principal */ }
                default -> System.out.println("[ERRO] Opção inválida. Tente novamente.");
            }
        }
    }

    // ---------- Leitura segura de opção ----------

    private int lerOpcao() {
        System.out.print("Escolha uma opção: ");
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
