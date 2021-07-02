package services.xenlan.hub.queue.custom;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import services.xenlan.hub.bungee.BungeeListener;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;

import java.util.*;

@Getter
public class CustomQueue {

	// TODO: Make some kind of QueuePlayer object this is just aids :facepalm:

	private xHub plugin;
	private String server;
	private List<UUID> players;
	private Map<Player, BukkitTask> playerTaskMap;
	@Setter private boolean paused;

	public CustomQueue(String server) {
		this.plugin = xHub.getInstance();
		players = new ArrayList<>();
		this.playerTaskMap = new HashMap<>();
		this.paused = false;
		this.server = server;
		new BukkitRunnable() {
			@Override
			public void run() {
				players.forEach(player -> {
					if (Bukkit.getPlayer(player).isOnline()) {
						if (plugin.getQueueManager().getQueue(Bukkit.getPlayer(player)).isPaused()) {
							for (String msg : plugin.getMessagesYML().getConfig().getStringList("QUEUE.PAUSED")) {
								String pos = String.valueOf(plugin.getQueueManager().getQueue(Bukkit.getPlayer(player)).getPlayers().indexOf(player) + 1);
								String size = String.valueOf(plugin.getQueueManager().getQueue(Bukkit.getPlayer(player)).getSize());
								Bukkit.getPlayer(player).sendMessage(CC.chat(msg
										.replaceAll("%size%", size)
										.replaceAll("%queue%", server)
										.replaceAll("%pos%", pos)));
							}
						} else {
							String pos = String.valueOf(plugin.getQueueManager().getQueue(Bukkit.getPlayer(player)).getPlayers().indexOf(player) + 1);
							String size = String.valueOf(plugin.getQueueManager().getQueue(Bukkit.getPlayer(player)).getSize());
							for (String msg : plugin.getMessagesYML().getConfig().getStringList("QUEUE.PAUSED")) {
								Bukkit.getPlayer(player).sendMessage(CC.chat(msg
										.replaceAll("%size%", size)
										.replaceAll("%queue%", server)
										.replaceAll("%pos%", pos)));
							}
						}
					} else {
						players.remove(player);

					}
				});
			}
		}.runTaskTimerAsynchronously(xHub.getInstance(), 20 * plugin.getQueueYML().getConfig().getInt("queue-message.delay"), 20 * plugin.getQueueYML().getConfig().getInt("queue-message.delay"));
	}

	public void addToQueue(Player player) {
		if (players.contains(player.getUniqueId())) {
			return;
		}
		if (plugin.getQueueManager().getPriority(player) == 0 && player.hasPermission("queue.bypass")) {
			if (xHub.getInstance().getConfig().getBoolean("BungeeCord")) {
				BungeeListener.sendPlayerToServer(player, this.server);
			} else {
				Bukkit.getConsoleSender().sendMessage(CC.chat("&cWe tried to send " + player.getName() + " to a server using the built in queue, but couldn't because xHub doesn't have BungeeCord enabled in the config.yml."));
			}
			return;
		}
		List<String> message = xHub.getInstance().getMessagesYML().getConfig().getStringList("QUEUE.JOINED");
		for (String msg : message) {
			player.sendMessage(CC.chat(msg.replaceAll("%queue%", this.server)));
		}
		players.add(player.getUniqueId());

		players.forEach(queuePlayer -> {
			int pos = players.indexOf(queuePlayer);
			if (Bukkit.getPlayer(queuePlayer) != player && this.plugin.getQueueManager().getPriority(player) < this.plugin.getQueueManager().getPriority(Bukkit.getPlayer(queuePlayer))) {
				if (Bukkit.getPlayer(players.get(pos)).isOnline()) {
					List<String> msg = xHub.getInstance().getMessagesYML().getConfig().getStringList("QUEUE.HIGHER-PRIORITY");
					for (String mes : msg) {
						Bukkit.getPlayer(players.get(pos)).sendMessage(CC.chat(mes));
					}
				}
				Collections.swap(players, pos, players.size() - 1);
			}
		});
	}

	public void removeFromQueue(Player p) {
		if (!players.contains(p.getUniqueId())) {
			return;
		}
		players.remove(p.getUniqueId());
	}

	public int getSize() {
		return players.size();
	}

	public Player getPlayer(int p) {
		return Bukkit.getPlayer(players.get(p));
	}

	public void update() {
		if (!players.isEmpty()) {
			Player p = Bukkit.getPlayer(players.get(0));
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(this.server);
			p.sendPluginMessage(xHub.getInstance(), "BungeeCord", out.toByteArray());
		}
	}

}
