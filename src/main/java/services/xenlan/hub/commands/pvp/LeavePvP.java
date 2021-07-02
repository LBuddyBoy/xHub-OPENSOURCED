package services.xenlan.hub.commands.pvp;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;

public class LeavePvP implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if (!(sender instanceof Player)) {
            return true;
        }
        if (xHub.getInstance().getPvPManager().getCooldown().onCooldown(player)) {
            for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Is-Combat-Tagged")) {
                player.sendMessage(CC.chat(list));
            }
            return true;
        }
        if (!player.hasMetadata("pvpmode")) {
            return false;
        }
        xHub.getInstance().getPvPManager().disengagePvPMode(player, true);
        teleportToSpawn(player);
        return false;
    }

    public void teleportToSpawn(Player player) {
        Location location = player.getLocation();
        float yaw = xHub.getInstance().getSettingsYML().getConfig().getInt("Spawn.Yaw");
        float pitch = xHub.getInstance().getSettingsYML().getConfig().getInt("Spawn.Pitch");
        double x = xHub.getInstance().getSettingsYML().getConfig().getDouble("Spawn.X");
        double y = xHub.getInstance().getSettingsYML().getConfig().getDouble("Spawn.Y");
        double z = xHub.getInstance().getSettingsYML().getConfig().getDouble("Spawn.Z");

        location.setYaw(yaw);
        location.setPitch(pitch);
        location.setY(y);
        location.setX(x);
        location.setZ(z);

        player.teleport(location);

    }

}
