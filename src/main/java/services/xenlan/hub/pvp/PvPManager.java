package services.xenlan.hub.pvp;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import services.xenlan.hub.action.ActionManager;
import services.xenlan.hub.pvp.cooldown.PvPCooldown;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;

public class PvPManager {
	private xHub xHub;

	@Getter
	public PvPCooldown cooldown = new PvPCooldown();

	public PvPManager(xHub xHub) {
		this.xHub = xHub;
	}

	@Getter
	public PvPCooldown enderpearl = new PvPCooldown();

	public void engagePvPMode(Player player) {
		player.setNoDamageTicks(0);
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
		player.setMetadata("pvpmode", new FixedMetadataValue(xHub, player));
		player.removeMetadata("vanished", this.xHub);
		for (String list : this.xHub.getMessagesYML().getConfig().getStringList("Turned-PvP-On"))
			player.sendMessage(CC.chat(list));
		teleportToArena(player);
		player.getInventory().clear();
		for (Player online : Bukkit.getOnlinePlayers())
			online.showPlayer(player);
		for (Player online : Bukkit.getOnlinePlayers())
			player.showPlayer(online);
	}

	public void disengagePvPMode(Player player, boolean sendMessage) {
		player.setHealth(20);
		player.setNoDamageTicks(120);
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
		if (sendMessage) {
			for (String list : this.xHub.getMessagesYML().getConfig().getStringList("Turned-PvP-Off"))
				player.sendMessage(CC.chat(list));
		}
		xHub.getKitManager().getKit().put(player, null);
		xHub.getTrailsManager().getCosm().remove(player.getUniqueId());
		xHub.getTrailsManager().getActiveTrail().remove(player.getUniqueId());
		xHub.getGearManager().getActiveGear().remove(player.getUniqueId());
		xHub.getHatManager().getActiveHat().remove(player.getUniqueId());
		player.removeMetadata("pvpmode", xHub);
		player.removeMetadata("vanished", xHub);
		player.getInventory().setArmorContents(null);
		new ActionManager().giveJoinItems(player);
		this.cooldown.removeCooldown(player);

	}

	public void teleportToSpawn(Player player) {
		Location location = player.getLocation();
		float yaw = xHub.getSettingsYML().getConfig().getInt("Spawn.Yaw");
		float pitch = xHub.getSettingsYML().getConfig().getInt("Spawn.Pitch");
		double x = xHub.getSettingsYML().getConfig().getDouble("Spawn.X");
		double y = xHub.getSettingsYML().getConfig().getDouble("Spawn.Y");
		double z = xHub.getSettingsYML().getConfig().getDouble("Spawn.Z");
		location.setYaw(yaw);
		location.setPitch(pitch);
		location.setY(y);
		location.setX(x);
		location.setZ(z);
		player.teleport(location);
	}

	public void teleportToArena(Player player) {
		Location location = player.getLocation();
		float yaw = xHub.getPvpYML().getConfig().getInt("Spawn.Yaw");
		float pitch = xHub.getPvpYML().getConfig().getInt("Spawn.Pitch");
		double x = xHub.getPvpYML().getConfig().getDouble("Spawn.X");
		double y = xHub.getPvpYML().getConfig().getDouble("Spawn.Y");
		double z = xHub.getPvpYML().getConfig().getDouble("Spawn.Z");
		location.setYaw(yaw);
		location.setPitch(pitch);
		location.setY(y);
		location.setX(x);
		location.setZ(z);
		player.teleport(location);
	}
}
