package services.xenlan.hub.tablist.utils.playerversion.impl;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;
import services.xenlan.hub.tablist.utils.playerversion.IPlayerVersion;
import services.xenlan.hub.tablist.utils.playerversion.PlayerVersion;

public class PlayerVersionProtocolLibImpl implements IPlayerVersion {

    @Override
    public PlayerVersion getPlayerVersion(Player player) {
        return PlayerVersion.getVersionFromRaw(
                ProtocolLibrary.getProtocolManager().getProtocolVersion(player)
        );
    }
}
