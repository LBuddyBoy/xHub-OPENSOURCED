package services.xenlan.hub.listeners;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;
import services.xenlan.hub.action.ActionManager;
import services.xenlan.hub.xHub;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.ItemBuilder;

import java.util.List;

public class ItemListeners implements Listener {


	@SneakyThrows
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile projectile = event.getEntity();
		if (projectile instanceof EnderPearl) {
			EnderPearl enderPearl = (EnderPearl) projectile;
			ProjectileSource source = enderPearl.getShooter();
			if (source instanceof Player) {
				Player shooter = (Player) source;
				if (shooter.hasMetadata("pvpmode")) {
					if (xHub.getInstance().getPvPManager().getEnderpearl().onCooldown(shooter)) {
						for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("EnderPearl-Cooldown-Message.Message")) {
							shooter.sendMessage(CC.chat(message)
									.replaceAll("%time%", xHub.getInstance().getPvPManager().getEnderpearl().getRemaining(shooter)));
						}
						shooter.getInventory().getItemInHand().setAmount(shooter.getItemInHand().getAmount() + 1);
						event.setCancelled(true);
					} else {
						int time = xHub.getInstance().getPvpYML().getConfig().getInt("PvP-Mode.EnderPearl-Cooldown.Time");
						xHub.getInstance().getPvPManager().getEnderpearl().applyCooldown(shooter, time * 1000);

					}
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (xHub.getInstance().getBuildmode().contains(event.getPlayer()) || event.getPlayer().hasMetadata("pvpmode")) {
			event.setCancelled(false);
			return;
		}
		event.setCancelled(true);

	}

	@EventHandler
	public void onEntityDismount(EntityDismountEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player != null && player.getVehicle() instanceof EnderPearl) {
				Entity pearl = player.getVehicle();
				player.spigot().setCollidesWithEntities(true);
				player.eject();
				pearl.remove();
			}
		}
	}

	@EventHandler
	public void onInteractCustomItem(PlayerInteractEvent event) {

		if (event.getItem() == null)
			return;

		Player player = event.getPlayer();

		if (player.hasMetadata("pvpmode"))
			return;

		if (xHub.getInstance().getParkourManager().parkour.contains(player)) {
			for (String sect : xHub.getInstance().getConfig().getConfigurationSection("Parkour-Inventory").getKeys(false)) {
				List<String> clicks = xHub.getInstance().getConfig().getStringList("Parkour-Inventory." + sect + ".Click");
				for (String acts : clicks) {
					if (event.getAction().name().contains(acts)) {
						String mat = xHub.getInstance().getConfig().getString("Parkour-Inventory." + sect + ".Material");
						String name = CC.chat(xHub.getInstance().getConfig().getString("Parkour-Inventory." + sect + ".Name"));
						String action = xHub.getInstance().getConfig().getString("Parkour-Inventory." + sect + ".Action");
						List<String> lore = CC.list(xHub.getInstance().getConfig().getStringList("Parkour-Inventory." + sect + ".Lore"));
						List<String> msg = xHub.getInstance().getConfig().getStringList("Parkour-Inventory." + sect + ".Open-Message.Message");
						int data = xHub.getInstance().getConfig().getInt("Parkour-Inventory." + sect + ".Data");
						boolean enabled = xHub.getInstance().getConfig().getBoolean("Parkour-Inventory." + sect + ".Open-Message.Enabled");
						ItemStack stack = new ItemBuilder(Material.valueOf(mat)).displayName(name).data((short) data).setLore(lore).build();
						if (stack.isSimilar(event.getItem())) {
							event.setUseItemInHand(Event.Result.DENY);
							event.setCancelled(true);
							new ActionManager().performAction(action, event.getPlayer());
							event.getPlayer().updateInventory();
							if (enabled) {
								for (String mes : msg) {
									event.getPlayer().sendMessage(CC.chat(mes.replaceAll("%toggle-status%", toggleStatus(event.getPlayer()))));
								}
							}
						}
					}
				}
			}
		} else {
			for (String sect : xHub.getInstance().getConfig().getConfigurationSection("Join-Inventory").getKeys(false)) {
				List<String> clicks = xHub.getInstance().getConfig().getStringList("Join-Inventory." + sect + ".Click");
				for (String acts : clicks) {
					if (event.getAction().name().contains(acts)) {
						String mat = xHub.getInstance().getConfig().getString("Join-Inventory." + sect + ".Material");
						String name = CC.chat(xHub.getInstance().getConfig().getString("Join-Inventory." + sect + ".Name"));
						String action = xHub.getInstance().getConfig().getString("Join-Inventory." + sect + ".Action");
						List<String> lore = CC.list(xHub.getInstance().getConfig().getStringList("Join-Inventory." + sect + ".Lore"));
						List<String> msg = xHub.getInstance().getConfig().getStringList("Join-Inventory." + sect + ".Open-Message.Message");
						int data = xHub.getInstance().getConfig().getInt("Join-Inventory." + sect + ".Data");
						boolean enabled = xHub.getInstance().getConfig().getBoolean("Join-Inventory." + sect + ".Open-Message.Enabled");
						ItemStack stack = new ItemBuilder(Material.valueOf(mat)).displayName(name).data((short) data).setLore(lore).build();
						if (stack.isSimilar(event.getItem())) {
							if (player.isInsideVehicle())
								player.getVehicle().setPassenger(null);
							event.setUseItemInHand(Event.Result.DENY);
							event.setCancelled(true);
							new ActionManager().performAction(action, event.getPlayer());
							event.getPlayer().updateInventory();
							if (enabled) {
								for (String mes : msg) {
									event.getPlayer().sendMessage(CC.chat(mes.replaceAll("%toggle-status%", toggleStatus(event.getPlayer()))));
								}
							}
						}
					}
				}
			}
		}
	}

	public String toggleStatus(Player player) {
		if (player.hasMetadata("vanished")) {
			return "off";
		}
		return "on";
	}

	@EventHandler
	public void onPressure(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.PHYSICAL)) {
			if (event.getClickedBlock() == null)
				return;

			if (event.getPlayer().hasMetadata("pvpmode"))
				return;

			if (xHub.getInstance().getParkourManager().parkour.contains(event.getPlayer()))
				return;

			if (event.getClickedBlock().getType() == Material.valueOf(xHub.getInstance().getSettingsYML().getConfig().getString("Bump-Plate.Material"))) {
				Player player = event.getPlayer();
				player.setVelocity(new Vector(player.getLocation().getDirection().getX() *
						xHub.getInstance().getSettingsYML().getConfig().getInt("Bump-Plate.Boost.Z"), xHub.getInstance().getSettingsYML().getConfig().getInt("Bump-Plate.Boost.Y"),
						player.getLocation().getDirection().getZ() * xHub.getInstance().getSettingsYML().getConfig().getInt("Bump-Plate.Boost.X")));
				player.playSound(player.getLocation(), Sound.valueOf(xHub.getInstance().getSettingsYML().getConfig().getString("Bump-Plate.Sound")), 2.0f, 1.0f);
			}
		}
	}
}
