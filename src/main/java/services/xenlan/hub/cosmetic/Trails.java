package services.xenlan.hub.cosmetic;

import lombok.Getter;
import org.bukkit.Effect;

public class Trails {

	@Getter
	private String name;
	@Getter private Effect effect;

	public Trails(String name, Effect effect) {
		this.name = name;
		this.effect = effect;
	}
	
}
