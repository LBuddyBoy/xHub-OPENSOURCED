package services.xenlan.hub.queue;

import org.bukkit.entity.Player;

public abstract class Queue
{
	public abstract boolean inQueue(Player player);

	public abstract String getQueueIn(Player player);

	public abstract int getPosition(Player player);

	public abstract int getQueueSize(String player);

	public abstract void sendPlayer(Player player, String server);
}
