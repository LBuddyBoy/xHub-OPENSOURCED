package services.xenlan.hub.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class ItemUtils {

	public static ItemStack make(Material m, int amount, short data, String name, List<String> lore, boolean leather, Color color, boolean enchanted, List<Enchantment> enchantment, int level) {
		if (enchanted) {
			ItemStack stack;
			stack = new ItemBuilder(m).setLore(CC.list(lore)).data(data).displayName(CC.chat(name)).build();
			if (leather) {
				stack = new ItemBuilder(m).setLore(CC.list(lore)).data(data).displayName(CC.chat(name)).setLeatherArmorColor(color).build();
			}
			for (Enchantment ench : enchantment) {
				stack.addUnsafeEnchantment(ench, level);
			}
			stack.setAmount(amount);
			return stack;
		} else {
			ItemStack stack;
			stack = new ItemBuilder(m).setLore(CC.list(lore)).data(data).displayName(CC.chat(name)).build();
			if (leather) {
				stack = new ItemBuilder(m).setLore(CC.list(lore)).data(data).displayName(CC.chat(name)).setLeatherArmorColor(color).build();
			}
			stack.setAmount(amount);
			return stack;
		}
	}

	public static void tryFit(Player p, ItemStack item) {
		PlayerInventory inv = p.getInventory();
		boolean canfit = false;
		for (int i = 0; i < inv.getSize(); ++i) {
			if (inv.getItem(i) == null || inv.getItem(i) != null && inv.getItem(i).getType() == Material.AIR) {
				canfit = true;
				inv.setItem(i, item);
				break;
			}
		}
		if (!canfit) {
			p.getWorld().dropItemNaturally(p.getLocation(), item);
		}
	}

}
