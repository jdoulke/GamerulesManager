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
    private Inventory inventory;

    public GuiInventoryHolder(MenuType menuType) {
        this.menuType = menuType;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return Objects.requireNonNull(inventory, "GUI inventory has not been initialized yet.");
    }
}
