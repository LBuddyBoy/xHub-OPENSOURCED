package services.xenlan.hub.utils.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import services.xenlan.hub.xHub;

import java.util.Map;

public class MenuListener implements Listener {

    private xHub instance;

    public MenuListener(xHub instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Menu menu = Menu.getOpenedMenus().get(player);

        if (menu == null) {
            return;
        }

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getClickedInventory().getType() != InventoryType.CHEST) {
            return;
        }

        event.setCancelled(true);
        int slot = event.getSlot();
        Map<Integer, Button> buttons = menu.getButtons(player);

        if (buttons.containsKey(slot)) {
            Button button = buttons.get(slot);
            button.click(player, slot, event.getClick(), event.getHotbarButton());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Menu menu = Menu.getOpenedMenus().get(player);

        if (menu == null) {
            return;
        }

        menu.onClose(player);
        Menu.getOpenedMenus().remove(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Menu menu = Menu.getOpenedMenus().get(player);

        if (menu == null) {
            return;
        }

        menu.onClose(player);
        Menu.getOpenedMenus().remove(player);
    }

}
