package me.ted2001.gamerules_manager_remake.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;

import static me.ted2001.gamerules_manager_remake.GamerulesManager.getPlugin;

public class Buttons {

    public ItemStack backButton(){
        ItemStack backButton = new ItemStack(Material.ARROW,1);
        ItemMeta backButtonmeta = backButton.getItemMeta();
        backButtonmeta.setDisplayName(ChatColor.RED + "Get Back in World Selection.");
        backButtonmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        backButton.setItemMeta(backButtonmeta);

        return backButton;
    }

    private ItemStack nextPageButton(){
        ItemStack nextButton = new ItemStack(Material.ARROW,1);
        ItemMeta nextButtonmeta = nextButton.getItemMeta();
        nextButtonmeta.setDisplayName(ChatColor.RED + "Next page with Gamerules.");
        nextButtonmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        nextButton.setItemMeta(nextButtonmeta);

        return nextButton;
    }
    private ItemStack previousPageButton(){
        ItemStack previousButton = new ItemStack(Material.ARROW,1);
        ItemMeta previousButtonmeta = previousButton.getItemMeta();
        previousButtonmeta.setDisplayName(ChatColor.RED + "Previous page with Gamerules.");
        previousButtonmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        previousButton.setItemMeta(previousButtonmeta);

        return previousButton;
    }

    public ItemStack exitButton(){
        ItemStack exitButton = new ItemStack(Material.DARK_OAK_DOOR,1);
        ItemMeta exitButtonmeta = exitButton.getItemMeta();
        exitButtonmeta.setDisplayName(ChatColor.RED + "EXIT");
        exitButtonmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        exitButton.setItemMeta(exitButtonmeta);

        return exitButton;
    }

    public ItemStack resetButton(){
        ArrayList<String> lore = new ArrayList<>();
        ItemStack resetButton = new ItemStack(Material.REDSTONE_BLOCK,1);
        ItemMeta resetButtonmeta = resetButton.getItemMeta();
        resetButtonmeta.setDisplayName(ChatColor.RED + "Reset all " + ChatColor.YELLOW + "Gamerules");
        resetButtonmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        lore.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getPlugin().getConfig().getString("resetGamerule"))));
        resetButtonmeta.setLore(lore);
        resetButton.setItemMeta(resetButtonmeta);

        return resetButton;
    }
    public ItemStack copyButton(World w){
        ArrayList<String> lore = new ArrayList<>();
        ItemStack copyButton = new ItemStack(Material.BOOK,1);
        ItemMeta copyButtonmeta = copyButton.getItemMeta();
        copyButtonmeta.setDisplayName(ChatColor.DARK_BLUE + "Copy " + ChatColor.YELLOW + "Gamerules");
        copyButtonmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        lore.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getPlugin().getConfig().getString("copyGamerule")))+ ChatColor.RED+ w.getName());
        copyButtonmeta.setLore(lore);
        copyButton.setItemMeta(copyButtonmeta);

        return copyButton;
    }
    public ItemStack pasteButton(){
        ArrayList<String> lore = new ArrayList<>();
        ItemStack pasteButton = new ItemStack(Material.ENCHANTED_BOOK,1);
        ItemMeta pasteButtonmeta = pasteButton.getItemMeta();
        pasteButtonmeta.setDisplayName(ChatColor.DARK_RED + "Paste " + ChatColor.YELLOW + "Gamerules");
        pasteButtonmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        lore.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getPlugin().getConfig().getString("pasteGamerule"))));
        pasteButtonmeta.setLore(lore);
        pasteButton.setItemMeta(pasteButtonmeta);

        return pasteButton;
    }
}
