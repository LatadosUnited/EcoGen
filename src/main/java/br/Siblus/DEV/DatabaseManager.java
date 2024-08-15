package br.Siblus.DEV;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private EcoGen plugin;
    private Connection connection;

    public DatabaseManager(EcoGen plugin) {
        this.plugin = plugin;
        connect();
        createTables(); // Cria as tabelas após a conexão
    }

    private void connect() {
        String dbType = plugin.getConfig().getString("database.type");

        if (dbType.equalsIgnoreCase("mysql")) {
            String host = plugin.getConfig().getString("database.mysql.host");
            String port = plugin.getConfig().getString("database.mysql.port");
            String database = plugin.getConfig().getString("database.mysql.name");
            String user = plugin.getConfig().getString("database.mysql.user");
            String password = plugin.getConfig().getString("database.mysql.password");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

            try {
                connection = DriverManager.getConnection(url, user, password);
                plugin.getLogger().info("Conectado ao banco de dados MySQL.");
            } catch (SQLException e) {
                plugin.getLogger().severe("Erro ao conectar ao banco de dados MySQL: " + e.getMessage());
            }
        } else if (dbType.equalsIgnoreCase("sqlite")) {
            String database = plugin.getConfig().getString("database.sqlite.name");
            String url = "jdbc:sqlite:" + plugin.getDataFolder() + "/" + database + ".db";

            try {
                connection = DriverManager.getConnection(url);
                plugin.getLogger().info("Conectado ao banco de dados SQLite.");
            } catch (SQLException e) {
                plugin.getLogger().severe("Erro ao conectar ao banco de dados SQLite: " + e.getMessage());
            }
        }
    }

    private void createTables() {
        String sqlCreateMoedasTable;
        String sqlCreateSaldosTable;

        if (isSQLite()) {
            // Sintaxe para SQLite
            sqlCreateMoedasTable = "CREATE TABLE IF NOT EXISTS moedas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT NOT NULL, " +
                    "valor REAL NOT NULL);"; // REAL é usado em SQLite para valores de ponto flutuante

            sqlCreateSaldosTable = "CREATE TABLE IF NOT EXISTS saldos (" +
                    "jogador TEXT NOT NULL, " +
                    "moeda_id INTEGER NOT NULL, " +
                    "saldo REAL NOT NULL, " +
                    "PRIMARY KEY(jogador, moeda_id), " +
                    "FOREIGN KEY(moeda_id) REFERENCES moedas(id));";
        } else {
            // Sintaxe para MySQL
            sqlCreateMoedasTable = "CREATE TABLE IF NOT EXISTS moedas (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(255) NOT NULL, " +
                    "valor DOUBLE NOT NULL);";

            sqlCreateSaldosTable = "CREATE TABLE IF NOT EXISTS saldos (" +
                    "jogador VARCHAR(255) NOT NULL, " +
                    "moeda_id INT NOT NULL, " +
                    "saldo DOUBLE NOT NULL, " +
                    "PRIMARY KEY(jogador, moeda_id), " +
                    "FOREIGN KEY(moeda_id) REFERENCES moedas(id));";
        }

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlCreateMoedasTable);
            stmt.execute(sqlCreateSaldosTable);
            plugin.getLogger().info("Tabelas criadas com sucesso.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    private boolean isSQLite() {
        String dbType = plugin.getConfig().getString("database.type");
        return dbType.equalsIgnoreCase("sqlite");
    }


    public Connection getConnection() {
        try {
            ensureConnectionOpen();
        } catch (SQLException e) {
            plugin.getLogger().severe("Erro ao garantir a conexão: " + e.getMessage());
        }
        return connection;
    }

    private void ensureConnectionOpen() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect(); // Tenta reconectar se a conexão estiver fechada
        }
    }
}
