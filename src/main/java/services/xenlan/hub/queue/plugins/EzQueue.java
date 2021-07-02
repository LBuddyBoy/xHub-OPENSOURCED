package services.xenlan.hub.queue.plugins;

import me.signatured.ezqueueshared.QueueInfo;
import me.signatured.ezqueuespigot.EzQueueAPI;
import org.bukkit.entity.Player;
import services.xenlan.hub.queue.Queue;

public class EzQueue extends Queue {
	@Override
	public boolean inQueue(Player p) {
		QueueInfo info = QueueInfo.getQueueInfo(EzQueueAPI.getQueue(p));
		return info != null;
	}

	@Override
	public String getQueueIn(Player p) {
		return EzQueueAPI.getQueue(p);
	}

	@Override
	public int getPosition(Player p) {
		return EzQueueAPI.getPosition(p);
	}

	@Override
	public int getQueueSize(String server) {
		return QueueInfo.getQueueInfo(server).getPlayersInQueue().size();
	}

	@Override
	public void sendPlayer(Player p, String server) {
		EzQueueAPI.addToQueue(p, server);
	}
}
