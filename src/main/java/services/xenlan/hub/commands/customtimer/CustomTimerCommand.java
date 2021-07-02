package services.xenlan.hub.commands.customtimer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import services.xenlan.hub.customtimer.CustomTimer;
import services.xenlan.hub.utils.JavaUtils;
import services.xenlan.hub.xHub;
import services.xenlan.hub.utils.CC;

import java.util.ArrayList;
import java.util.List;

public class CustomTimerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (xHub.getInstance().getConfig().getString("License").equalsIgnoreCase("")) {
            Bukkit.broadcastMessage("I think this is cracked?");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("Players Only");
            return false;
        }


        if (!sender.hasPermission("xhub.command.customtimer")) {
            sender.sendMessage(CC.chat(xHub.getInstance().getMessagesYML().getConfig().getString("No-Perm")));
            return true;
        }
        if (args.length < 1) {
            for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CustomTimer-Messages.Help-Message")) {
                sender.sendMessage(CC.chat(message));
            }
            return true;
        }

        switch (args[0]) {
            case "list": {
                List<String> names = new ArrayList<>();
                xHub.getInstance().getCustomTimerManager().getCustomtimer().forEach(timer -> names.add(timer.getName()));
                if (names.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < names.size(); i++) {
                        sb.append(names.get(i)).append(", ");
                    }
                    sender.sendMessage(CC.chat("&b&lActive CustomTimers:"));
                    sender.sendMessage(CC.chat("&f" + sb.toString()));
                    return true;
                }
                for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CustomTimer-Messages.No-Timers-Active")) {
                    sender.sendMessage(CC.chat(message));
                }
                break;
            }

            case "stop": {
                if (args.length < 2) {
                    for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CustomTimer-Messages.Help-Message")) {
                        sender.sendMessage(CC.chat(message));
                    }
                    return true;
                }
                CustomTimer timer = xHub.getInstance().getCustomTimerManager().getCustomTimer(args[1]);
                if (timer == null) {
                    for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CustomTimer-Messages.Invalid-Timer")) {
                        sender.sendMessage(CC.chat(message));
                    }
                    return true;
                }
                for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CustomTimer-Messages.Stopped-Customtimer")) {
                    sender.sendMessage(CC.chat(message));
                }
                timer.cancel();
                break;
            }

            case "start": {
                if (args.length < 4) {
                    for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CustomTimer-Messages.Help-Message")) {
                        sender.sendMessage(CC.chat(message));
                    }
                    return true;
                }

                long duration = JavaUtils.parse(args[2]);

                if (duration == -1L) {
                    sender.sendMessage(CC.chat("&c" + args[1] + " is an invalid duration."));
                    return true;
                }

                if (duration < 1000L) {
                    sender.sendMessage(CC.chat("&cThe time must last for atleast 1 second."));
                    return true;
                }

                if (xHub.getInstance().getCustomTimerManager().getCustomTimer(args[1]) != null) {
                    for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CustomTimer-Messages.Timer-Already-Exists")) {
                        sender.sendMessage(CC.chat(message).replaceAll("%timer%", args[1]));
                    }
                    return true;
                }


                xHub.getInstance().getCustomTimerManager().createTimer(new CustomTimer(CC.chat(args[1]), CC.chat(args[3].replace("-", " ")), System.currentTimeMillis(), System.currentTimeMillis() + duration));
                for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CustomTimer-Messages.Created-Customtimer")) {
                    sender.sendMessage(CC.chat(message).replaceAll("%timer%", args[1]));
                }
                break;
            }
            default: {
                for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("CustomTimer-Messages.Help-Message")) {
                    sender.sendMessage(CC.chat(message));
                }
                break;
            }
        }
        return false;
    }
}
