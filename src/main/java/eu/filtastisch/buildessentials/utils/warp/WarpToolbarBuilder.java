package eu.filtastisch.buildessentials.utils.warp;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.toolbar.SGToolbarBuilder;
import com.samjakob.spigui.toolbar.SGToolbarButtonType;
import org.bukkit.Material;
import org.bukkit.event.Event;

import static com.samjakob.spigui.toolbar.SGToolbarButtonType.PREV_BUTTON;

public class WarpToolbarBuilder implements SGToolbarBuilder {
    @Override
    public SGButton buildToolbarButton(int slot, int page, SGToolbarButtonType type, SGMenu menu) {
        switch (type) {
            case PREV_BUTTON:
                if (menu.getCurrentPage() > 0) return new SGButton(new ItemBuilder(Material.ARROW)
                        .name("&a&l← Vorherige Seite").build()
                ).withListener(event -> {
                    event.setResult(Event.Result.DENY);
                    menu.previousPage(event.getWhoClicked());
                });
                else return null;

            case CURRENT_BUTTON:
                return new SGButton(new ItemBuilder(Material.NAME_TAG)
                        .name("&7&lSeite " + (menu.getCurrentPage() + 1) + " von " + menu.getMaxPage())
                        .build()
                ).withListener(event -> event.setResult(Event.Result.DENY));

            case NEXT_BUTTON:
                if (menu.getCurrentPage() < menu.getMaxPage() - 1) return new SGButton(new ItemBuilder(Material.ARROW)
                        .name("&a&lNächste Seite →")
                        .build()
                ).withListener(event -> {
                    event.setResult(Event.Result.DENY);
                    menu.nextPage(event.getWhoClicked());
                });
                else return null;

            case UNASSIGNED:
            default:
                return null;
        }
    }
}
