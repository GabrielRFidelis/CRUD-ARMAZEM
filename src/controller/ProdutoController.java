package controller;

import model.Produto;
import service.ProdutoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Recebe dados do usuário via console e delega ao ProdutoService.
 * Nunca acessa o banco diretamente.
 */
public class ProdutoController {

    private final ProdutoService produtoService;
    private final Scanner scanner;

    public ProdutoController(Scanner scanner) {
        this.produtoService = new ProdutoService();
        this.scanner = scanner;
    }

    public void cadastrarProduto() {
        System.out.println("\n===== CADASTRAR PRODUTO =====");

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Descrição (opcional): ");
        String descricao = scanner.nextLine().trim();
        if (descricao.isEmpty()) {
            descricao = null;
        }

        double preco = lerDouble("Preço: ");
        if (preco == -1) return;

        int quantidade = lerInteiro("Quantidade inicial: ");
        if (quantidade == -1) return;

        int categoriaId = lerInteiro("ID da categoria: ");
        if (categoriaId == -1) return;

        try {
            Produto produto = new Produto(nome, descricao, preco, quantidade, categoriaId);
            produtoService.cadastrarProduto(produto);
            System.out.println("Produto cadastrado com sucesso! ID: " + produto.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao cadastrar produto: " + e.getMessage());
        }
    }

    public void listarProdutos() {
        System.out.println("\n===== LISTA DE PRODUTOS =====");

        try {
            List<Produto> produtos = produtoService.listarProdutos();

            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
                return;
            }

            for (Produto produto : produtos) {
                System.out.println(produto);
            }
            System.out.println("Total: " + produtos.size() + " produto(s).");
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao listar produtos: " + e.getMessage());
        }
    }

    public void buscarProdutoPorId() {
        System.out.println("\n===== BUSCAR PRODUTO POR ID =====");

        int id = lerInteiro("ID do produto: ");
        if (id == -1) return;

        try {
            Produto produto = produtoService.buscarProdutoPorId(id);
            if (produto == null) {
                System.out.println("Produto com ID " + id + " não encontrado.");
            } else {
                System.out.println(produto);
            }
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao buscar produto: " + e.getMessage());
        }
    }

    public void buscarProdutoPorNome() {
        System.out.println("\n===== BUSCAR PRODUTO POR NOME =====");

        System.out.print("Nome (ou parte do nome): ");
        String nome = scanner.nextLine().trim();

        try {
            List<Produto> produtos = produtoService.buscarProdutosPorNome(nome);

            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto encontrado com o termo '" + nome + "'.");
            } else {
                for (Produto produto : produtos) {
                    System.out.println(produto);
                }
                System.out.println("Encontrado(s): " + produtos.size() + " produto(s).");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao buscar produtos: " + e.getMessage());
        }
    }

    public void atualizarProduto() {
        System.out.println("\n===== ATUALIZAR PRODUTO =====");

        int id = lerInteiro("ID do produto: ");
        if (id == -1) return;

        try {
            Produto existente = produtoService.buscarProdutoPorId(id);
            if (existente == null) {
                System.out.println("[ERRO] Produto com ID " + id + " não encontrado.");
                return;
            }

            System.out.println("Produto atual: " + existente);
            System.out.println("(Pressione ENTER para manter o valor atual)");

            System.out.print("Novo nome [" + existente.getNome() + "]: ");
            String novoNome = scanner.nextLine().trim();
            if (novoNome.isEmpty()) {
                novoNome = existente.getNome();
            }

            System.out.print("Nova descrição [" + (existente.getDescricao() != null ? existente.getDescricao() : "-") + "]: ");
            String novaDescricao = scanner.nextLine().trim();
            if (novaDescricao.isEmpty()) {
                novaDescricao = existente.getDescricao();
            }

            System.out.print("Novo preço [" + String.format("%.2f", existente.getPreco()) + "]: ");
            String precoStr = scanner.nextLine().trim();
            double novoPreco = existente.getPreco();
            if (!precoStr.isEmpty()) {
                try {
                    novoPreco = Double.parseDouble(precoStr.replace(",", "."));
                } catch (NumberFormatException e) {
                    System.out.println("[ERRO] Preço inválido. Mantendo o valor atual.");
                }
            }

            System.out.print("Novo ID da categoria [" + existente.getCategoriaId() + "]: ");
            String catStr = scanner.nextLine().trim();
            int novaCategoriaId = existente.getCategoriaId();
            if (!catStr.isEmpty()) {
                try {
                    novaCategoriaId = Integer.parseInt(catStr);
                } catch (NumberFormatException e) {
                    System.out.println("[ERRO] ID inválido. Mantendo a categoria atual.");
                }
            }

            Produto produto = new Produto(novoNome, novaDescricao, novoPreco, existente.getQuantidade(), novaCategoriaId);
            produto.setId(id);

            produtoService.atualizarProduto(produto);
            System.out.println("Produto atualizado com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao atualizar produto: " + e.getMessage());
        }
    }

    public void excluirProduto() {
        System.out.println("\n===== EXCLUIR PRODUTO =====");

        int id = lerInteiro("ID do produto: ");
        if (id == -1) return;

        try {
            Produto existente = produtoService.buscarProdutoPorId(id);
            if (existente == null) {
                System.out.println("[ERRO] Produto com ID " + id + " não encontrado.");
                return;
            }

            System.out.println("Produto a ser excluído: " + existente);
            System.out.print("Confirma exclusão? (S/N): ");
            String confirmacao = scanner.nextLine().trim();

            if (confirmacao.equalsIgnoreCase("S")) {
                produtoService.excluirProduto(id);
                System.out.println("Produto excluído com sucesso!");
            } else {
                System.out.println("Exclusão cancelada.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao excluir produto: " + e.getMessage());
        }
    }

    // ---------- Utilitários de leitura ----------

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

    private double lerDouble(String mensagem) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine().trim().replace(",", ".");
        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] Valor inválido. Informe um número decimal.");
            return -1;
        }
    }
}
