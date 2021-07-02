package services.xenlan.hub.queue.manager;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import services.xenlan.hub.queue.custom.CustomQueue;
import services.xenlan.hub.xHub;

import java.util.ArrayList;
import java.util.List;

public class QueueManager implements Listener {
	@Getter private List<CustomQueue> queues;

	public QueueManager(xHub plugin) {
		this.queues = new ArrayList<>();
	}

	public CustomQueue getQueue(Player player) {
		return this.queues.stream().filter(queue -> queue.getPlayers().contains(player.getUniqueId())).findFirst().orElse(null);
	}

	public CustomQueue getQueue(String serverName) {
		return this.queues.stream().filter(queue -> queue.getServer().equalsIgnoreCase(serverName)).findFirst().orElse(null);
	}

	public int getPriority(Player player) {
		if (player.hasPermission("queue.bypass")) {
			return 0;
		}
		if (player.hasPermission("queue.priority.1")) {
			return 1;
		}
		if (player.hasPermission("queue.priority.2")) {
			return 2;
		}
		if (player.hasPermission("queue.priority.3")) {
			return 3;
		}
		if (player.hasPermission("queue.priority.4")) {
			return 4;
		}
		if (player.hasPermission("queue.priority.5")) {
			return 5;
		}
		if (player.hasPermission("queue.priority.6")) {
			return 6;
		}
		if (player.hasPermission("queue.priority.7")) {
			return 7;
		}
		if (player.hasPermission("queue.priority.8")) {
			return 8;
		}
		if (player.hasPermission("queue.priority.9")) {
			return 9;
		}
		if (player.hasPermission("queue.priority.10")) {
			return 10;
		}
		if (player.hasPermission("queue.priority.11")) {
			return 11;
		}
		if (player.hasPermission("queue.priority.12")) {
			return 12;
		}
		if (player.hasPermission("queue.priority.13")) {
			return 13;
		}
		if (player.hasPermission("queue.priority.14")) {
			return 14;
		}

		return 15;
	}

	@EventHandler
	public void handleQueueQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		this.queues.forEach(queue -> {
			if (queue.getPlayers().contains(p.getUniqueId())) {
				queue.removeFromQueue(p);
			}
			if (queue.getPlayerTaskMap().containsKey(p)) {
				queue.getPlayerTaskMap().get(p).cancel();
				queue.getPlayerTaskMap().remove(p);
			}
		});
	}

}
