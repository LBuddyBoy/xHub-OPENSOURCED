package services.xenlan.hub.gui.menu;


import lombok.Getter;
import org.bukkit.entity.Player;
import services.xenlan.hub.gui.button.CustomButton;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.menu.Button;
import services.xenlan.hub.utils.menu.Menu;
import services.xenlan.hub.xHub;

import java.util.HashMap;
import java.util.Map;

public class CustomGUI extends Menu {

	@Getter
	private String title;
	@Getter
	private String inv;
	@Getter
	private int size;
	@Getter private boolean usePholder;

	public CustomGUI() {

	}

	public CustomGUI(String inv, String title, int size, boolean usePholder) {
		this.title = title;
		this.inv = inv;
		this.size = size;
		this.usePholder = usePholder;
	}


	@Override
	public String getTitle(Player player) {
		return CC.chat(title);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttonMap = new HashMap<>();

		for (CustomButton customButton : xHub.getInstance().getCustomButton()) {
			if (customButton.getGuiName().equals(inv)) {
				buttonMap.put(customButton.getSlot(), customButton);
			}
		}
		return buttonMap;
	}

	@Override
	public boolean usePlaceholder() {
		return usePholder;
	}
}
