package me.ted2001.gamerules_manager_remake.Utils;

import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;

import static me.ted2001.gamerules_manager_remake.GamerulesManager.getPlugin;

public class GameruleCreator {

    public ItemStack GamerulesCreator(String gamerule, World playerworld){

        ArrayList<String> lore = new ArrayList<>();
        ItemStack item = new ItemStack(Material.BOOK,1);
        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.RED + gamerule);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        GameRule<?> tempGamerule = GameRule.getByName(gamerule);
        assert tempGamerule != null;
        if(tempGamerule.getType() == Boolean.class){
            if(Boolean.TRUE.equals(playerworld.getGameRuleValue(GameRule.getByName(gamerule))))
                lore.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("gameruleCondition") + ChatColor.GREEN + "" + ChatColor.BOLD + "True"));

            else
                lore.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("gameruleCondition") + ChatColor.RED + "" + ChatColor.BOLD + "False"));
        }else
            lore.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("gameruleCondition") + ChatColor.GREEN + "" + ChatColor.BOLD + playerworld.getGameRuleValue(GameRule.getByName(gamerule))));

        lore.add("");
        lore.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString(gamerule)));
        lore.add("");
        lore.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', getPlugin().getConfig().getString("gameruleDefaultValue ") + playerworld.getGameRuleDefault(tempGamerule)));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }
}
