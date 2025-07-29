package me.ted2001.gamerulesmanager.Utils;

import me.ted2001.gamerulesmanager.GamerulesManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class GameruleDisplayItem {

    private final FileConfiguration config = GamerulesManager.getPlugin().getConfig();

    public ItemStack gameruleDisplayItem(String gamerule) {
        String materialName = config.getString("gameruleItems." + gamerule, "BOOK");
        Material material = Material.matchMaterial(materialName);
        if (material == null)
            material = Material.BOOK; // Default material if invalid

        return new ItemStack(material);
    }
}
