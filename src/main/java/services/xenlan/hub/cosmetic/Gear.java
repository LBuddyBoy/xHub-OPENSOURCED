package services.xenlan.hub.cosmetic;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Gear {

	@Getter
	private String name;
	@Getter private List<ItemStack> stack;

	public Gear(String name, List<ItemStack> stack) {
		this.name = name;
		this.stack = stack;
	}

}
