package services.xenlan.hub.gui;

import services.xenlan.hub.action.Action;
import services.xenlan.hub.gui.menu.CustomGUI;
import services.xenlan.hub.xHub;

public class CustomManager {

	public CustomManager() {

	}

	public CustomGUI getGUIByName(String name) {

		for (CustomGUI cg : xHub.getInstance().getCustomGUI()) {
			if (cg.getInv().equalsIgnoreCase(name)) {
				return cg;
			}
		}
		return null;
	}

	public Action getActionByName(String action) {

		for (Action a : xHub.getInstance().getActions()) {
			if (a.getName().equalsIgnoreCase(action)) {
				return a;
			}
		}
		return null;
	}

}
