package br.Siblus.DEV.comands;

import br.Siblus.DEV.EcoGen;
import br.Siblus.DEV.EconomyManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlterarValorCommand implements CommandExecutor {

    private EcoGen plugin;

    public AlterarValorCommand(EcoGen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("economy.alterarvalor")) {
            player.sendMessage("Você não tem permissão para usar este comando.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage("Uso incorreto! Use: /alterarvalor <moeda> <novo_valor>");
            return true;
        }

        String moeda = args[0];
        double novoValor;

        try {
            novoValor = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Por favor, insira um valor válido.");
            return true;
        }

        EconomyManager economyManager = plugin.getEconomyManager();

        try {
            economyManager.alterarValorMoeda(moeda, novoValor);
            player.sendMessage("O valor da moeda " + moeda + " foi alterado para " + novoValor + ".");
        } catch (IllegalArgumentException e) {
            player.sendMessage(e.getMessage());
        }

        return true;
    }
}
