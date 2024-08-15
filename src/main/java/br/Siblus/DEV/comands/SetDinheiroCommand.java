package br.Siblus.DEV.comands;

import br.Siblus.DEV.EcoGen;
import br.Siblus.DEV.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDinheiroCommand implements CommandExecutor {

    private EcoGen plugin;

    public SetDinheiroCommand(EcoGen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(plugin.getMessage("valor_invalido"));
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        String moeda = args[1];
        double valor;

        try {
            valor = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessage("valor_invalido"));
            return false;
        }

        if (targetPlayer == null) {
            sender.sendMessage(plugin.getMessage("jogador_nao_encontrado"));
            return false;
        }

        if (valor < 0) {
            sender.sendMessage(plugin.getMessage("erro_valor_negativo"));
            return false;
        }

        try {
            plugin.getEconomyManager().setSaldo(targetPlayer.getName(), moeda, valor);
            sender.sendMessage(plugin.getMessage("setdinheiro_sucesso")
                    .replace("%jogador%", targetPlayer.getName())
                    .replace("%moeda%", moeda)
                    .replace("%valor%", String.valueOf(valor)));
        } catch (Exception e) {
            sender.sendMessage(plugin.getMessage("erro_definir_saldo"));
            plugin.getLogger().severe("Erro ao definir saldo: " + e.getMessage());
        }

        return true;
    }
}
