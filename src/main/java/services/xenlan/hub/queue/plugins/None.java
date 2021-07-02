package services.xenlan.hub.queue.plugins;

import org.bukkit.entity.Player;
import services.xenlan.hub.bungee.BungeeListener;
import services.xenlan.hub.queue.Queue;
import services.xenlan.hub.xHub;

public class None extends Queue {
	@Override
	public boolean inQueue(Player p) {
		return false;
	}

	@Override
	public String getQueueIn(Player p) {
		return "";
	}

	@Override
	public int getPosition(Player p) {
		return 0;
	}

	@Override
	public int getQueueSize(String server) {
		return 0;
	}

	@Override
	public void sendPlayer(Player p, String server) {
		if (xHub.getInstance().getConfig().getBoolean("BungeeCord")) {
			BungeeListener.sendPlayerToServer(p, server);
		}
	}
}
