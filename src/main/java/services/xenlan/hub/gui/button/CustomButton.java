package services.xenlan.hub.gui.button;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import services.xenlan.hub.bungee.BungeeListener;
import services.xenlan.hub.gui.CustomManager;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.ItemBuilder;
import services.xenlan.hub.utils.Serializer;
import services.xenlan.hub.utils.menu.Button;
import services.xenlan.hub.xHub;

import java.util.ArrayList;
import java.util.List;

public class CustomButton extends Button {


	@Getter
	private ItemStack itemStack;
	@Getter
	private int slot;
	@Getter
	private final String sectionName;
	@Getter
	private final String guiName;

	public CustomButton(String sectionName, ItemStack itemStack, int slot, String guiName) {
		this.sectionName = sectionName;
		this.itemStack = itemStack;
		this.slot = slot;

		this.guiName = guiName;
	}

	@Override
	public ItemStack getItem(Player player) {
		return itemStack;
	}

	@Override
	public void click(Player player, int slot, ClickType clickType, int hotbarButton) {
		doAction(player);
	}

	public void doAction(Player player) {
		switch (new CustomManager().getActionByName(sectionName).getType()) {
			case OPEN:
				for (String selection : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus").getKeys(false)) {
					for (String se : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus." + selection + ".Items").getKeys(false)) {

						if (se.equalsIgnoreCase(sectionName)) {
							String type = xHub.getInstance().getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".GUI-Name");
							player.closeInventory();
							new CustomManager().getGUIByName(type).openMenu(player);
						}
					}
				}
				break;
			case QUEUE:
				for (String selection : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus").getKeys(false)) {
					for (String se : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus." + selection + ".Items").getKeys(false)) {
						if (se.equalsIgnoreCase(sectionName)) {
							String server = xHub.getInstance().getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".Queue-Name");
							player.closeInventory();
							xHub.getInstance().getQueue().sendPlayer(player, server);
						}
					}
				}
				break;
			case RESET:
				for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("DeActivated-Cosmetic")) {
					player.sendMessage(CC.chat(list));
				}
				player.getInventory().setHelmet(null);
				player.getInventory().setChestplate(null);
				player.getInventory().setLeggings(null);
				player.getInventory().setBoots(null);
				xHub.getInstance().getTrailsManager().getCosm().remove(player.getUniqueId());
				xHub.getInstance().getGearManager().getActiveGear().remove(player.getUniqueId());
				xHub.getInstance().getTrailsManager().getActiveTrail().remove(player.getUniqueId());
				xHub.getInstance().getHatManager().getActiveHat().remove(player.getUniqueId());
				break;
			case HAT:
				for (String selection : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus").getKeys(false)) {
					for (String se : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus." + selection + ".Items").getKeys(false)) {
						if (se.equalsIgnoreCase(sectionName)) {
							String hat = xHub.getInstance().getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".Hat-Name");
							if (!player.hasPermission("xhub.hats." + hat)) {
								player.closeInventory();
								player.sendMessage(CC.chat(xHub.getInstance().getMessagesYML().getConfig().getString("No-Perm")));
								return;
							}
							if (xHub.getInstance().getHatManager().getActiveHat().contains(player.getUniqueId())) {
								for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Already-Active")) {
									player.sendMessage(CC.chat(list));
								}
								return;
							}
							player.getInventory().setHelmet(null);
							String material = xHub.getInstance().getHatsYML().getConfig().getString("hats." + hat + ".Material");
							String displayname = xHub.getInstance().getHatsYML().getConfig().getString("hats." + hat + ".Name");
							String color = xHub.getInstance().getHatsYML().getConfig().getString("hats." + hat + ".Color");
							int hatdata = xHub.getInstance().getHatsYML().getConfig().getInt("hats." + hat + ".Data");
							List<String> hatlore = xHub.getInstance().getHatsYML().getConfig().getStringList("hats." + hat + ".Lore");
							ItemStack stack2 = new ItemBuilder(Material.valueOf(material)).displayName(CC.chat(displayname)).data((short) hatdata)
									.setLeatherArmorColor(Serializer.getColor(color)).setLore(CC.list(hatlore)).build();
							for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("Activated-Cosmetic")) {
								player.sendMessage(CC.chat(message.replaceAll("%cosmetic%", displayname)));
							}
							player.getInventory().setHelmet(stack2);
							xHub.getInstance().getHatManager().getActiveHat().add(player.getUniqueId());
						}
					}
				}
				break;
			case GEAR:
				for (String selection : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus").getKeys(false)) {
					for (String se : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus." + selection + ".Items").getKeys(false)) {
						if (se.equalsIgnoreCase(sectionName)) {
							String gear = xHub.getInstance().getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".Gear-Name");
							if (!player.hasPermission("xhub.gear." + selection)) {
								player.closeInventory();
								player.sendMessage(CC.chat(xHub.getInstance().getMessagesYML().getConfig().getString("No-Perm")));
								return;
							}
							if (xHub.getInstance().getGearManager().getActiveGear().contains(player.getUniqueId())) {
								for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Already-Active")) {
									player.sendMessage(CC.chat(list));
								}
								return;
							}
							List<ItemStack> he = new ArrayList<>();
							List<ItemStack> ch = new ArrayList<>();
							List<ItemStack> le = new ArrayList<>();
							List<ItemStack> bo = new ArrayList<>();
							for (String serial : xHub.getInstance().getGearYML().getConfig().getConfigurationSection("gear." + gear + ".Helmet").getKeys(false)) {
								he.add(Serializer.deserializeItemStack(xHub.getInstance().getGearYML().getConfig().getString("gear." + gear + ".Helmet." + serial)));
								player.getInventory().setHelmet(he.get(he.size() - 1));
							}
							for (String serial : xHub.getInstance().getGearYML().getConfig().getConfigurationSection("gear." + gear + ".Chestplate").getKeys(false)) {
								ch.add(Serializer.deserializeItemStack(xHub.getInstance().getGearYML().getConfig().getString("gear." + gear + ".Chestplate." + serial)));
								player.getInventory().setChestplate(ch.get(ch.size() - 1));
							}
							for (String serial : xHub.getInstance().getGearYML().getConfig().getConfigurationSection("gear." + gear + ".Leggings").getKeys(false)) {
								le.add(Serializer.deserializeItemStack(xHub.getInstance().getGearYML().getConfig().getString("gear." + gear + ".Leggings." + serial)));
								player.getInventory().setLeggings(le.get(le.size() - 1));
							}
							for (String serial : xHub.getInstance().getGearYML().getConfig().getConfigurationSection("gear." + gear + ".Boots").getKeys(false)) {
								bo.add(Serializer.deserializeItemStack(xHub.getInstance().getGearYML().getConfig().getString("gear." + gear + ".Boots." + serial)));
								player.getInventory().setBoots(bo.get(bo.size() - 1));
							}
							xHub.getInstance().getGearManager().getActiveGear().add(player.getUniqueId());
						}
					}
				}
				break;
			case TRAILS:
				for (String selection : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus").getKeys(false)) {
					for (String se : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus." + selection + ".Items").getKeys(false)) {
						if (se.equalsIgnoreCase(sectionName)) {
							String hat = xHub.getInstance().getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".Trails-Name");
							if (!player.hasPermission("xhub.trails." + hat)) {
								player.closeInventory();
								player.sendMessage(CC.chat(xHub.getInstance().getMessagesYML().getConfig().getString("No-Perm")));
								return;
							}
							if (xHub.getInstance().getTrailsManager().getActiveTrail().contains(player.getUniqueId())) {
								for (String list : xHub.getInstance().getMessagesYML().getConfig().getStringList("Already-Active")) {
									player.sendMessage(CC.chat(list));
								}
								return;
							}
							String eff = xHub.getInstance().getTrailsYML().getConfig().getString("trails." + hat + ".Effect");

							for (String message : xHub.getInstance().getMessagesYML().getConfig().getStringList("Activated-Cosmetic")) {
								player.sendMessage(CC.chat(message.replaceAll("%cosmetic%", hat)));
							}
							xHub.getInstance().getTrailsManager().getActiveTrail().add(player.getUniqueId());
							xHub.getInstance().getTrailsManager().getCosm().put(player.getUniqueId(), eff);
						}
					}
				}
				break;
			case HUB:
				for (String selection : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus").getKeys(false)) {
					for (String se : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus." + selection + ".Items").getKeys(false)) {
						if (se.equalsIgnoreCase(sectionName)) {
							String server = xHub.getInstance().getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".Hub-Name");
							if (!player.hasPermission("xhub.hubselector." + server)) {
								player.closeInventory();
								player.sendMessage(CC.chat(xHub.getInstance().getMessagesYML().getConfig().getString("No-Perm")));
								return;
							}
							if (xHub.getInstance().getConfig().getBoolean("BungeeCord")) {
								BungeeListener.sendPlayerToServer(player, server);
							} else {
								Bukkit.getConsoleSender().sendMessage(CC.chat("&cWe tried to send " + player.getName() + " to a hub when using the hub selector, but couldn't because xHub doesn't have BungeeCord enabled in the config.yml."));
							}
						}
					}
				}
				break;
			case MESSAGE:
				for (String selection : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus").getKeys(false)) {
					for (String se : xHub.getInstance().getMenusYML().getConfig().getConfigurationSection("menus." + selection + ".Items").getKeys(false)) {
						if (se.equalsIgnoreCase(sectionName)) {
							List<String> msg = xHub.getInstance().getMenusYML().getConfig().getStringList("menus." + selection + ".Items." + se + ".Message");
							for (String m : msg) {
								player.sendMessage(CC.chat(m));
							}
						}
					}
				}
				break;
			default:
				break;
		}
	}
}
