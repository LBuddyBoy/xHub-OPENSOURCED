package services.xenlan.hub.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import services.xenlan.hub.xHub;

import java.util.List;

public class PapiUtil {

	public static String addPlaceholders(String string, Player p) {

		if (xHub.getInstance().isPlaceholderAPI()) {
			return PlaceholderAPI.setPlaceholders(p, string);
		}
		return string;
	}
	public static List<String> addPlaceholders(List<String> string, Player p) {

		if (xHub.getInstance().isPlaceholderAPI()) {
			return PlaceholderAPI.setPlaceholders(p, string);
		}
		return string;
	}
}
