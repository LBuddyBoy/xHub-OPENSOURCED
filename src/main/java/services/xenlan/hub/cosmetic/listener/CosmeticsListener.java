package services.xenlan.hub.cosmetic.listener;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import services.xenlan.hub.xHub;

public class CosmeticsListener implements Listener {



    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (String section : xHub.getInstance().getTrailsYML().getConfig().getConfigurationSection("trails").getKeys(false)) {
            if (xHub.getInstance().getTrailsManager().getCosm().isEmpty())
                return;
            if (xHub.getInstance().getTrailsManager().getCosm().containsKey(player.getUniqueId())) {
                if (xHub.getInstance().getTrailsManager().getCosm().get(player.getUniqueId()).
                        equalsIgnoreCase(xHub.getInstance().getTrailsYML().getConfig().getString("trails." + section + ".Effect"))) {
                    player.getWorld().playEffect(player.getLocation().subtract(0, 0, 0),
                            Effect.valueOf(xHub.getInstance().getTrailsYML().getConfig().getString("trails." +section + ".Effect")), 0);
                }
            }
        }

    }

}
