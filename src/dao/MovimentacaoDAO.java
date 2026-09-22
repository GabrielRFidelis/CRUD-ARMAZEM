package dao;

import connection.ConnectionFactory;
import model.Movimentacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Acesso a dados da tabela 'movimentacoes'.
 * Apenas operações SQL — sem regras de negócio.
 */
public class MovimentacaoDAO {

    public void registrar(Movimentacao movimentacao) throws SQLException {
        String sql = "INSERT INTO movimentacoes (produto_id, tipo, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, movimentacao.getProdutoId());
            stmt.setString(2, movimentacao.getTipo());
            stmt.setInt(3, movimentacao.getQuantidade());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movimentacao.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Movimentacao> listarTodas() throws SQLException {
        String sql = """
            SELECT m.id, m.produto_id, p.nome AS produto_nome, m.tipo,
                   m.quantidade, m.data_movimentacao
            FROM movimentacoes m
            INNER JOIN produtos p ON m.produto_id = p.id
            ORDER BY m.data_movimentacao DESC
            """;
        List<Movimentacao> movimentacoes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movimentacoes.add(mapearMovimentacao(rs));
            }
        }
        return movimentacoes;
    }

    public List<Movimentacao> listarPorProduto(int produtoId) throws SQLException {
        String sql = """
            SELECT m.id, m.produto_id, p.nome AS produto_nome, m.tipo,
                   m.quantidade, m.data_movimentacao
            FROM movimentacoes m
            INNER JOIN produtos p ON m.produto_id = p.id
            WHERE m.produto_id = ?
            ORDER BY m.data_movimentacao DESC
            """;
        List<Movimentacao> movimentacoes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movimentacoes.add(mapearMovimentacao(rs));
                }
            }
        }
        return movimentacoes;
    }

    private Movimentacao mapearMovimentacao(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("data_movimentacao");
        return new Movimentacao(
            rs.getInt("id"),
            rs.getInt("produto_id"),
            rs.getString("produto_nome"),
            rs.getString("tipo"),
            rs.getInt("quantidade"),
            timestamp != null ? timestamp.toLocalDateTime() : null
        );
    }
}
