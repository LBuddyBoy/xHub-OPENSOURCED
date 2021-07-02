package services.xenlan.hub.parkour.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import services.xenlan.hub.xHub;

public class Listener implements org.bukkit.event.Listener {

	@EventHandler
	public void onCrack(PlayerInteractEvent event) {
		if (xHub.getInstance().getConfig().getString("License").equalsIgnoreCase("")) {
			Bukkit.broadcastMessage("I think this is cracked?");
		}
	}
	@EventHandler
	public void onCrack(PlayerMoveEvent event) {
		if (xHub.getInstance().getConfig().getString("License").equalsIgnoreCase("")) {
			Bukkit.broadcastMessage("I think this is cracked?");
		}
	}
}
