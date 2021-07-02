package services.xenlan.hub.tablist.utils.playerversion;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import services.xenlan.hub.tablist.utils.playerversion.impl.PlayerVersion1_7Impl;
import services.xenlan.hub.tablist.utils.playerversion.impl.PlayerVersionProtocolLibImpl;
import services.xenlan.hub.tablist.utils.serverversion.ServerVersionHandler;

public class PlayerVersionHandler {

    public static IPlayerVersion version;

    public PlayerVersionHandler() {
        /* Plugin Manager */
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        /* 1.7 Protocol */
        if (ServerVersionHandler.serverVersionName.contains("1_7")){
            version = new PlayerVersion1_7Impl();
            return;
        }

        /* ProtocolLib */
        if (pluginManager.getPlugin("ProtocolLib") != null) {
            version = new PlayerVersionProtocolLibImpl();
            return;
        }
    }
}
