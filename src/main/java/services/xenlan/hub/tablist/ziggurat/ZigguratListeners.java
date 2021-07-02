package services.xenlan.hub.tablist.ziggurat;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class ZigguratListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CraftPlayer player = (CraftPlayer) event.getPlayer();
        if (player.getHandle().playerConnection.networkManager.getVersion() < 47)
            return;
        Ziggurat.getInstance().getTablists().put(player.getUniqueId(), new ZigguratTablist(event.getPlayer()));
    }

    @EventHandler(
            priority = EventPriority.LOW
    )
    public void onQuit(PlayerQuitEvent event) {
        CraftPlayer player = (CraftPlayer) event.getPlayer();
        if (player.getHandle().playerConnection.networkManager.getVersion() < 47)
            return;
        Ziggurat.getInstance().getTablists().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Ziggurat.getInstance().disable();
    }
}
