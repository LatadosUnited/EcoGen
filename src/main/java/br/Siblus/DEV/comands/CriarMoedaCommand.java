package br.Siblus.DEV.comands;

import br.Siblus.DEV.EcoGen;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CriarMoedaCommand implements CommandExecutor {
    private EcoGen plugin;

    public CriarMoedaCommand(EcoGen plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2) {
                String nomeMoeda = args[0];
                double valorInicial;
                try {
                    valorInicial = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("Valor inicial inválido!");
                    return false;
                }

                try {
                    plugin.getEconomyManager().criarMoeda(nomeMoeda, valorInicial);
                    String mensagem = plugin.getMessage("moeda_criada");

                    if (mensagem != null) {
                        player.sendMessage(mensagem
                                .replace("%moeda%", nomeMoeda)
                                .replace("%valor%", String.valueOf(valorInicial)));
                    } else {
                        player.sendMessage("Moeda criada com sucesso, mas a mensagem de confirmação não foi encontrada.");
                    }

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();  // Loga o stack trace completo
                    player.sendMessage("Ocorreu um erro ao tentar criar a moeda. Verifique os logs.");
                    return false;
                }
            } else {
                player.sendMessage("Uso: /criarmoeda <nome> <valor_inicial>");
            }
        }
        return false;
    }
}
