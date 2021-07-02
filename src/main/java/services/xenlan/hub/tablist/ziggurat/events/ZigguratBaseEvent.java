package services.xenlan.hub.tablist.ziggurat.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ZigguratBaseEvent extends Event implements Cancellable {

    @Getter
	public static HandlerList handlerList = new HandlerList();
    @Getter
	@Setter
	private boolean cancelled = false;

    public ZigguratBaseEvent() {

    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
