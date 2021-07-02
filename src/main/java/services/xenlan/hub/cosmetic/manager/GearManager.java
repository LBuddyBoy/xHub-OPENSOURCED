package services.xenlan.hub.cosmetic.manager;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import services.xenlan.hub.cosmetic.Gear;
import services.xenlan.hub.utils.Serializer;
import services.xenlan.hub.xHub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class GearManager {
	@Getter
	public HashSet<Gear> gears;
	@Getter
	public HashSet<UUID> activeGear;

	public GearManager() {
		gears = Sets.newHashSet();
		activeGear = Sets.newHashSet();

		init();
	}


	public void init() {

		List<ItemStack> stacks = new ArrayList<>();
		for (String selection : xHub.getInstance().getGearYML().getConfig().getConfigurationSection("gear").getKeys(false)) {

			for (String serial : xHub.getInstance().getGearYML().getConfig().getConfigurationSection("gear." + selection + ".Helmet").getKeys(false)) {
				stacks.add(Serializer.deserializeItemStack(xHub.getInstance().getGearYML().getConfig().getString("gear." + selection + ".Helmet." + serial)));
			}
			for (String serial : xHub.getInstance().getGearYML().getConfig().getConfigurationSection("gear." + selection + ".Chestplate").getKeys(false)) {
				stacks.add(Serializer.deserializeItemStack(xHub.getInstance().getGearYML().getConfig().getString("gear." + selection + ".Chestplate." + serial)));
			}
			for (String serial : xHub.getInstance().getGearYML().getConfig().getConfigurationSection("gear." + selection + ".Leggings").getKeys(false)) {
				stacks.add(Serializer.deserializeItemStack(xHub.getInstance().getGearYML().getConfig().getString("gear." + selection + ".Leggings." + serial)));
			}
			for (String serial : xHub.getInstance().getGearYML().getConfig().getConfigurationSection("gear." + selection + ".Boots").getKeys(false)) {
				stacks.add(Serializer.deserializeItemStack(xHub.getInstance().getGearYML().getConfig().getString("gear." + selection + ".Boots." + serial)));
			}
			gears.add(new Gear(selection, stacks));
		}
		//new ItemBuilder(Material.valueOf(mat)).displayName(CC.chat(dis)).data(data).setLeatherArmorColor(Serializer.getColor(color)).setLore(CC.list(lore)).build()

	}

}
