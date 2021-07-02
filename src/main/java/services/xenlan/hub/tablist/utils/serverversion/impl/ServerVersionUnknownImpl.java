package services.xenlan.hub.tablist.utils.serverversion.impl;

import org.bukkit.entity.Player;
import services.xenlan.hub.tablist.utils.serverversion.IServerVersion;

public class ServerVersionUnknownImpl implements IServerVersion {

    @Override
    public void clearArrowsFromPlayer(Player player) {

    }

    @Override
    public String getPlayerLanguage(Player player) {
        return "en";
    }
}
