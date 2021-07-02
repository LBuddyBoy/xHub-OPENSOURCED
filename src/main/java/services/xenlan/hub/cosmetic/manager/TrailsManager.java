package services.xenlan.hub.cosmetic.manager;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.Effect;
import services.xenlan.hub.cosmetic.Trails;
import services.xenlan.hub.xHub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class TrailsManager {
	@Getter
	public HashSet<Trails> trails;
	@Getter public HashSet<UUID> activeTrail;
	@Getter public HashMap<UUID, String> cosm;

	public TrailsManager() {
		trails = Sets.newHashSet();
		activeTrail = Sets.newHashSet();
		cosm = new HashMap<>();

		init();
	}


	public void init() {

		for (String sec : xHub.getInstance().getTrailsYML().getConfig().getConfigurationSection("trails").getKeys(false)) {
			String eff = xHub.getInstance().getTrailsYML().getConfig().getString("trails." + sec + ".Effect");
			trails.add(new Trails(sec, Effect.getByName(eff)));
		}

	}
}
