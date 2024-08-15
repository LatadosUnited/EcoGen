package br.Siblus.DEV.comands;

import br.Siblus.DEV.EcoGen;
import br.Siblus.DEV.EconomyManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TransferirCommand implements CommandExecutor {

    private EcoGen plugin;

    public TransferirCommand(EcoGen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando só pode ser usado por jogadores.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 3) {
            player.sendMessage("Uso incorreto! Use: /transferir <jogador> <moeda> <quantia>");
            return true;
        }

        String targetPlayer = args[0];
        String moeda = args[1];
        double quantia;

        try {
            quantia = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("Por favor, insira uma quantia válida.");
            return true;
        }

        if (quantia <= 0) {
            player.sendMessage("A quantia deve ser maior que zero.");
            return true;
        }

        EconomyManager economyManager = plugin.getEconomyManager();

        // Obtém o saldo do jogador
        double saldoAtual = economyManager.getSaldo(player.getName(), moeda);

        if (saldoAtual < quantia) {
            player.sendMessage("Você não tem saldo suficiente para realizar esta transferência.");
            return true;
        }

        // Realiza a transferência
        economyManager.setSaldo(player.getName(), moeda, saldoAtual - quantia);
        double saldoDestino = economyManager.getSaldo(targetPlayer, moeda);
        economyManager.setSaldo(targetPlayer, moeda, saldoDestino + quantia);

        player.sendMessage("Transferência de " + quantia + " " + moeda + " para " + targetPlayer + " realizada com sucesso.");
        return true;
    }
}
