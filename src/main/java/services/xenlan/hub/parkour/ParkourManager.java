package services.xenlan.hub.parkour;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import services.xenlan.hub.action.ActionManager;
import services.xenlan.hub.parkour.command.ParkourCommand;
import services.xenlan.hub.parkour.listener.Listener;
import services.xenlan.hub.parkour.listener.ParkourListener;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;
import services.xenlan.hub.utils.ItemBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ParkourManager {

	private xHub hub;

	public HashSet<Player> parkour;
	public HashMap<Player, Location> checkPointLoc;
	public HashSet<Player> clickedPlate;

	public ParkourManager(xHub xHub) {
		this.hub = xHub;
		parkour = new HashSet<>();
		checkPointLoc = new HashMap<>();
		clickedPlate = new HashSet<>();
		Bukkit.getPluginManager().registerEvents(new ParkourListener(), hub);
		Bukkit.getPluginManager().registerEvents(new Listener(), hub);
		hub.getCommand("parkour").setExecutor(new ParkourCommand());
	}

	public void saveCheckPoint(Player player) {
		if (parkour.contains(player)) {
			checkPointLoc.putIfAbsent(player, player.getLocation());
			if (!checkPointLoc.containsKey(player)) {
				for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CheckPoint-Set")) {
					player.sendMessage(CC.chat(message));
				}
			}
			if (checkPointLoc.get(player).equals(player.getLocation())) {
				for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CheckPoint-Already-Set")) {
					player.sendMessage(CC.chat(message));
				}
				return;
			}
			if (!checkPointLoc.get(player).equals(player.getLocation())) {
				for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CheckPoint-Set")) {
					player.sendMessage(CC.chat(message));
				}
				checkPointLoc.put(player, player.getLocation());
			}
		}
	}

	public void activateParkour(Player player, Location location) {
		if (player.hasMetadata("pvpmode")) {
			return;
		}
		for (PotionEffect pot : player.getActivePotionEffects())
			player.removePotionEffect(pot.getType());
		for (String section : xHub.getInstance().getSettingsYML().getConfig().getConfigurationSection("Parkour.Effects").getKeys(false)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(section), Integer.MAX_VALUE, xHub.getInstance().getSettingsYML().getConfig().getInt("Parkour.Effects." + section + ".Amplifier") - 1));
		}
		player.getInventory().clear();
		parkour.add(player);
		giveItems(player);
		player.teleport(location);
		for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("Parkour-Started")) {
			player.sendMessage(CC.chat(message));
		}
		String sound = hub.getSettingsYML().getConfig().getString("Parkour.Start-Sound.Sound");
		boolean enabled = hub.getSettingsYML().getConfig().getBoolean("Parkour.Start-Sound.Enabled");
		if (enabled) {
			player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, 1.0f);
		}
	}

	public void finishParkour(Player player) {
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("Walk-Speed.Enabled")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, xHub.getInstance().getSettingsYML().getConfig().getInt("Walk-Speed.Speed") - 1, true));
		}
		player.getInventory().clear();
		parkour.remove(player);
		teleportToSpawn(player);
		if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("Parkour.Broadcast.Enabled")) {
			for (String message : xHub.getInstance().getSettingsYML().getConfig().getStringList("Parkour.Broadcast.Message")) {
				Bukkit.broadcastMessage(CC.chat(message).replaceAll("%player%", player.getName()));
			}
		}
		for (Player online : Bukkit.getOnlinePlayers()) {
			String sound = hub.getSettingsYML().getConfig().getString("Parkour.Finish-Sound.Sound");
			boolean enabled = hub.getSettingsYML().getConfig().getBoolean("Parkour.Finish-Sound.Enabled");
			if (enabled) {
				online.playSound(online.getLocation(), Sound.valueOf(sound), 1.0f, 1.0f);
			}
		}
		checkPointLoc.remove(player);
		new ActionManager().giveJoinItems(player);

	}

	public void deactivateParkour(Player player) {
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("Walk-Speed.Enabled")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, xHub.getInstance().getSettingsYML().getConfig().getInt("Walk-Speed.Speed") - 1, true));
		}
		player.getInventory().clear();
		parkour.remove(player);
		teleportToSpawn(player);
		new ActionManager().giveJoinItems(player);
		for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("Parkour-Left")) {
			player.sendMessage(CC.chat(message));
		}
		String sound = hub.getSettingsYML().getConfig().getString("Parkour.Left-Sound.Sound");
		boolean enabled = hub.getSettingsYML().getConfig().getBoolean("Parkour.Left-Sound.Enabled");
		if (enabled) {
			player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0f, 1.0f);
		}
		checkPointLoc.remove(player);
	}


	private void teleportToSpawn(Player player) {
		Location location = player.getLocation();
		float yaw = xHub.getInstance().getSettingsYML().getConfig().getInt("Spawn.Yaw");
		float pitch = xHub.getInstance().getSettingsYML().getConfig().getInt("Spawn.Pitch");
		double x = xHub.getInstance().getSettingsYML().getConfig().getDouble("Spawn.X");
		double y = xHub.getInstance().getSettingsYML().getConfig().getDouble("Spawn.Y");
		double z = xHub.getInstance().getSettingsYML().getConfig().getDouble("Spawn.Z");

		location.setYaw(yaw);
		location.setPitch(pitch);
		location.setY(y);
		location.setX(x);
		location.setZ(z);

		player.teleport(location);

	}

	public void giveItems(Player player) {
		player.getInventory().clear();
		for (String sect : xHub.getInstance().getConfig().getConfigurationSection("Parkour-Inventory").getKeys(false)) {
			String mat = xHub.getInstance().getConfig().getString("Parkour-Inventory." + sect + ".Material");
			String name = CC.chat(xHub.getInstance().getConfig().getString("Parkour-Inventory." + sect + ".Name"));
			List<String> lore = CC.list(xHub.getInstance().getConfig().getStringList("Parkour-Inventory." + sect + ".Lore"));
			int data = xHub.getInstance().getConfig().getInt("Parkour-Inventory." + sect + ".Data");
			int slot = xHub.getInstance().getConfig().getInt("Parkour-Inventory." + sect + ".Slot");
			int amount = xHub.getInstance().getConfig().getInt("Parkour-Inventory." + sect + ".Amount");
			ItemStack stack = new ItemBuilder(Material.valueOf(mat), amount).displayName(name).data((short) data).setLore(lore).build();
			player.getInventory().setItem(slot - 1, stack);
		}
		player.updateInventory();
	}

}
