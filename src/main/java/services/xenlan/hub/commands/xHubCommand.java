package services.xenlan.hub.commands;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.interfaces.CallBack;
import services.xenlan.hub.xHub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class xHubCommand implements CommandExecutor, TabCompleter {
    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (!(new CallBack(xHub.getInstance().getConfig().getString("License"), "https://system.xenlan.services/verify.php", xHub.getInstance())).register()) {
                return true;
            }
        } catch (NoClassDefFoundError var2) {
            Bukkit.shutdown();
            Bukkit.getPluginManager().disablePlugin(xHub.getInstance());
        }
        if (!sender.hasPermission("xhub.command.xhub.admin")) {
            sender.sendMessage(CC.chat(xHub.getInstance().getMessagesYML().getConfig().getString("No-Perm")));
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length < 1) {
            for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Help-Message")) {
                sender.sendMessage(CC.chat(list));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("buildmode")) {
            Player player = (Player) sender;
            if (xHub.getInstance().getBuildmode().contains(player)) {
                xHub.getInstance().getBuildmode().remove(player);
                for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("Build-Mode.DeActivated")) {
                    player.sendMessage(CC.chat(message));
                }
                return false;
            }
            if (!xHub.getInstance().getBuildmode().contains(player)) {
                xHub.getInstance().getBuildmode().add(player);
                for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("Build-Mode.Activated")) {
                    player.sendMessage(CC.chat(message));
                }
                return false;
            }

        }
        if (args[0].equalsIgnoreCase("reload")) {
            xHub.getInstance().getCosmeticYML().reloadConfig();
            xHub.getInstance().getHatsYML().reloadConfig();
            xHub.getInstance().getInventoriesYML().reloadConfig();
            xHub.getInstance().getSettingsYML().reloadConfig();
            xHub.getInstance().getTrailsYML().reloadConfig();
            xHub.getInstance().getMessagesYML().reloadConfig();
            xHub.getInstance().getGearYML().reloadConfig();
            xHub.getInstance().getPvpYML().reloadConfig();
            xHub.getInstance().getQueueYML().reloadConfig();
            xHub.getInstance().getTabYML().reloadConfig();
            xHub.getInstance().getMenusYML().reloadConfig();
            xHub.getInstance().reloadConfig();
            xHub.getInstance().createMenus();
            xHub.getInstance().registerManagers();
            xHub.getInstance().setupQueue();

            sender.sendMessage(CC.chat("&aYou have successfully reloaded all of the configuration files!"));
            return true;
        }
        if (args[0].equalsIgnoreCase("setspawn")) {
            Player player = (Player) sender;
            sender.sendMessage(CC.chat("&aSet the spawn!"));
            xHub.getInstance().getSettingsYML().getConfig().set("Spawn.X", player.getLocation().getX());
            xHub.getInstance().getSettingsYML().getConfig().set("Spawn.Y", player.getLocation().getY());
            xHub.getInstance().getSettingsYML().getConfig().set("Spawn.Z", player.getLocation().getZ());
            xHub.getInstance().getSettingsYML().getConfig().set("Spawn.Yaw", player.getLocation().getYaw());
            xHub.getInstance().getSettingsYML().getConfig().set("Spawn.Pitch", player.getLocation().getPitch());
            xHub.getInstance().getSettingsYML().save();
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Help-Message")) {
                sender.sendMessage(CC.chat(list));
            }
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 1) {
            return Collections.emptyList();
        }
        List<String> option = new ArrayList<>();
        option.add("setspawn");
        option.add("reload");
        option.add("buildmode");
        for (String o : option) {
            if (args[0].startsWith(o)) {
                return Collections.singletonList(o);
            }
        }
        return option;
    }
}
