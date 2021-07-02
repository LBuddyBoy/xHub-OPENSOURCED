package services.xenlan.hub.parkour.command;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;

public class ParkourCommand implements CommandExecutor {
	@SneakyThrows
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (xHub.getInstance().getConfig().getString("License").equalsIgnoreCase("")) {
			Bukkit.broadcastMessage("I think this is cracked?");
			return true;
		}
		if (!sender.hasPermission("xhub.command.parkour.admin")) {
			sender.sendMessage(CC.chat(xHub.getInstance().getMessagesYML().getConfig().getString("No-Perm")));
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(CC.chat("&cPlayers only"));
			return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("setspawn")) {
				Player player = (Player) sender;
				sender.sendMessage(CC.chat("&aSet the parkour spawn!"));
				xHub.getInstance().getSettingsYML().getConfig().set("Parkour.Start-Location.X", player.getLocation().getX());
				xHub.getInstance().getSettingsYML().getConfig().set("Parkour.Start-Location.Y", player.getLocation().getY());
				xHub.getInstance().getSettingsYML().getConfig().set("Parkour.Start-Location.Z", player.getLocation().getZ());
				xHub.getInstance().getSettingsYML().getConfig().set("Parkour.Start-Location.Yaw", player.getLocation().getYaw());
				xHub.getInstance().getSettingsYML().getConfig().set("Parkour.Start-Location.Pitch", player.getLocation().getPitch());
				xHub.getInstance().getSettingsYML().save();
				return true;
			} else {
				for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Parkour-Help")) {
					sender.sendMessage(CC.chat(list));
				}
				return true;
			}
		} else {
			for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Parkour-Help")) {
				sender.sendMessage(CC.chat(list));
			}
		}
		return false;
	}
}
