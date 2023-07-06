package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import java.util.Objects;

import net.md_5.bungee.api.ChatColor;

import static me.ted2001.gamerulesmanager.GamerulesManager.gamerulesDisplayItems;
import static me.ted2001.gamerulesmanager.GamerulesManager.getPlugin;

public class GameruleCreator {

    public ItemStack GamerulesCreator(String gamerule, World playerWorld){

        ArrayList<String> lore = new ArrayList<>();
        ItemStack item = gamerulesDisplayItems.get(GameRule.getByName(gamerule));
        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.RED + gamerule);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        GameRule<?> tempGamerule = GameRule.getByName(gamerule);
        assert tempGamerule != null;
        if(tempGamerule.getType() == Boolean.class){
            if(Boolean.TRUE.equals(playerWorld.getGameRuleValue(Objects.requireNonNull(GameRule.getByName(gamerule)))))
                lore.add(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("gameruleCondition") + ChatColor.GREEN + "" + ChatColor.BOLD + "True"));

            else
                lore.add(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("gameruleCondition") + ChatColor.RED + "" + ChatColor.BOLD + "False"));
        }else
            lore.add(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("gameruleCondition") + ChatColor.GREEN + "" + ChatColor.BOLD + playerWorld.getGameRuleValue(Objects.requireNonNull(GameRule.getByName(gamerule)))));
        lore.add("");

        String remainingString = Objects.requireNonNull(getPlugin().getConfig().getString(gamerule));
        String[] lines = remainingString.split("\n");
        for (String line : lines)
            lore.add(ChatColor.translateAlternateColorCodes('&', line));

        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("gameruleDefaultValue") + playerWorld.getGameRuleDefault(tempGamerule)));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }
}
