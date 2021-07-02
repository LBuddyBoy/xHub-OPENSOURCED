package services.xenlan.hub.queue.plugins;

import org.bukkit.entity.Player;
import services.xenlan.hub.queue.Queue;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;

public class Custom extends Queue {
	@Override
	public boolean inQueue(Player p) {
		return xHub.getInstance().getQueueManager().getQueue(p) != null;
	}

	@Override
	public String getQueueIn(Player p) {
		return xHub.getInstance().getQueueManager().getQueue(p).getServer();
	}

	@Override
	public int getPosition(Player p) {
		int pos = xHub.getInstance().getQueueManager().getQueue(p).getPlayers().indexOf(p) + 1;
		return pos;
	}

	@Override
	public int getQueueSize(String server) {
		return xHub.getInstance().getQueueManager().getQueue(server).getSize();
	}

	@Override
	public void sendPlayer(Player p, String server) {
		if (xHub.getInstance().getQueueManager().getQueue(server) == null) {
			p.sendMessage(CC.chat("&cFailed to add you to the " + server + "'s queue."));
			return;
		}
		xHub.getInstance().getQueueManager().getQueue(server).addToQueue(p);
	}
}
