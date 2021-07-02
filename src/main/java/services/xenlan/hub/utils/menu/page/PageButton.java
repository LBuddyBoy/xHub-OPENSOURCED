package services.xenlan.hub.utils.menu.page;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.ItemBuilder;
import services.xenlan.hub.utils.menu.Button;
import services.xenlan.hub.xHub;

public class PageButton extends Button {

    private int mod;
    private PagedMenu menu;

    public PageButton(int mod, PagedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }


    @Override
    public ItemStack getItem(Player player) {
        if (this.hasNext(player)) {
            return new ItemBuilder(Material.ARROW)
                    .displayName(mod > 0 ? "Next Page" : "Previous Page")
                    .build();
        } else {
            return new ItemBuilder(Material.ARROW)
                    .displayName(mod > 0 ? CC.chat(xHub.getInstance().getSettingsYML().getConfig().getString("Last-Page.Name")) :
                            CC.chat(xHub.getInstance().getSettingsYML().getConfig().getString("First-Page.Name")))
                    .setLore((mod > 0 ? CC.list(xHub.getInstance().getSettingsYML().getConfig().getStringList("Last-Page.Lore")) :
                            CC.list(xHub.getInstance().getSettingsYML().getConfig().getStringList("First-Page.Lore"))))

                    .data((short) (mod > 0 ? xHub.getInstance().getSettingsYML().getConfig().getInt("Last-Page.Data") :
                            xHub.getInstance().getSettingsYML().getConfig().getInt("First-Page.Data")
                            ))
                    .build();
        }
    }

    @Override
    public void click(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (hasNext(player)) {
            this.menu.modPage(player, mod);
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }

}
