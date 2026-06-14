package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import static me.ted2001.gamerulesmanager.GamerulesManager.gamerulesDisplayItems;
import static me.ted2001.gamerulesmanager.GamerulesManager.getPlugin;
import static org.bukkit.Bukkit.getServer;

public class GameruleCreator {

    public ItemStack GamerulesCreator(String gamerule, World selectedWorld) {
        if (selectedWorld == null) {
            getServer().getLogger().warning("Selected world is null while creating gamerule item for: " + gamerule);
            return null;
        }

        GameRule<?> tempGamerule = GameRule.getByName(gamerule);

        if (tempGamerule == null) {
            getServer().getLogger().warning("Gamerule " + gamerule + " does not exist!");
            return null;
        }

        ItemStack item = gamerulesDisplayItems.get(tempGamerule);

        if (item == null) {
            item = new GameruleDisplayItem().gameruleDisplayItem(gamerule);
        } else {
            item = item.clone();
        }

        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) {
            getServer().getLogger().warning("Could not get item meta for gamerule item: " + gamerule);
            return item;
        }

        itemMeta.setDisplayName(ChatColor.RED + gamerule);
        itemMeta.addItemFlags(
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS
        );

        List<String> lore = new ArrayList<>();

        Object currentValue = selectedWorld.getGameRuleValue(tempGamerule);

        if (tempGamerule.getType() == Boolean.class) {
            if (Boolean.TRUE.equals(currentValue)) {
                lore.add(color(getConfigString("gameruleCondition", "&7Gamerule is currently set to: ")
                        + ChatColor.GREEN + "" + ChatColor.BOLD + "True"));
            } else {
                lore.add(color(getConfigString("gameruleCondition", "&7Gamerule is currently set to: ")
                        + ChatColor.RED + "" + ChatColor.BOLD + "False"));
            }
        } else {
            lore.add(color(getConfigString("gameruleCondition", "&7Gamerule is currently set to: ")
                    + ChatColor.GREEN + "" + ChatColor.BOLD + currentValue));
        }

        lore.add("");

        String configKey = GameRuleNameUtil.toConfigKey(gamerule);

        String description = getPlugin().getConfig().getString(configKey);

        if (description == null) {
            description = getConfigString(
                    "gameruleNotFound",
                    "&cGamerule description and item not found.\n&cAdd description and item on config.yml file."
            );
        }

        String[] lines = description.split("\n");

        for (String line : lines) {
            lore.add(color(line));
        }

        lore.add("");

        Object defaultValue = selectedWorld.getGameRuleDefault(tempGamerule);

        lore.add(color(getConfigString("gameruleDefaultValue", "&7Default value is: ") + defaultValue));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    private String getConfigString(String path, String fallback) {
        return getPlugin().getConfig().getString(path, fallback);
    }

    private String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}