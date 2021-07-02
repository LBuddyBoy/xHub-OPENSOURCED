package services.xenlan.hub.tablist.ziggurat;


import org.bukkit.entity.Player;
import services.xenlan.hub.tablist.ziggurat.utils.BufferedTabObject;

import java.util.Set;

public interface ZigguratAdapter {

    Set<BufferedTabObject> getSlots(Player player);

    String getFooter();

    String getHeader();

}
