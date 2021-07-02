package services.xenlan.hub.commands.queue;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import services.xenlan.hub.xHub;

public class LeaveQueueCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("xhub.command.leavequeue")) {
			sender.sendMessage(ChatColor.RED + "No permission.");
			return true;
		}
		if (!(sender instanceof Player))
			return true;
		Player player = (Player) sender;
		if (args.length > 0) {
			sender.sendMessage(ChatColor.RED + "Usage: /leavequeue");
			return true;
		}
		if (xHub.getInstance().getQueueManager().getQueue(player) == null) {
			return true;
		}
		xHub.getInstance().getQueueManager().getQueue(player).removeFromQueue(player);
		return false;
	}
}
