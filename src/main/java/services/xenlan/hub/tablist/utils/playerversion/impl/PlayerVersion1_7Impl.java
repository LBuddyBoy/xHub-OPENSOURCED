package services.xenlan.hub.tablist.utils.playerversion.impl;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import services.xenlan.hub.tablist.utils.playerversion.IPlayerVersion;
import services.xenlan.hub.tablist.utils.playerversion.PlayerVersion;

public class PlayerVersion1_7Impl implements IPlayerVersion {

    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(
                ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion()
        );
    }
}
