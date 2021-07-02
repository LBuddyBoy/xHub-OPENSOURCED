package services.xenlan.hub.utils.api;

import club.frozed.core.ZoomAPI;
import com.broustudio.MizuAPI.MizuAPI;
import me.activated.core.plugin.AquaCoreAPI;
import me.quartz.hestia.HestiaAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;

public class RankAPI {

	public static boolean isVault = false;

	public static String checkVaultRankPrefix(Player player) {
		if (checkRankCore().equalsIgnoreCase("vault")) {
			for (String section : xHub.getInstance().getSettingsYML().getConfig().getConfigurationSection("Vault-Ranks").getKeys(false)) {
				if (xHub.getChat().getPrimaryGroup(player).equalsIgnoreCase(xHub.getInstance().getSettingsYML().getConfig().getString("Vault-Ranks." + section + ".Group-Name")))
					return CC.chat(xHub.getInstance().getSettingsYML().getConfig().getString("Vault-Ranks." + section + ".Color") + xHub.getInstance().getSettingsYML().getConfig().getString("Vault-Ranks." + section + ".Display-Name"));

			}
		}
		return "" + xHub.getChat().getPrimaryGroup(player);
	}

	public static String checkRank(Player player) {
		if (checkRankCore().equalsIgnoreCase("mizu")) {
			return MizuAPI.getAPI().getRankColor(MizuAPI.getAPI().getRank(player.getUniqueId())) + MizuAPI.getAPI().getRank(player.getUniqueId());
		} else if (!isVault) {
			return "";
		} else if (checkRankCore().equalsIgnoreCase("aquacore")) {
			return AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getColor() + AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getName();
		} else if (checkRankCore().equalsIgnoreCase("hestia")) {
			return HestiaAPI.instance.getRankColor(player.getUniqueId()) + HestiaAPI.instance.getRank(player.getUniqueId());
		} else if (checkRankCore().equalsIgnoreCase("vault")) {
			return checkVaultRankPrefix(player);
		} else if (checkRankCore().equalsIgnoreCase("zoom")) {
			return ZoomAPI.getRankColor(player) + ZoomAPI.getRankName(player);
		} else {
			return "";
		}
	}

	public static String checkPrefix(Player player) {
		if (checkRankCore().equalsIgnoreCase("mizu")) {
			return MizuAPI.getAPI().getRankPrefix(MizuAPI.getAPI().getRank(player.getUniqueId()));
		} else if (!isVault) {
			return "";
		} else if (checkRankCore().equalsIgnoreCase("aquacore")) {
			return AquaCoreAPI.INSTANCE.getPlayerRank(player.getUniqueId()).getPrefix();
		} else if (checkRankCore().equalsIgnoreCase("hestia")) {
			return HestiaAPI.instance.getRankPrefix(player.getUniqueId());
		} else if (checkRankCore().equalsIgnoreCase("vault")) {
			return xHub.getChat().getPlayerPrefix(player);
		} else if (checkRankCore().equalsIgnoreCase("zoom")) {
			return ZoomAPI.getRankPrefix(player);
		} else {
			return "";
		}
	}

	public static String checkTag(Player player) {
		if (checkRankCore().equalsIgnoreCase("mizu")) {
			if (MizuAPI.getAPI().getTag(player.getUniqueId()) == null)
				return "";
			return MizuAPI.getAPI().getTag(player.getUniqueId());
		} else if (checkRankCore().equalsIgnoreCase("aquacore")) {
			if (AquaCoreAPI.INSTANCE.getTag(player.getUniqueId()) == null)
				return "";
			return AquaCoreAPI.INSTANCE.getTag(player.getUniqueId()).getPrefix();
		} else if (checkRankCore().equalsIgnoreCase("hestia")) {
			if (HestiaAPI.instance.getTag(player.getUniqueId()) == null)
				return "";
			return HestiaAPI.instance.getTagPrefix(player.getUniqueId());
		} else {
			return "";
		}
	}

	public static String checkSuffix(Player player) {
		if (!isVault) {
			return "";
		}
		return xHub.getChat().getPlayerSuffix(player);
	}

	public static String checkRankCore() {
		String rankcore = xHub.getInstance().getConfig().getString("Rank-Core");
		switch (rankcore.toUpperCase()) {
			case "MIZU":
				return "mizu";
			case "AQUACORE":
				return "aquacore";
			case "VAULT":
				return "vault";
			case "HESTIA":
				return "hestia";
			case "ZOOM":
				return "zoom";
			default:
				return "None";
		}
	}

	public static ChatColor getRankColor(String rank) {
		for (String sect : xHub.getInstance().getSettingsYML().getConfig().getConfigurationSection("Vault-Ranks").getKeys(false)) {
			if (xHub.getInstance().getSettingsYML().getConfig().getString("Vault-Ranks." + sect + ".Group-Name").equalsIgnoreCase(rank))
				return getColor(xHub.getInstance().getSettingsYML().getConfig().getString("Vault-Ranks." + sect + ".Color"));
		}
		return ChatColor.WHITE;
	}

	public static ChatColor getColor(String path) {
		switch (path) {
			case "&1":
				return ChatColor.DARK_BLUE;
			case "&2":
				return ChatColor.DARK_GREEN;
			case "&3":
				return ChatColor.DARK_AQUA;
			case "&4":
				return ChatColor.DARK_RED;
			case "&5":
				return ChatColor.DARK_PURPLE;
			case "&6":
				return ChatColor.GOLD;
			case "&7":
				return ChatColor.GRAY;
			case "&8":
				return ChatColor.DARK_GRAY;
			case "&9":
				return ChatColor.BLUE;
			case "&0":
				return ChatColor.BLACK;
			case "&a":
				return ChatColor.GREEN;
			case "&b":
				return ChatColor.AQUA;
			case "&c":
				return ChatColor.RED;
			case "&d":
				return ChatColor.LIGHT_PURPLE;
			case "&e":
				return ChatColor.YELLOW;
			case "&f":
				return ChatColor.WHITE;
			default:
				return ChatColor.WHITE;

		}
	}

}
