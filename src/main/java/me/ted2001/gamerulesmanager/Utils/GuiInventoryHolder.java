package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Identifies inventories created by GamerulesManager without relying on their visible titles.
 */
public final class GuiInventoryHolder implements InventoryHolder {

    public enum MenuType {
        WORLD_SELECTOR,
        GAMERULE_PAGE_1,
        GAMERULE_PAGE_2
    }

    private final MenuType menuType;
    private final int page;
    private Inventory inventory;

    public GuiInventoryHolder(MenuType menuType) {
        this(menuType, 1);
    }

    // Stores the page number together with the menu type so paginated GUIs remain stateless.
    public GuiInventoryHolder(MenuType menuType, int page) {
        this.menuType = menuType;
        this.page = Math.max(1, page);
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public int getPage() {
        return page;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return Objects.requireNonNull(inventory, "GUI inventory has not been initialized yet.");
    }
}
