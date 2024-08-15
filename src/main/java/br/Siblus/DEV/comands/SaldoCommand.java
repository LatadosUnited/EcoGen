package br.Siblus.DEV.comands;

import br.Siblus.DEV.EcoGen;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class SaldoCommand implements CommandExecutor {

    private EcoGen plugin;

    public SaldoCommand(EcoGen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando só pode ser executado por um jogador.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Mostrar saldo em todas as moedas
            Map<String, Double> saldos = plugin.getEconomyManager().getSaldo(player.getName());
            StringBuilder saldoMensagem = new StringBuilder(plugin.getMessage("saldo_todos"));
            for (Map.Entry<String, Double> entry : saldos.entrySet()) {
                saldoMensagem.append("\n").append(entry.getKey()).append(": ").append(entry.getValue());
            }
            player.sendMessage(saldoMensagem.toString());
        } else if (args.length == 1) {
            // Mostrar saldo em uma moeda específica
            String moeda = args[0];
            double saldo = plugin.getEconomyManager().getSaldo(player.getName(), moeda);
            player.sendMessage(plugin.getMessage("saldo_mostrado")
                    .replace("%moeda%", moeda)
                    .replace("%saldo%", String.valueOf(saldo)));
        } else {
            player.sendMessage("Uso: /saldo [moeda]");
        }

        return true;
    }
}
