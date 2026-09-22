package controller;

import model.Movimentacao;
import service.MovimentacaoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Recebe dados do usuário via console e delega ao MovimentacaoService.
 * Nunca acessa o banco diretamente.
 */
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;
    private final Scanner scanner;

    public MovimentacaoController(Scanner scanner) {
        this.movimentacaoService = new MovimentacaoService();
        this.scanner = scanner;
    }

    public void registrarEntrada() {
        System.out.println("\n===== ENTRADA DE ESTOQUE =====");

        int produtoId = lerInteiro("ID do produto: ");
        if (produtoId == -1) return;

        int quantidade = lerInteiro("Quantidade a adicionar: ");
        if (quantidade == -1) return;

        try {
            movimentacaoService.registrarEntrada(produtoId, quantidade);
            System.out.println("Entrada de estoque registrada com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao registrar entrada: " + e.getMessage());
        }
    }

    public void registrarSaida() {
        System.out.println("\n===== SAÍDA DE ESTOQUE =====");

        int produtoId = lerInteiro("ID do produto: ");
        if (produtoId == -1) return;

        int quantidade = lerInteiro("Quantidade a retirar: ");
        if (quantidade == -1) return;

        try {
            movimentacaoService.registrarSaida(produtoId, quantidade);
            System.out.println("Saída de estoque registrada com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao registrar saída: " + e.getMessage());
        }
    }

    public void listarMovimentacoes() {
        System.out.println("\n===== HISTÓRICO DE MOVIMENTAÇÕES =====");

        try {
            List<Movimentacao> movimentacoes = movimentacaoService.listarMovimentacoes();

            if (movimentacoes.isEmpty()) {
                System.out.println("Nenhuma movimentação registrada.");
                return;
            }

            for (Movimentacao mov : movimentacoes) {
                System.out.println(mov);
            }
            System.out.println("Total: " + movimentacoes.size() + " movimentação(ões).");
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao listar movimentações: " + e.getMessage());
        }
    }

    public void listarMovimentacoesPorProduto() {
        System.out.println("\n===== MOVIMENTAÇÕES POR PRODUTO =====");

        int produtoId = lerInteiro("ID do produto: ");
        if (produtoId == -1) return;

        try {
            List<Movimentacao> movimentacoes = movimentacaoService.listarMovimentacoesPorProduto(produtoId);

            if (movimentacoes.isEmpty()) {
                System.out.println("Nenhuma movimentação registrada para o produto ID " + produtoId + ".");
                return;
            }

            for (Movimentacao mov : movimentacoes) {
                System.out.println(mov);
            }
            System.out.println("Total: " + movimentacoes.size() + " movimentação(ões).");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERRO] Falha ao listar movimentações: " + e.getMessage());
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
