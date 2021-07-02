package services.xenlan.hub.commands.pvp;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import services.xenlan.hub.xHub;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.ItemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PvPCommand implements CommandExecutor {


    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("xhub.command.pvp.admin")) {
            sender.sendMessage(CC.chat(xHub.getInstance().getMessagesYML().getConfig().getString("No-Perm")));
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }
        if (args.length < 1) {
            for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("PvP-Help-Message")) {
                sender.sendMessage(CC.chat(list));
            }
            return true;
        }
        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("test")) {
            Inventory inv = Bukkit.createInventory(null, 27, "Test");
            List<String> lore = Arrays.asList("Lore1", "Lore2");
            List<Enchantment> enchantments = Arrays.asList(Enchantment.DURABILITY, Enchantment.DAMAGE_ALL);
            inv.setItem(1, ItemUtils.make(Material.COAL, 64, (short) 0, "Test", lore, false, null, true, enchantments, 2));
            player.openInventory(inv);
            return true;
        }

        if (args[0].equalsIgnoreCase("test2")) {
            List<String> lore = Arrays.asList("Lore1", "Lore2");
            List<Enchantment> enchantments = Arrays.asList(Enchantment.DURABILITY, Enchantment.DAMAGE_ALL);
            ItemStack stack = ItemUtils.make(Material.COAL, 64, (short) 0, "Test", lore, false, null, true, enchantments, 2);
            ItemUtils.tryFit(player, stack);
            return true;
        }

        if (args[0].equalsIgnoreCase("test3")) {
            List<String> lore = Arrays.asList("Lore1", "Lore2");
            List<Enchantment> enchantments = Arrays.asList(Enchantment.DURABILITY, Enchantment.DAMAGE_ALL);
            ItemStack stack = ItemUtils.make(Material.COAL, 64, (short) 0, "Test", lore, false, null, true, enchantments, 2);
            for (int i = 0; i < player.getInventory().getSize(); ++i) {
                player.getInventory().setItem(i, stack);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("setspawn")) {
            sender.sendMessage(CC.chat("&aSet the spawn!"));
            xHub.getInstance().getPvpYML().getConfig().set("Spawn.X", player.getLocation().getX());
            xHub.getInstance().getPvpYML().getConfig().set("Spawn.Y", player.getLocation().getY());
            xHub.getInstance().getPvpYML().getConfig().set("Spawn.Z", player.getLocation().getZ());
            xHub.getInstance().getPvpYML().getConfig().set("Spawn.Yaw", player.getLocation().getYaw());
            xHub.getInstance().getPvpYML().getConfig().set("Spawn.Pitch", player.getLocation().getPitch());
            xHub.getInstance().getPvpYML().save();
            return true;
        }
        return false;
    }
}
