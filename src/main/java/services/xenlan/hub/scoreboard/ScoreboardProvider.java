package services.xenlan.hub.scoreboard;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import services.xenlan.hub.bungee.BungeeListener;
import services.xenlan.hub.customtimer.CustomTimer;
import services.xenlan.hub.pvp.cooldown.util.CooldownFormatter;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.api.RankAPI;
import services.xenlan.hub.xHub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScoreboardProvider implements AssembleAdapter {

	@Override
	public String getTitle(Player player) {
		return CC.chat(xHub.getInstance().getScoreboardYML().getConfig().getString("Scoreboard.Title"));
	}

	@Override
	public List<String> getLines(Player player) {
		List<String> toReturn = new ArrayList<>();
		Collection<CustomTimer> customtimer = xHub.getInstance().getCustomTimerManager().getCustomtimer();

		if (xHub.getInstance().isPlaceholderAPI()) {
			if (!player.hasMetadata("pvpmode")) {
				if (!xHub.getInstance().getQueue().inQueue(player)) {
					for (String lines : xHub.getInstance().getScoreboardYML().getConfig().getStringList("Scoreboard.Default"))
						toReturn.add(CC.chat(PlaceholderAPI.setPlaceholders(player, lines))
								.replaceAll("%rank%", RankAPI.checkRank(player))
								.replaceAll("%online%", String.valueOf(BungeeListener.PLAYER_COUNT)));
				} else {
					for (String lines : xHub.getInstance().getScoreboardYML().getConfig().getStringList("Scoreboard.Queued"))
						toReturn.add(CC.chat(PlaceholderAPI.setPlaceholders(player, lines))
								.replaceAll("%rank%", RankAPI.checkRank(player))
								.replaceAll("%online%", String.valueOf(BungeeListener.PLAYER_COUNT))
								.replaceAll("%queue%", xHub.getInstance().getQueue().getQueueIn(player))
								.replaceAll("%queue-pos%", String.valueOf(xHub.getInstance().getQueue().getPosition(player)))
								.replaceAll("%queue-size%", String.valueOf(xHub.getInstance().getQueue().getQueueSize(xHub.getInstance().getQueue().getQueueIn(player)))));
				}
			} else {
				for (String lines : xHub.getInstance().getScoreboardYML().getConfig().getStringList("Scoreboard.PvP-Mode")) {
					for (String sect2 : xHub.getInstance().getPvpYML().getConfig().getConfigurationSection("GUI.Items").getKeys(false)) {
						if (xHub.getInstance().getPvpYML().getConfig().getString("GUI.Items." + sect2 + ".Kit-Name").equalsIgnoreCase(xHub.getInstance().getKitManager().getKit().get(player))) {
							toReturn.add(CC.chat(PlaceholderAPI.setPlaceholders(player, lines
									.replaceAll("%kit%", ChatColor.valueOf(xHub.getInstance().getPvpYML().getConfig().getString("GUI.Items."
											+ sect2 + ".Color")) + xHub.getInstance().getKitManager().getKit().get(player)))));
						}
					}
				}
			}
		} else {
			if (!player.hasMetadata("pvpmode")) {
				if (!xHub.getInstance().getQueue().inQueue(player)) {
					for (String lines : xHub.getInstance().getScoreboardYML().getConfig().getStringList("Scoreboard.Default"))
						toReturn.add(CC.chat(lines
								.replaceAll("%rank%", RankAPI.checkRank(player))
								.replaceAll("%online%", String.valueOf(BungeeListener.PLAYER_COUNT))));
				} else {
					for (String lines : xHub.getInstance().getScoreboardYML().getConfig().getStringList("Scoreboard.Queued"))
						toReturn.add(CC.chat(lines
								.replaceAll("%rank%", RankAPI.checkRank(player))
								.replaceAll("%online%", String.valueOf(BungeeListener.PLAYER_COUNT))
								.replaceAll("%queue%", xHub.getInstance().getQueue().getQueueIn(player))
								.replaceAll("%queue-pos%", String.valueOf(xHub.getInstance().getQueue().getPosition(player)))
								.replaceAll("%queue-size%", String.valueOf(xHub.getInstance().getQueue().getQueueSize(xHub.getInstance().getQueue().getQueueIn(player))))));
				}
			} else {

				for (String lines : xHub.getInstance().getScoreboardYML().getConfig().getStringList("Scoreboard.PvP-Mode")) {
					for (String sect2 : xHub.getInstance().getPvpYML().getConfig().getConfigurationSection("GUI.Items").getKeys(false)) {
						if (xHub.getInstance().getPvpYML().getConfig().getString("GUI.Items." + sect2 + ".Kit-Name").equalsIgnoreCase(xHub.getInstance().getKitManager().getKit().get(player))) {
							toReturn.add(CC.chat(lines
									.replaceAll("%kit%", ChatColor.valueOf(xHub.getInstance().getPvpYML().getConfig().getString("GUI.Items."
											+ sect2 + ".Color")) + xHub.getInstance().getKitManager().getKit().get(player))));
						}
					}
				}
			}
		}
		if (xHub.getInstance().getScoreboardYML().getConfig().getBoolean("Scoreboard.Combat-Tag.Show-On-ScoreBoard")) {
			if (xHub.getInstance().getPvPManager().getCooldown().onCooldown(player)) {
				String display = xHub.getInstance().getScoreboardYML().getConfig().getString("Scoreboard.Combat-Tag.Display");
				toReturn.add(CC.chat(display + ": &f" + xHub.getInstance().getPvPManager().getCooldown().getRemaining(player)));
			}
		}
		if (xHub.getInstance().getScoreboardYML().getConfig().getBoolean("Scoreboard.EnderPearl-Cooldown.Show-On-ScoreBoard")) {
			if (xHub.getInstance().getPvPManager().getEnderpearl().onCooldown(player)) {
				String display = xHub.getInstance().getScoreboardYML().getConfig().getString("Scoreboard.EnderPearl-Cooldown.Display");
				toReturn.add(CC.chat(display + ": &f" + xHub.getInstance().getPvPManager().getEnderpearl().getRemaining(player)));
			}
		}

		for (CustomTimer timer : customtimer) {
			toReturn.add(timer.getScoreboard() + "&7: &f" + CooldownFormatter.getRemaining(timer.getRemaining(), true));
		}

		if (!customtimer.isEmpty() || xHub.getInstance().getPvPManager().getCooldown().onCooldown(player) || xHub.getInstance().getPvPManager().getEnderpearl().onCooldown(player)) {
			toReturn.add(CC.chat(xHub.getInstance().getScoreboardYML().getConfig().getString("Scoreboard.Extra-Seperator")));
		}
		return toReturn;
	}

}
