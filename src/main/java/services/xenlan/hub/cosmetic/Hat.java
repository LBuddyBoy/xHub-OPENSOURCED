package services.xenlan.hub.cosmetic;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class Hat {

	@Getter private String name;
	@Getter private ItemStack stack;

	public Hat(String name, ItemStack stack) {
		this.name = name;
		this.stack = stack;
	}
}
