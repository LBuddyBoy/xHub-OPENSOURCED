package services.xenlan.hub.commands;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (xHub.getInstance().getConfig().getString("License").equalsIgnoreCase("")) {
            Bukkit.broadcastMessage("I think this is cracked?");
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length == 0) {
            Player player = (Player) sender;
            for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Stats-Message")) {
                sender.sendMessage(CC.chat(list).replaceAll("%player%", player.getName())
                        .replaceAll("%kills%", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)))
                        .replaceAll("%deaths%", String.valueOf(player.getStatistic(Statistic.DEATHS))));
            }
            return true;
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);

        if (args.length == 1) {
            if (target == null) {
                sender.sendMessage(CC.chat("&cInvalid Player."));
                return true;
            }
            for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Stats-Message")) {
                sender.sendMessage(CC.chat(list).replaceAll("%player%", target.getName())
                        .replaceAll("%kills%", String.valueOf(target.getStatistic(Statistic.PLAYER_KILLS)))
                        .replaceAll("%deaths%", String.valueOf(target.getStatistic(Statistic.DEATHS))));
            }
            return true;
        }
        return false;
    }
}
