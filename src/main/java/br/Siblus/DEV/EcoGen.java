package br.Siblus.DEV;

import br.Siblus.DEV.comands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class EcoGen extends JavaPlugin {

    private DatabaseManager dbManager;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Salva o config.yml padrão
        dbManager = new DatabaseManager(this);
        economyManager = new EconomyManager(dbManager);

        // Registrar comandos
        getCommand("criarmoeda").setExecutor(new CriarMoedaCommand(this));
        getCommand("saldo").setExecutor(new SaldoCommand(this));
        getCommand("setdinheiro").setExecutor(new SetDinheiroCommand(this));
        getCommand("transferir").setExecutor(new TransferirCommand(this));
        getCommand("alterarvalor").setExecutor(new AlterarValorCommand(this));

        getLogger().info("EconomyPlugin habilitado!");
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public String getMessage(String key) {
        return getConfig().getString("messages." + key, "Mensagem não encontrada para a chave: " + key);
    }
}
