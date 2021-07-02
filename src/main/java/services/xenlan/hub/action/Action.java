package services.xenlan.hub.action;

import lombok.Getter;

public class Action {

	@Getter private String name;
	@Getter private ActionTypes type;


	public Action(String name, ActionTypes type) {
		this.name = name;
		this.type = type;

	}
}
