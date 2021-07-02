package services.xenlan.hub.action;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import services.xenlan.hub.gui.menu.CustomGUI;
import services.xenlan.hub.pvp.gui.PvPGUI;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.ItemBuilder;
import services.xenlan.hub.xHub;

import java.util.List;

public class ActionManager {


	public ActionManager() {
	}

	public void performAction(String action, Player player) {
		for (CustomGUI ui : xHub.getInstance().getCustomGUI()) {
			if (action.equalsIgnoreCase("[GUI-" + ui.getInv() + "]")) {
				player.closeInventory();
				ui.openMenu(player);
			}
		}
		if (action.equalsIgnoreCase("[ENDERBUTT]")) {
			activateEnderbutt(player);
		} else if (action.equalsIgnoreCase("[LAST-CHECKPOINT]")) {
			lastCheck(player);
		} else if (action.equalsIgnoreCase("[LEAVE-PARKOUR]")) {
			leaveParkour(player);
		} else if (action.equalsIgnoreCase("[ACTIVATE-PARKOUR]")) {
			startParkour(player);
		} else if (action.equalsIgnoreCase("[GUI-PVP]")) {
			player.closeInventory();
			PvPGUI.open(player);
		} else {
			System.out.println(player.getName() + " tried to execute " + action + ", but we could not find a function for it!");
		}
	}

	public void lastCheck(Player player) {
		if (xHub.getInstance().getParkourManager().checkPointLoc.containsKey(player)) {
			player.teleport(xHub.getInstance().getParkourManager().checkPointLoc.get(player));
			return;
		}
		double x = xHub.getInstance().getSettingsYML().getConfig().getDouble("Parkour.Start-Location.X");
		double y = xHub.getInstance().getSettingsYML().getConfig().getDouble("Parkour.Start-Location.Y");
		double z = xHub.getInstance().getSettingsYML().getConfig().getDouble("Parkour.Start-Location.Z");
		float pitch = xHub.getInstance().getSettingsYML().getConfig().getInt("Parkour.Start-Location.Pitch");
		float yaw = xHub.getInstance().getSettingsYML().getConfig().getInt("Parkour.Start-Location.Yaw");
		Location location = new Location(player.getWorld(), x, y, z);
		location.setPitch(pitch);
		location.setYaw(yaw);
		player.teleport(location);
	}
	
	public void leaveParkour(Player player) {
		xHub.getInstance().getParkourManager().deactivateParkour(player);
	}

	public void startParkour(Player player) {
		double x = xHub.getInstance().getSettingsYML().getConfig().getDouble("Parkour.Start-Location.X");
		double y = xHub.getInstance().getSettingsYML().getConfig().getDouble("Parkour.Start-Location.Y");
		double z = xHub.getInstance().getSettingsYML().getConfig().getDouble("Parkour.Start-Location.Z");
		float pitch = xHub.getInstance().getSettingsYML().getConfig().getInt("Parkour.Start-Location.Pitch");
		float yaw = xHub.getInstance().getSettingsYML().getConfig().getInt("Parkour.Start-Location.Yaw");
		Location location = new Location(player.getWorld(), x, y, z);
		location.setPitch(pitch);
		location.setYaw(yaw);
		xHub.getInstance().getParkourManager().activateParkour(player, location);
	}

	public void togglePlayers(Player player) {
		if (player.hasMetadata("vanished")) {
			for (Player online : Bukkit.getOnlinePlayers()) {
				player.showPlayer(online);
			}
			player.removeMetadata("vanished", xHub.getInstance());
			player.updateInventory();
			for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Players-On")) {
				player.sendMessage(CC.chat(list));
			}
		} else {
			for (Player online : Bukkit.getOnlinePlayers()) {
				player.hidePlayer(online);
			}
			player.setMetadata("vanished", new FixedMetadataValue(xHub.getInstance(), player));
			player.updateInventory();
			for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Players-Off")) {
				player.sendMessage(CC.chat(list));
			}
		}
	}

	public void activateEnderbutt(Player player) {
		if (player.isInsideVehicle()) {
			player.getVehicle().setPassenger(null);
			player.getVehicle().remove();
		}
		double boost = xHub.getInstance().getSettingsYML().getConfig().getDouble("EnderButt.Boost");
		player.setVelocity(player.getLocation().getDirection().normalize().multiply(boost));
		player.playSound(player.getLocation(), Sound.valueOf(xHub.getInstance().getSettingsYML().getConfig().getString("EnderButt.Sound")), 2.0f, 1.0f);
		boolean enabled = xHub.getInstance().getSettingsYML().getConfig().getBoolean("EnderButt.Ride-Pearl");
		if (enabled) {
			EnderPearl pearl = player.launchProjectile(EnderPearl.class);
			pearl.setPassenger(player);
			player.spigot().setCollidesWithEntities(false);
		}
	}

	public void giveJoinItems(Player player) {
		player.getInventory().clear();
		for (String sect : xHub.getInstance().getSettingsYML().getConfig().getStringList("Join-Effects")) {
			YamlConfiguration conf = xHub.getInstance().getSettingsYML().getConfig();
			player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(conf.getString("Join-Effects." + sect + ".Effect")), Integer.MAX_VALUE, conf.getInt("Join-Effects." + sect + ".Power") - 1));
		}
		for (String sect : xHub.getInstance().getConfig().getConfigurationSection("Join-Inventory").getKeys(false)) {
			String mat = xHub.getInstance().getConfig().getString("Join-Inventory." + sect + ".Material");
			String name = CC.chat(xHub.getInstance().getConfig().getString("Join-Inventory." + sect + ".Name"));
			List<String> lore = CC.list(xHub.getInstance().getConfig().getStringList("Join-Inventory." + sect + ".Lore"));
			int data = xHub.getInstance().getConfig().getInt("Join-Inventory." + sect + ".Data");
			int slot = xHub.getInstance().getConfig().getInt("Join-Inventory." + sect + ".Slot");
			int amount = xHub.getInstance().getConfig().getInt("Join-Inventory." + sect + ".Amount");
			ItemStack stack = new ItemBuilder(Material.valueOf(mat), amount).displayName(name).data((short) data).setLore(lore).build();
			player.getInventory().setItem(slot - 1, stack);
		}
		player.updateInventory();
	}
}
