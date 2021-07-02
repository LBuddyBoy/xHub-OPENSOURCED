package services.xenlan.hub.cosmetic.manager;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.Material;
import services.xenlan.hub.cosmetic.Hat;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.ItemBuilder;
import services.xenlan.hub.utils.Serializer;
import services.xenlan.hub.xHub;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class HatManager {

	@Getter public HashSet<Hat> hats;
	@Getter public HashSet<UUID> activeHat;

	public HatManager() {
		hats = Sets.newHashSet();
		activeHat = Sets.newHashSet();

		init();
	}


	public void init() {

		for (String sec : xHub.getInstance().getHatsYML().getConfig().getConfigurationSection("hats").getKeys(false)) {
			String mat = xHub.getInstance().getHatsYML().getConfig().getString("hats." + sec + ".Material");
			String dis = xHub.getInstance().getHatsYML().getConfig().getString("hats." + sec + ".Name");
			String color = xHub.getInstance().getHatsYML().getConfig().getString("hats." + sec + ".Color");
			int data = xHub.getInstance().getHatsYML().getConfig().getInt("hats." + sec + ".Data");
			List<String> lore = xHub.getInstance().getHatsYML().getConfig().getStringList("hats." + sec + ".Lore");
			hats.add(new Hat(sec, new ItemBuilder(Material.valueOf(mat)).displayName(CC.chat(dis)).data(data).setLeatherArmorColor(Serializer.getColor(color)).setLore(CC.list(lore)).build()));
		}

	}

}
