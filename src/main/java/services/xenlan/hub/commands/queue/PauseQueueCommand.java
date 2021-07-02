package services.xenlan.hub.commands.queue;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import services.xenlan.hub.queue.custom.CustomQueue;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;

public class PauseQueueCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("xhub.command.pausequeue")) {
			sender.sendMessage(ChatColor.RED + "No permission.");
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Usage: /pausequeue <server>");
			return true;
		}
		CustomQueue queue = xHub.getInstance().getQueueManager().getQueue(args[0]);
		if (queue == null) {
			sender.sendMessage(CC.chat("&cCould not find the queue '" + args[0] + "."));
			return true;
		}
		sender.sendMessage(CC.chat("&fYou have just toggled " + args[0] + "'s queue status to " +
						(queue.isPaused() ? "&ajoinable" : "&coff")
				));
		queue.setPaused(!queue.isPaused());
		return true;
	}
}
