package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static me.ted2001.gamerulesmanager.GamerulesManager.getPlugin;

/**
 * Stores internal GamerulesManager action identifiers on temporary GUI items.
 */
public final class GuiItemData {

    private static final NamespacedKey ACTION_KEY = new NamespacedKey(getPlugin(), "gui_action");
    private static final NamespacedKey VALUE_KEY = new NamespacedKey(getPlugin(), "gui_value");

    private GuiItemData() {
    }

    public static void setAction(ItemMeta meta, String action) {
        meta.getPersistentDataContainer().set(ACTION_KEY, PersistentDataType.STRING, action);
    }

    public static void setAction(ItemMeta meta, String action, String value) {
        setAction(meta, action);
        meta.getPersistentDataContainer().set(VALUE_KEY, PersistentDataType.STRING, value);
    }

    public static String getAction(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }

        return meta.getPersistentDataContainer().get(ACTION_KEY, PersistentDataType.STRING);
    }

    public static String getValue(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }

        return meta.getPersistentDataContainer().get(VALUE_KEY, PersistentDataType.STRING);
    }
}
