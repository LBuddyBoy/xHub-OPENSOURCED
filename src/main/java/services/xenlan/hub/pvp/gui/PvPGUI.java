package services.xenlan.hub.pvp.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import services.xenlan.hub.utils.Serializer;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;
import services.xenlan.hub.utils.ItemBuilder;

import java.util.List;

public class PvPGUI {
    public static void open(Player player) {
        String title = xHub.getInstance().getPvpYML().getConfig().getString("GUI.Settings.Title");
        int size = xHub.getInstance().getPvpYML().getConfig().getInt("GUI.Settings.Size");
        Inventory inv = Bukkit.createInventory(null, size, CC.chat(title));
        if (xHub.getInstance().getPvpYML().getConfig().getBoolean("GUI.Auto-Fill.Enabled")) {
            for (int i = 0; i < inv.getSize(); i++) {
                int data = xHub.getInstance().getPvpYML().getConfig().getInt("GUI.Auto-Fill.Data");
                String material = xHub.getInstance().getPvpYML().getConfig().getString("GUI.Auto-Fill.Material");
                inv.setItem(i, (new ItemBuilder(Material.valueOf(material), 1, (byte) data)).displayName(null).build());
            }
        }
        for (String selection : xHub.getInstance().getPvpYML().getConfig().getConfigurationSection("GUI.Items").getKeys(false)) {
            String display = xHub.getInstance().getPvpYML().getConfig().getString("GUI.Items." + selection + ".Name");
            String color = xHub.getInstance().getPvpYML().getConfig().getString("GUI.Items." + selection + ".Mat-Color");
            int slot = xHub.getInstance().getPvpYML().getConfig().getInt("GUI.Items." + selection + ".Slot");
            Material material = Material.valueOf(xHub.getInstance().getPvpYML().getConfig().getString("GUI.Items." + selection + ".Material"));
            List<String> lore = xHub.getInstance().getPvpYML().getConfig().getStringList("GUI.Items." + selection + ".Lore");

            if (xHub.getInstance().isPlaceholderAPI()) {
                ItemStack stack = new ItemBuilder(material).setLeatherArmorColor(Serializer.getColor(color)).displayName(PlaceholderAPI.setPlaceholders(player, CC.chat(display)))
                        .setLore(PlaceholderAPI.setPlaceholders(player, CC.list(lore))).build();
                inv.setItem(slot - 1, stack);
            } else {
                ItemStack stack = new ItemBuilder(material).setLeatherArmorColor(Serializer.getColor(color)).displayName(CC.chat(display))
                        .setLore(CC.list(lore)).build();
                inv.setItem(slot - 1, stack);
            }

        }
        player.openInventory(inv);
    }
}
