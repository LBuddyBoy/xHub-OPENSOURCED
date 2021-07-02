package services.xenlan.hub.pvp.gui.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.xHub;

public class PvPGUIListener implements Listener {
	@EventHandler
	public void onInventoryCosmeticClick(InventoryClickEvent event) {
		if (!ChatColor.stripColor(event.getInventory().getTitle()).equalsIgnoreCase(ChatColor.stripColor(CC.chat(xHub.getInstance().getPvpYML().getConfig().getString("GUI.Settings.Title")))))
			return;
		event.setCancelled(true);
if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
			return;
		Player player = (Player) event.getWhoClicked();
		if (ChatColor.stripColor(event.getInventory().getTitle()).equalsIgnoreCase(ChatColor.stripColor(CC.chat(xHub.getInstance().getPvpYML().getConfig().getString("GUI.Settings.Title"))))) {
			if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
				return;
			if (event.getCurrentItem().getType() == Material.valueOf(xHub.getInstance().getPvpYML().getConfig().getString("GUI.Auto-Fill.Material"))) {
				event.setCancelled(true);
				return;
			}
			for (String sect2 : xHub.getInstance().getPvpYML().getConfig().getConfigurationSection("GUI.Items").getKeys(false)) {
				int slot = xHub.getInstance().getPvpYML().getConfig().getInt("GUI.Items." + sect2 + ".Slot");
				String kit = xHub.getInstance().getPvpYML().getConfig().getString("GUI.Items." + sect2 + ".Kit-Name");
				if (event.getSlot() == slot - 1) {
					xHub.getInstance().getKitManager().getKit().put(player, kit);
				}
			}
		}
		player.closeInventory();
		xHub.getInstance().getPvPManager().engagePvPMode(player);
		xHub.getInstance().getKitManager().giveKit(player);
//			if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equals(ChatColor.stripColor(chatUtil.chat(xHub.getInstance().getPvp().getConfig().getString("GUI.Items.Diamond.Name"))))) {
//				player.closeInventory();
//				xHub.getInstance().getPvPManager().engagePvPMode(player);
//				DiamondInventory.giveDiamondKit(player);
//				xHub.getInstance().getKitManager().getDiamond().add(player);
//			}
//			if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equals(ChatColor.stripColor(chatUtil.chat(xHub.getInstance().getPvp().getConfig().getString("GUI.Items.Axe.Name"))))) {
//				player.closeInventory();
//				xHub.getInstance().getPvPManager().engagePvPMode(player);
//				AxeInventory.giveAxeKit(player);
//				xHub.getInstance().getKitManager().getAxe().add(player);
//			}
//			if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equals(ChatColor.stripColor(chatUtil.chat(xHub.getInstance().getPvp().getConfig().getString("GUI.Items.Archer.Name"))))) {
//				player.closeInventory();
//				xHub.getInstance().getPvPManager().engagePvPMode(player);
//				ArcherInventory.giveArcherKit(player);
//				xHub.getInstance().getKitManager().getArcher().add(player);
//			}
	}
}
