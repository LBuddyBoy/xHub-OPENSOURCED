package services.xenlan.hub.tablist.provider;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import services.xenlan.hub.tablist.ziggurat.ZigguratAdapter;
import services.xenlan.hub.tablist.ziggurat.utils.BufferedTabObject;
import services.xenlan.hub.tablist.ziggurat.utils.TabColumn;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.api.RankAPI;
import services.xenlan.hub.xHub;

import java.util.HashSet;
import java.util.Set;

public class TablistProvider implements ZigguratAdapter {
	public String replaceAll(String text, Player player) {
		if (xHub.getInstance().isPlaceholderAPI()) {
			return PlaceholderAPI.setPlaceholders(player, CC.chat(text
					.replaceAll("%rank%", RankAPI.checkRank(player))
					.replaceAll("%online_total%", "" + Bukkit.getOnlinePlayers().size())
					.replaceAll("%player%", player.getName())
					.replaceAll("%kills%", "" + player.getStatistic(Statistic.PLAYER_KILLS))
					.replaceAll("%deaths%", "" + player.getStatistic(Statistic.DEATHS)))
			);
		}
		return CC.chat(text
				.replaceAll("%rank%", RankAPI.checkRank(player))
				.replaceAll("%online_total%", "" + Bukkit.getOnlinePlayers().size())
				.replaceAll("%player%", player.getName())
				.replaceAll("%kills%", "" + player.getStatistic(Statistic.PLAYER_KILLS))
				.replaceAll("%deaths%", "" + player.getStatistic(Statistic.DEATHS))
		);
	}

	@Override
	public Set<BufferedTabObject> getSlots(Player player) {
		YamlConfiguration conf = xHub.getInstance().getTabYML().getConfig();
		HashSet<BufferedTabObject> tabs = new HashSet<>();
		String left = "TAB.LEFT";
		String mid = "TAB.MIDDLE";
		String right = "TAB.RIGHT";
		tabs.add(new BufferedTabObject().slot(1).text(replaceAll(conf.getString(left + ".1"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(2).text(replaceAll(conf.getString(left + ".2"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(3).text(replaceAll(conf.getString(left + ".3"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(4).text(replaceAll(conf.getString(left + ".4"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(5).text(replaceAll(conf.getString(left + ".5"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(6).text(replaceAll(conf.getString(left + ".6"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(7).text(replaceAll(conf.getString(left + ".7"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(8).text(replaceAll(conf.getString(left + ".8"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(9).text(replaceAll(conf.getString(left + ".9"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(10).text(replaceAll(conf.getString(left + ".10"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(11).text(replaceAll(conf.getString(left + ".11"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(12).text(replaceAll(conf.getString(left + ".12"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(13).text(replaceAll(conf.getString(left + ".13"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(14).text(replaceAll(conf.getString(left + ".14"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(15).text(replaceAll(conf.getString(left + ".15"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(16).text(replaceAll(conf.getString(left + ".16"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(17).text(replaceAll(conf.getString(left + ".17"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(18).text(replaceAll(conf.getString(left + ".18"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(19).text(replaceAll(conf.getString(left + ".19"), player)).column(TabColumn.LEFT));
		tabs.add(new BufferedTabObject().slot(20).text(replaceAll(conf.getString(left + ".20"), player)).column(TabColumn.LEFT));
		// MID
		tabs.add(new BufferedTabObject().slot(1).text(replaceAll(conf.getString(mid + ".1"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(2).text(replaceAll(conf.getString(mid + ".2"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(3).text(replaceAll(conf.getString(mid + ".3"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(4).text(replaceAll(conf.getString(mid + ".4"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(5).text(replaceAll(conf.getString(mid + ".5"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(6).text(replaceAll(conf.getString(mid + ".6"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(7).text(replaceAll(conf.getString(mid + ".7"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(8).text(replaceAll(conf.getString(mid + ".8"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(9).text(replaceAll(conf.getString(mid + ".9"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(10).text(replaceAll(conf.getString(mid + ".10"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(11).text(replaceAll(conf.getString(mid + ".11"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(12).text(replaceAll(conf.getString(mid + ".12"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(13).text(replaceAll(conf.getString(mid + ".13"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(14).text(replaceAll(conf.getString(mid + ".14"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(15).text(replaceAll(conf.getString(mid + ".15"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(16).text(replaceAll(conf.getString(mid + ".16"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(17).text(replaceAll(conf.getString(mid + ".17"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(18).text(replaceAll(conf.getString(mid + ".18"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(19).text(replaceAll(conf.getString(mid + ".19"), player)).column(TabColumn.MIDDLE));
		tabs.add(new BufferedTabObject().slot(20).text(replaceAll(conf.getString(mid + ".20"), player)).column(TabColumn.MIDDLE));
		// RIGHT
		tabs.add(new BufferedTabObject().slot(1).text(replaceAll(conf.getString(right + ".1"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(2).text(replaceAll(conf.getString(right + ".2"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(3).text(replaceAll(conf.getString(right + ".3"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(4).text(replaceAll(conf.getString(right + ".4"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(5).text(replaceAll(conf.getString(right + ".5"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(6).text(replaceAll(conf.getString(right + ".6"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(7).text(replaceAll(conf.getString(right + ".7"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(8).text(replaceAll(conf.getString(right + ".8"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(9).text(replaceAll(conf.getString(right + ".9"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(10).text(replaceAll(conf.getString(right + ".10"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(11).text(replaceAll(conf.getString(right + ".11"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(12).text(replaceAll(conf.getString(right + ".12"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(13).text(replaceAll(conf.getString(right + ".13"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(14).text(replaceAll(conf.getString(right + ".14"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(15).text(replaceAll(conf.getString(right + ".15"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(16).text(replaceAll(conf.getString(right + ".16"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(17).text(replaceAll(conf.getString(right + ".17"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(18).text(replaceAll(conf.getString(right + ".18"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(19).text(replaceAll(conf.getString(right + ".19"), player)).column(TabColumn.RIGHT));
		tabs.add(new BufferedTabObject().slot(20).text(replaceAll(conf.getString(right + ".20"), player)).column(TabColumn.RIGHT));
//		tabs.add(new BufferedTabObject().slot(3).text(replaceAll("&eHello&6 %player%", player)).column(TabColumn.MIDDLE));
		return tabs;
	}

	private String getQueue(String queue) {
		return xHub.getInstance().getQueueYML().getConfig().getString("queues." + queue + ".bungee-name");
	}

	@Override
	public String getFooter() {
		return "Test";
	}

	@Override
	public String getHeader() {
		return "Test";
	}
}
