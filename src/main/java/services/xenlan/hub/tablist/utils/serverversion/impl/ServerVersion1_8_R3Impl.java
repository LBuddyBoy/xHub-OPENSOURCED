package services.xenlan.hub.tablist.utils.serverversion.impl;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import services.xenlan.hub.tablist.utils.serverversion.IServerVersion;

public class ServerVersion1_8_R3Impl implements IServerVersion {

    @Override
    public void clearArrowsFromPlayer(Player player) {

        ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);
    }

    @Override
    public String getPlayerLanguage(Player player) {
        return ((CraftPlayer) player).getHandle().locale.substring(0, 2);
    }
}
