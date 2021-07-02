package services.xenlan.hub.bungee;

import org.bukkit.Bukkit;

public class BungeeUpdateTask implements Runnable {
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream().findFirst().ifPresent(BungeeListener::updateCount);
    }
}
