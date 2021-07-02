package services.xenlan.hub.utils.menu.page;

import lombok.Getter;
import org.bukkit.entity.Player;
import services.xenlan.hub.utils.menu.Button;
import services.xenlan.hub.utils.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public abstract class PagedMenu extends Menu {

    @Getter
    private int page = 1;

    public abstract Map<Integer, Button> getAllPagesButtons(Player player);

    public abstract String getRawTitle(Player player);

    public final int getPages(Player player) {
        int buttonAmount = getAllPagesButtons(player).size();

        if (buttonAmount == 0) {
            return 1;
        }

        return (int) Math.ceil(buttonAmount / (double) getMaxItemsPerPage(player));
    }

    public final void modPage(Player player, int mod) {
        page += mod;
        getButtons(player).clear();
        openMenu(player);
    }

    @Override
    public final Map<Integer, Button> getButtons(Player player) {
        int minIndex = (int) ((double) (page - 1) * getMaxItemsPerPage(player));
        int maxIndex = (int) ((double) (page) * getMaxItemsPerPage(player));

        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));

        for (Map.Entry<Integer, Button> entry : getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();

            if (ind >= minIndex && ind < maxIndex) {
                ind -= (int) ((double) (getMaxItemsPerPage(player)) * (page - 1)) - 9;
                buttons.put(ind, entry.getValue());
            }
        }

        Map<Integer, Button> global = getGlobalButtons(player);

        if (global != null) {
            for (Map.Entry<Integer, Button> gent : global.entrySet()) {
                buttons.put(gent.getKey(), gent.getValue());
            }
        }

        return buttons;
    }

    public int getMaxItemsPerPage(Player player) {
        return 45;
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    @Override
    public String getTitle(Player player) {
        return "(" + page + "/" + getPages(player) + ") " + getRawTitle(player);
    }
}
