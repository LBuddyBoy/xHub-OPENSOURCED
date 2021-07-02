package services.xenlan.hub.utils.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

    public abstract ItemStack getItem(Player player);

    public void click(Player player, int slot, ClickType clickType, int hotbarButton) {

    }

}
