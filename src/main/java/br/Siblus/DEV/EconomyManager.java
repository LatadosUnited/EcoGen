package br.Siblus.DEV;

import java.sql.Connection;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private DatabaseManager dbManager;
    private static final Logger logger = Logger.getLogger("Minecraft");

    public EconomyManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    //codigo de criarMoeda
    public void criarMoeda(String nome, double valorInicial) {
        try (Connection conn = dbManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO moedas (nome, valor) VALUES (?, ?)");
            stmt.setString(1, nome);
            stmt.setDouble(2, valorInicial);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //codigo saldo

    public double getSaldo(String player, String moeda) {
        double saldo = 0.0;
        try (Connection conn = dbManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT saldo FROM saldos WHERE jogador=? AND moeda_id=(SELECT id FROM moedas WHERE nome=?)");
            stmt.setString(1, player);
            stmt.setString(2, moeda);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                saldo = rs.getDouble("saldo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saldo;
    }

    public Map<String, Double> getSaldo(String player) {
        Map<String, Double> saldos = new HashMap<>();
        try (Connection conn = dbManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT m.nome, s.saldo FROM saldos s JOIN moedas m ON s.moeda_id=m.id WHERE s.jogador=?");
            stmt.setString(1, player);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                saldos.put(rs.getString("nome"), rs.getDouble("saldo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saldos;
    }

    public void setSaldo(String jogador, String moeda, double valor) {
        try (Connection conn = dbManager.getConnection()) {
            int moedaId = getMoedaId(conn, moeda);
            if (moedaId == -1) {
                throw new IllegalArgumentException("Moeda não encontrada: " + moeda);
            }

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT OR REPLACE INTO saldos (jogador, moeda_id, saldo) VALUES (?, ?, ?)"
            );
            stmt.setString(1, jogador);
            stmt.setInt(2, moedaId);
            stmt.setDouble(3, valor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Erro ao definir saldo: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Erro inesperado: " + e.getMessage());
        }
    }

    private int getMoedaId(Connection conn, String moeda) throws Exception {
        PreparedStatement stmt = conn.prepareStatement("SELECT id FROM moedas WHERE nome=?");
        stmt.setString(1, moeda);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            return -1; // Moeda não encontrada
        }
    }

    public void alterarValorMoeda(String nome, double novoValor) {
        try (Connection conn = dbManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE moedas SET valor=? WHERE nome=?");
            stmt.setDouble(1, novoValor);
            stmt.setString(2, nome);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Moeda não encontrada: " + nome);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Implementação de outros métodos de gestão econômica (saldo, transferências, etc.)
}
