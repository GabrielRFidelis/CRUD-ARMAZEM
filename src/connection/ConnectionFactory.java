package connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Centraliza a criação e o fechamento de conexões com o PostgreSQL.
 * Nenhuma outra classe do projeto deve usar DriverManager diretamente.
 * Suporta configuração via arquivo db.properties e variáveis de ambiente.
 */
public class ConnectionFactory {

    private static final Properties props = new Properties();

    static {
        carregarConfiguracoes();
    }

    private static void carregarConfiguracoes() {
        // 1. Tenta carregar do arquivo local db.properties
        File configFile = new File("db.properties");
        if (configFile.exists()) {
            try (InputStream is = new FileInputStream(configFile)) {
                props.load(is);
            } catch (IOException e) {
                System.err.println("Aviso: Falha ao ler db.properties local: " + e.getMessage());
            }
        } else {
            // 2. Tenta carregar do classpath
            try (InputStream is = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (is != null) {
                    props.load(is);
                }
            } catch (IOException e) {
                System.err.println("Aviso: Falha ao ler db.properties do classpath: " + e.getMessage());
            }
        }
    }

    private static String getParam(String envKey, String propKey, String defaultValue) {
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }
        String propVal = props.getProperty(propKey);
        if (propVal != null && !propVal.trim().isEmpty()) {
            return propVal.trim();
        }
        return defaultValue;
    }

    /**
     * Abre e retorna uma nova conexão com o banco de dados.
     */
    public static Connection getConnection() {
        String host = getParam("DB_HOST", "db.host", "localhost");
        String port = getParam("DB_PORT", "db.port", "5432");
        String dbName = getParam("DB_NAME", "db.name", "estoque_db");
        String usuario = getParam("DB_USER", "db.user", "postgres");
        String senha = getParam("DB_PASSWORD", "db.password", "");

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;

        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados (" + url + "): " + e.getMessage(), e);
        }
    }

    /**
     * Fecha Connection, PreparedStatement e ResultSet de forma segura.
     */
    public static void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(stmt);
        closeConnection(conn);
    }

    /**
     * Fecha Connection e PreparedStatement (sem ResultSet).
     */
    public static void closeConnection(Connection conn, PreparedStatement stmt) {
        closeStatement(stmt);
        closeConnection(conn);
    }

    /**
     * Fecha apenas a Connection.
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    private static void closeStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar statement: " + e.getMessage());
            }
        }
    }

    private static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar result set: " + e.getMessage());
            }
        }
    }
}
