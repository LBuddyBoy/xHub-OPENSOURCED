package services.xenlan.hub.tablist.utils.serverversion;

import org.bukkit.entity.Player;

public interface IServerVersion {

    void clearArrowsFromPlayer(Player player);

    String getPlayerLanguage(Player player);
}
