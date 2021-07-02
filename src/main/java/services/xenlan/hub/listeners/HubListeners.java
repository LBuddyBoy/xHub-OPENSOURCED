package services.xenlan.hub.listeners;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import services.xenlan.hub.action.ActionManager;
import services.xenlan.hub.bungee.BungeeListener;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.api.RankAPI;
import services.xenlan.hub.xHub;

public class HubListeners implements Listener {

	private xHub hub;

	public HubListeners() {
		hub = xHub.getInstance();
	}


	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("Chat.Enabled")) {
			Bukkit.getConsoleSender().sendMessage(CC.chat(xHub.getInstance().getSettingsYML().getConfig().getString("Chat.Format")
					.replaceAll("%kills%", String.valueOf(event.getPlayer().getStatistic(Statistic.PLAYER_KILLS)))
					.replaceAll("%tag%", RankAPI.checkTag(event.getPlayer()))
					.replaceAll("%suffix%", RankAPI.checkSuffix(event.getPlayer()))
					.replaceAll("%prefix%", RankAPI.checkPrefix(event.getPlayer()))
					.replaceAll("%message%", event.getMessage())
					.replaceAll("%name%", event.getPlayer().getName())));
			event.setCancelled(true);
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				online.sendMessage(CC.chat(xHub.getInstance().getSettingsYML().getConfig().getString("Chat.Format")
						.replaceAll("%tag%", RankAPI.checkTag(event.getPlayer()))
						.replaceAll("%suffix%", RankAPI.checkSuffix(event.getPlayer()))
						.replaceAll("%kills%", String.valueOf(event.getPlayer().getStatistic(Statistic.PLAYER_KILLS)))
						.replaceAll("%prefix%", RankAPI.checkPrefix(event.getPlayer()))
						.replaceAll("%message%", event.getMessage())
						.replaceAll("%name%", event.getPlayer().getName())));
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {

		if (xHub.getInstance().getBuildmode().contains(event.getPlayer())) {
			event.setCancelled(false);
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {

		if (xHub.getInstance().getBuildmode().contains(event.getPlayer())) {
			event.setCancelled(false);
			return;
		}
		event.setCancelled(true);

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (xHub.getInstance().getConfig().getBoolean("Join-Message.Enabled")) {
			for (String message : xHub.getInstance().getConfig().getStringList("Join-Message.Message")) {
				event.getPlayer().sendMessage(CC.chat(message));
			}
		}
		if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("Join-Message.Enabled")) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				for (String message : xHub.getInstance().getSettingsYML().getConfig().getStringList("Join-Message.Message")) {
					online.sendMessage(CC.chat(message)
							.replaceAll("%prefix%", CC.chat(xHub.getChat().getPlayerPrefix(event.getPlayer())))
							.replaceAll("%player%", event.getPlayer().getName()));
				}
			}
		}

		event.setJoinMessage(null);
		event.getPlayer().getInventory().setArmorContents(null);
		event.getPlayer().getInventory().clear();
		new ActionManager().giveJoinItems(event.getPlayer());
		teleportToSpawn(event.getPlayer());
		new BukkitRunnable() {
			@Override
			public void run() {
				boolean ena = xHub.getInstance().getSettingsYML().getConfig().getBoolean("Restricted-Hub.Enabled");
				if (ena) {
					if (event.getPlayer().hasPermission("xhub.restricted")) {
						String server = xHub.getInstance().getSettingsYML().getConfig().getString("Restricted-Hub.Bungee-Name");
						if (xHub.getInstance().getConfig().getBoolean("BungeeCord")) {
							BungeeListener.sendPlayerToServer(event.getPlayer(), server);
						} else {
							Bukkit.getConsoleSender().sendMessage(CC.chat("&cWe tried to send " + event.getPlayer().getName() + " to a restricted hub, but couldn't because xHub doesn't have BungeeCord enabled in the config.yml."));
						}
					}
				}
			}
		}.runTaskLater(xHub.getInstance(), 20 * 3);
	}

	@EventHandler
	public void onVoid(PlayerMoveEvent event) {

		if (event.getPlayer().getLocation().getY() == 10) {
			teleportToSpawn(event.getPlayer());
		}

	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (player.getGameMode() != GameMode.CREATIVE
				&& player.getLocation().subtract(0.0, 0.1, 0.0).getBlock().getType() != Material.AIR
				&& !player.isFlying()) {
			if (xHub.getInstance().getParkourManager().parkour.contains(player)) {
				player.setAllowFlight(false);
				return;
			}
			if (player.hasMetadata("pvpmode")) {
				player.setAllowFlight(false);
				return;
			}
			player.setAllowFlight(true);
		}
	}


	@EventHandler
	public void onFly(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("Double-Jump.Enabled")) {
			if (player.getGameMode() == GameMode.CREATIVE) {
				return;
			}
			if (xHub.getInstance().getParkourManager().parkour.contains(player)) {
				player.setAllowFlight(false);
				event.setCancelled(true);
			}
			if (player.hasMetadata("pvpmode")) {
				player.setAllowFlight(false);
				event.setCancelled(true);
			}

			event.setCancelled(true);
			player.setAllowFlight(false);
			player.setFlying(false);
			player.setVelocity(player.getLocation().getDirection().multiply(xHub.getInstance().getSettingsYML().getConfig().getDouble("Double-Jump.Boost")).setY(1));
			player.playSound(player.getLocation(), Sound.valueOf(xHub.getInstance().getSettingsYML().getConfig().getString("Double-Jump.Sound")), 2.0f, 1.0f);

		}
	}

	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent event) {

		if (event.getDamager() instanceof Player) {
			if (event.getEntity() instanceof Player) {

				Player damaged = (Player) event.getEntity();
				Player damager = (Player) event.getDamager();

				if (damaged.hasMetadata("pvpmode") && damager.hasMetadata("pvpmode")) {
					event.setCancelled(false);
					xHub.getInstance().getPvPManager().getCooldown().applyCooldown(damaged, xHub.getInstance().getScoreboardYML().getConfig().getInt("Scoreboard.Combat-Tag.Time") * 1000);
					xHub.getInstance().getPvPManager().getCooldown().applyCooldown(damager, xHub.getInstance().getScoreboardYML().getConfig().getInt("Scoreboard.Combat-Tag.Time") * 1000);
				}

				if (!damaged.hasMetadata("pvpmode")) {
					event.setCancelled(true);
				}
				if (!damager.hasMetadata("pvpmode")) {
					event.setCancelled(true);
				}

			}
		}

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (xHub.getInstance().getQueue().inQueue(event.getPlayer())) {
			xHub.getInstance().getQueueManager().getQueue(event.getPlayer()).removeFromQueue(event.getPlayer());
		}
		if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("Leave-Message.Enabled")) {
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				for (String message : xHub.getInstance().getSettingsYML().getConfig().getStringList("Leave-Message.Message")) {
					online.sendMessage(CC.chat(message)
							.replaceAll("%prefix%", CC.chat(xHub.getChat().getPlayerPrefix(event.getPlayer())))
							.replaceAll("%player%", event.getPlayer().getName()));
				}
			}
		}
		xHub.getInstance().getParkourManager().parkour.remove(event.getPlayer());
		event.setQuitMessage(null);
		event.getPlayer().getInventory().clear();
		xHub.getInstance().getPvPManager().disengagePvPMode(event.getPlayer(), false);
		event.getPlayer().removeMetadata("pvpmode", xHub.getInstance());

	}

	@EventHandler
	public void onMobspawn(EntitySpawnEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onMobTarget(EntityTargetEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onInvInter(InventoryClickEvent event) {
		if (event.getWhoClicked().getGameMode() == GameMode.CREATIVE && !event.getWhoClicked().hasMetadata("pvpmode")) {
			event.setCancelled(false);
			return;
		}
		if (event.getWhoClicked().hasMetadata("pvpmode")) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFoodLevel(FoodLevelChangeEvent event) {
		if (event.getEntity().hasMetadata("pvpmode")) {
			event.setFoodLevel(20);
			return;
		}
		event.setFoodLevel(20);
		event.setCancelled(true);
	}

	@EventHandler
	public void onInvInter(InventoryDragEvent event) {
		if (event.getWhoClicked().getGameMode() == GameMode.CREATIVE && !event.getWhoClicked().hasMetadata("pvpmode")) {
			event.setCancelled(false);
			return;
		}
		if (event.getWhoClicked().hasMetadata("pvpmode")) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInvInter(InventoryInteractEvent event) {
		if (event.getWhoClicked().getGameMode() == GameMode.CREATIVE && !event.getWhoClicked().hasMetadata("pvpmode")) {
			event.setCancelled(false);
			return;
		}
		if (event.getWhoClicked().hasMetadata("pvpmode")) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!event.getEntity().hasMetadata("pvpmode")) {
			event.setCancelled(true);
		}
	}


	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setDeathMessage(null);

		if (event.getEntity().getKiller() == null) {
			return;
		}
		if (xHub.getInstance().getMessagesYML().getConfig().getBoolean("Death-Message.Enabled")) {
			for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("Death-Message.Message")) {
				Bukkit.broadcastMessage(CC.chat(message)
						.replaceAll("%kills-killer%", String.valueOf(event.getEntity().getKiller().getStatistic(Statistic.PLAYER_KILLS)))
						.replaceAll("%kills-victim%", String.valueOf(event.getEntity().getStatistic(Statistic.PLAYER_KILLS)))
						.replaceAll("%victim%", event.getEntity().getName())
						.replaceAll("%killer%", event.getEntity().getKiller().getName()));
			}
		}
	}

	@EventHandler
	public void onMoveVoid(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();

		if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("AntiVoid.Enabled")) {
			if (loc.getBlockY() <= xHub.getInstance().getSettingsYML().getConfig().getInt("AntiVoid.Max-Y")) {
				if (loc.getBlockY() <= xHub.getInstance().getSettingsYML().getConfig().getInt("AntiVoid.Min-Y")) {
					teleportToSpawn(player);
					// removed the lines from in the main because you made a whole new method when there was one in here
				}
			}
		}

	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("Walk-Speed.Enabled")) {
					event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, xHub.getInstance().getSettingsYML().getConfig().getInt("Walk-Speed.Speed") - 1, true));
				}
				xHub.getInstance().getPvPManager().disengagePvPMode(event.getPlayer(), true);
				teleportToSpawn(event.getPlayer());
			}
		}.runTaskLater(xHub.getInstance(), 5L);
	}

	public void teleportToSpawn(Player player) {
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


}
