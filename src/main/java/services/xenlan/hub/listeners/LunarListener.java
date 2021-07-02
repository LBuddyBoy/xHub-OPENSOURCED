package services.xenlan.hub.listeners;

import com.lunarclient.bukkitapi.LunarClientAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.api.RankAPI;
import services.xenlan.hub.xHub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LunarListener {

	public LunarListener() {
		if (!xHub.getInstance().getSettingsYML().getConfig().getBoolean("LunarClientAPI.Enabled")) {
			return;
		}
		Bukkit.getScheduler().runTaskTimerAsynchronously(xHub.getInstance(), () -> {
			for (Player player : Bukkit.getOnlinePlayers()) {
				int radiusX = 60;
				int radiusY = 60;
				int radiusZ = 60;
				List<Entity> entityList = player.getNearbyEntities(radiusX, radiusY, radiusZ);
				for (Entity en : entityList) {
					if (en instanceof Player) {
						Player other = (Player) en;
						if (player.getUniqueId() == other.getUniqueId())
							continue;
						if (xHub.getInstance().getSettingsYML().getConfig().getBoolean("LunarClientAPI.Above-Head.Enabled"))
							LunarClientAPI.getInstance().overrideNametag(other, Arrays.asList(fetchNametag(other, player).toArray(new String[0])), player);
					}
				}
			}
		}, 0L, 15L);
	}
	public List<String> fetchNametag(Player target, Player viewer) {
		List<String> tag = new ArrayList<>();

		// If we already found something above they override these, otherwise we can do these checks.
		String realTag = xHub.getInstance().getSettingsYML().getConfig().getString("LunarClientAPI.Above-Head.Format-Nametag");
		String formatstaff = xHub.getInstance().getSettingsYML().getConfig().getString("LunarClientAPI.Above-Head.Staff-Tag.Format");
		String perm = xHub.getInstance().getSettingsYML().getConfig().getString("LunarClientAPI.Above-Head.Staff-Tag.Permission");
		boolean enabled = xHub.getInstance().getSettingsYML().getConfig().getBoolean("LunarClientAPI.Above-Head.Staff-Tag.Enabled");

		if (enabled) {
			if (target.hasPermission(perm)) {
				tag.add(CC.chat(formatstaff.replaceAll("%prefix%", RankAPI.checkPrefix(target))));
				tag.add(CC.chat(realTag
						.replaceAll("%pvp%", getPvP(target))
						.replaceAll("%name%", target.getName())));

				return tag;
			}
		}
		String format = xHub.getInstance().getSettingsYML().getConfig().getString("LunarClientAPI.Above-Head.Format");
		tag.add(CC.chat(format.replaceAll("%prefix%", RankAPI.checkPrefix(target))));
		tag.add(CC.chat(realTag
				.replaceAll("%pvp%", getPvP(target))
				.replaceAll("%name%", target.getName())));
		return tag;
	}

	private String getPvP(Player player) {
		if (player.hasMetadata("pvpmode")) {
			String realTag = xHub.getInstance().getSettingsYML().getConfig().getString("LunarClientAPI.Above-Head.PvP-Display");
			return CC.chat(realTag);
		}
		return "";
	}

}
