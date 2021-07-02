package services.xenlan.hub.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CC {

    public static String chat(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> list(List<String> lore) {
        ArrayList<String> toAdd = new ArrayList<>();
        for (String lor : lore) {
            toAdd.add(chat(lor));
        }
        return toAdd;
    }

    public static ChatColor getColor(String path) {
        switch (path) {
            case "&1":
                return ChatColor.DARK_BLUE;
            case "&2":
                return ChatColor.DARK_GREEN;
            case "&3":
                return ChatColor.DARK_AQUA;
            case "&4":
                return ChatColor.DARK_RED;
            case "&5":
                return ChatColor.DARK_PURPLE;
            case "&6":
                return ChatColor.GOLD;
            case "&7":
                return ChatColor.GRAY;
            case "&8":
                return ChatColor.DARK_GRAY;
            case "&9":
                return ChatColor.BLUE;
            case "&0":
                return ChatColor.BLACK;
            case "&a":
                return ChatColor.GREEN;
            case "&b":
                return ChatColor.AQUA;
            case "&c":
                return ChatColor.RED;
            case "&d":
                return ChatColor.LIGHT_PURPLE;
            case "&e":
                return ChatColor.YELLOW;
            case "&f":
                return ChatColor.WHITE;
            default:
                return ChatColor.WHITE;

        }
    }
}