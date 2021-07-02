package services.xenlan.hub.pvp.kits;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import services.xenlan.hub.utils.Serializer;
import services.xenlan.hub.xHub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitManager {
    private xHub xHub;
    
    @Getter public HashMap<Player, String> kit;

    public KitManager(xHub hub) {
        this.xHub = hub;
        kit = new HashMap<>();
    }

    public void giveKit(Player player) {
        for (String sect : xHub.getInventoriesYML().getConfig().getConfigurationSection("Kits").getKeys(false)) {
            if (xHub.getInventoriesYML().getConfig().getString("Kits." + sect + ".Kit-Name").equalsIgnoreCase(getKit().get(player))) {
                player.getInventory().clear();
                List<ItemStack> items = new ArrayList<>();
                List<ItemStack> he = new ArrayList<>();
                List<ItemStack> ch = new ArrayList<>();
                List<ItemStack> le = new ArrayList<>();
                List<ItemStack> bo = new ArrayList<>();
                for (String serial : xHub.getInventoriesYML().getConfig().getConfigurationSection("Kits." + sect + ".Contents").getKeys(false)) {
                    items.add(Serializer.deserializeItemStack(xHub.getInventoriesYML().getConfig().getString("Kits." + sect + ".Contents." + serial)));
                    player.getInventory().addItem(items.get(items.size() - 1));
                }
                for (String serial : xHub.getInventoriesYML().getConfig().getConfigurationSection("Kits." + sect + ".Armor.Helmet").getKeys(false)) {
                    he.add(Serializer.deserializeItemStack(xHub.getInventoriesYML().getConfig().getString("Kits." + sect + ".Armor.Helmet." + serial)));
                    player.getInventory().setHelmet(he.get(he.size() - 1));
                }
                for (String serial : xHub.getInventoriesYML().getConfig().getConfigurationSection("Kits." + sect + ".Armor.Chestplate").getKeys(false)) {
                    ch.add(Serializer.deserializeItemStack(xHub.getInventoriesYML().getConfig().getString("Kits." + sect + ".Armor.Chestplate." + serial)));
                    player.getInventory().setChestplate(ch.get(ch.size() - 1));
                }
                for (String serial : xHub.getInventoriesYML().getConfig().getConfigurationSection("Kits." + sect + ".Armor.Leggings").getKeys(false)) {
                    le.add(Serializer.deserializeItemStack(xHub.getInventoriesYML().getConfig().getString("Kits." + sect + ".Armor.Leggings." + serial)));
                    player.getInventory().setLeggings(le.get(le.size() - 1));
                }
                for (String serial : xHub.getInventoriesYML().getConfig().getConfigurationSection("Kits." + sect + ".Armor.Boots").getKeys(false)) {
                    bo.add(Serializer.deserializeItemStack(xHub.getInventoriesYML().getConfig().getString("Kits." + sect + ".Armor.Boots." + serial)));
                    player.getInventory().setBoots(bo.get(bo.size() - 1));
                }
                for (String section : xHub.getInventoriesYML().getConfig().getConfigurationSection("Kits." + sect + ".Effects").getKeys(false)) {
                    String name = xHub.getInventoriesYML().getConfig().getString("Kits." + sect + ".Effects." + section + ".Name");
                    int time = xHub.getInventoriesYML().getConfig().getInt("Kits." + sect + ".Effects." + section + ".Time") * 20;
                    int power = xHub.getInventoriesYML().getConfig().getInt("Kits." + sect + ".Effects." + section + ".Power");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(name), time, power - 1));
                }
            }
        }
    }
    
}
