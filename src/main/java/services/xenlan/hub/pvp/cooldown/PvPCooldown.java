package services.xenlan.hub.pvp.cooldown;

import org.bukkit.entity.Player;
import services.xenlan.hub.pvp.cooldown.util.CooldownFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PvPCooldown {

    private Map<UUID, Long> cooldowns = new HashMap<UUID, Long>();

    public void applyCooldown(Player player, long cooldown) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
    }
    public boolean onCooldown(Player player) {
        return cooldowns.containsKey(player.getUniqueId()) && (cooldowns.get(player.getUniqueId()) >= System.currentTimeMillis());
    }
    public void removeCooldown(Player player) {

        cooldowns.remove(player.getUniqueId());
    }
    public String getRemaining(Player player) {
        long l = this.cooldowns.get(player.getUniqueId()) - System.currentTimeMillis();
        return CooldownFormatter.getRemaining(l, true);

    }

}