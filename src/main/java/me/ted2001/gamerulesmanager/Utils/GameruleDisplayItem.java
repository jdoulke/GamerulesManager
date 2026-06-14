package me.ted2001.gamerulesmanager.Utils;

import me.ted2001.gamerulesmanager.GamerulesManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class GameruleDisplayItem {

    private final FileConfiguration config = GamerulesManager.getPlugin().getConfig();

    public ItemStack gameruleDisplayItem(String gamerule) {
        String configKey = GameRuleNameUtil.toConfigKey(gamerule);

        String materialName = config.getString("gameruleItems." + configKey, "BOOK");

        Material material = Material.matchMaterial(
                materialName.trim().toUpperCase(Locale.ROOT)
        );

        if (material == null || !material.isItem()) {
            material = Material.BOOK;
        }

        return new ItemStack(material);
    }
}