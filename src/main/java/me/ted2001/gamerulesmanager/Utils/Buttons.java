package me.ted2001.gamerulesmanager.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;

import static me.ted2001.gamerulesmanager.GamerulesManager.getPlugin;

public class Buttons {

    public ItemStack backButton(){
        ItemStack backButton = new ItemStack(Material.ARROW,1);
        ItemMeta backButtonMeta = backButton.getItemMeta();
        assert backButtonMeta != null;
        backButtonMeta.setDisplayName(ChatColor.RED + "Get Back in World Selection.");
        backButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        GuiItemData.setAction(backButtonMeta, "back");
        backButton.setItemMeta(backButtonMeta);

        return backButton;
    }

    public ItemStack nextPageButton(){
        ItemStack nextButton = new ItemStack(Material.ARROW,1);
        ItemMeta nextButtonMeta = nextButton.getItemMeta();
        assert nextButtonMeta != null;
        nextButtonMeta.setDisplayName(ChatColor.RED + "Next page with Gamerules.");
        nextButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        GuiItemData.setAction(nextButtonMeta, "next_page");
        nextButton.setItemMeta(nextButtonMeta);

        return nextButton;
    }

    public ItemStack previousPageButton(){
        ItemStack previousButton = new ItemStack(Material.ARROW,1);
        ItemMeta previousButtonMeta = previousButton.getItemMeta();
        assert previousButtonMeta != null;
        previousButtonMeta.setDisplayName(ChatColor.RED + "Previous page with Gamerules.");
        previousButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        GuiItemData.setAction(previousButtonMeta, "previous_page");
        previousButton.setItemMeta(previousButtonMeta);

        return previousButton;
    }

    // Navigation button used only by the paginated world-selection GUI.
    public ItemStack nextWorldPageButton(){
        ItemStack nextButton = new ItemStack(Material.ARROW,1);
        ItemMeta nextButtonMeta = nextButton.getItemMeta();
        assert nextButtonMeta != null;
        nextButtonMeta.setDisplayName(ChatColor.GREEN + "Next World Page.");
        nextButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        GuiItemData.setAction(nextButtonMeta, "next_world_page");
        nextButton.setItemMeta(nextButtonMeta);

        return nextButton;
    }

    // Navigation button used only by the paginated world-selection GUI.
    public ItemStack previousWorldPageButton(){
        ItemStack previousButton = new ItemStack(Material.ARROW,1);
        ItemMeta previousButtonMeta = previousButton.getItemMeta();
        assert previousButtonMeta != null;
        previousButtonMeta.setDisplayName(ChatColor.GREEN + "Previous World Page.");
        previousButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        GuiItemData.setAction(previousButtonMeta, "previous_world_page");
        previousButton.setItemMeta(previousButtonMeta);

        return previousButton;
    }

    public ItemStack exitButton(){
        ItemStack exitButton = new ItemStack(Material.DARK_OAK_DOOR,1);
        ItemMeta exitButtonMeta = exitButton.getItemMeta();
        assert exitButtonMeta != null;
        exitButtonMeta.setDisplayName(ChatColor.RED + "EXIT");
        exitButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        GuiItemData.setAction(exitButtonMeta, "exit");
        exitButton.setItemMeta(exitButtonMeta);

        return exitButton;
    }

    public ItemStack resetButton(){
        ArrayList<String> lore = new ArrayList<>();
        ItemStack resetButton = new ItemStack(Material.REDSTONE_BLOCK,1);
        ItemMeta resetButtonMeta = resetButton.getItemMeta();
        assert resetButtonMeta != null;
        resetButtonMeta.setDisplayName(ChatColor.RED + "Reset all " + ChatColor.YELLOW + "Gamerules");
        resetButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        lore.add(ColorUtils.translateColorCodes(Objects.requireNonNull(getPlugin().getConfig().getString("resetGamerule"))));
        resetButtonMeta.setLore(lore);
        GuiItemData.setAction(resetButtonMeta, "reset");
        resetButton.setItemMeta(resetButtonMeta);

        return resetButton;
    }

    public ItemStack copyButton(World w){
        ArrayList<String> lore = new ArrayList<>();
        ItemStack copyButton = new ItemStack(Material.BOOK,1);
        ItemMeta copyButtonMeta = copyButton.getItemMeta();
        assert copyButtonMeta != null;
        copyButtonMeta.setDisplayName(ChatColor.DARK_BLUE + "Copy " + ChatColor.YELLOW + "Gamerules");
        copyButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        lore.add(ColorUtils.translateColorCodes(Objects.requireNonNull(getPlugin().getConfig().getString("copyGamerule"))) + ChatColor.RED + w.getName());
        copyButtonMeta.setLore(lore);
        GuiItemData.setAction(copyButtonMeta, "copy");
        copyButton.setItemMeta(copyButtonMeta);

        return copyButton;
    }

    public ItemStack pasteButton(){
        ArrayList<String> lore = new ArrayList<>();
        ItemStack pasteButton = new ItemStack(Material.ENCHANTED_BOOK,1);
        ItemMeta pasteButtonMeta = pasteButton.getItemMeta();
        assert pasteButtonMeta != null;
        pasteButtonMeta.setDisplayName(ChatColor.DARK_RED + "Paste " + ChatColor.YELLOW + "Gamerules");
        pasteButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        lore.add(ColorUtils.translateColorCodes(Objects.requireNonNull(getPlugin().getConfig().getString("pasteGamerule"))));
        pasteButtonMeta.setLore(lore);
        GuiItemData.setAction(pasteButtonMeta, "paste");
        pasteButton.setItemMeta(pasteButtonMeta);

        return pasteButton;
    }
}
