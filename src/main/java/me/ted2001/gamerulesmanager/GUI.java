package me.ted2001.gamerulesmanager;

import me.ted2001.gamerulesmanager.Utils.Buttons;
import me.ted2001.gamerulesmanager.Utils.GameRuleRegistryUtil;
import me.ted2001.gamerulesmanager.Utils.GameruleCreator;
import me.ted2001.gamerulesmanager.Utils.GuiInventoryHolder;
import me.ted2001.gamerulesmanager.Utils.GuiItemData;
import me.ted2001.gamerulesmanager.Utils.PlayerSessionManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUI {

    private static final Buttons button = new Buttons();
    private static final int WORLD_ITEMS_PER_PAGE = 45;

    public static Inventory gameruleSetterGui(Player p, World world) {
        GuiInventoryHolder holder = new GuiInventoryHolder(GuiInventoryHolder.MenuType.GAMERULE_PAGE_1);
        Inventory gui = Bukkit.createInventory(holder, 54,
                ChatColor.DARK_PURPLE + "Gamerule Manager" + ChatColor.AQUA + " " + ChatColor.BOLD + world.getName());
        holder.setInventory(gui);

        List<GameRule<?>> gamerules = GameRuleRegistryUtil.getSortedGameRules(world);
        GameruleCreator creator = new GameruleCreator();

        for (int i = 0; i < gamerules.size() && i < 36; i++) {
            String gameruleName = GameRuleRegistryUtil.getName(gamerules.get(i));
            ItemStack item = creator.GamerulesCreator(gameruleName, world);
            gui.setItem(i, item != null ? item : new ItemStack(Material.BARRIER));
        }

        gui.setItem(45, button.backButton());
        gui.setItem(48, button.copyButton(world));
        gui.setItem(49, button.pasteButton());
        if (gamerules.size() > 36) {
            gui.setItem(51, button.nextPageButton());
        }
        gui.setItem(52, button.resetButton());
        gui.setItem(53, button.exitButton());
        return gui;
    }

    public static Inventory gameruleSetterGuiPage2(Player p) {
        World selectedWorld = PlayerSessionManager.getSelectedWorld(p);

        if (selectedWorld == null) {
            return guiBuilder(p);
        }

        GuiInventoryHolder holder = new GuiInventoryHolder(GuiInventoryHolder.MenuType.GAMERULE_PAGE_2);
        Inventory gui = Bukkit.createInventory(holder, 54,
                ChatColor.DARK_PURPLE + "Gamerule Manager Page 2" + ChatColor.AQUA + " " + ChatColor.BOLD + selectedWorld.getName());
        holder.setInventory(gui);

        List<GameRule<?>> gamerules = GameRuleRegistryUtil.getSortedGameRules(selectedWorld);
        GameruleCreator creator = new GameruleCreator();

        int guiSlot = 0;
        for (int i = 36; i < gamerules.size() && guiSlot < 45; i++) {
            String gameruleName = GameRuleRegistryUtil.getName(gamerules.get(i));
            ItemStack item = creator.GamerulesCreator(gameruleName, selectedWorld);
            gui.setItem(guiSlot, item != null ? item : new ItemStack(Material.BARRIER));
            guiSlot++;
        }

        gui.setItem(45, button.backButton());
        gui.setItem(48, button.copyButton(selectedWorld));
        gui.setItem(49, button.pasteButton());
        gui.setItem(50, button.previousPageButton());
        gui.setItem(52, button.resetButton());
        gui.setItem(53, button.exitButton());

        return gui;
    }

    // Opens the last world-selector page used by this player, defaulting to the first page.
    public static Inventory guiBuilder(Player p) {
        return guiBuilder(p, PlayerSessionManager.getWorldSelectorPage(p));
    }

    // Builds a world selector that grows naturally from 2 to 6 rows and paginates after 45 worlds.
    public static Inventory guiBuilder(Player p, int requestedPage) {
        List<World> worlds = Bukkit.getWorlds();

        int totalPages = Math.max(1, (worlds.size() + WORLD_ITEMS_PER_PAGE - 1) / WORLD_ITEMS_PER_PAGE);
        int page = Math.max(1, Math.min(requestedPage, totalPages));

        // Remember the normalized page so returning from a gamerule menu opens the same world list page.
        PlayerSessionManager.setWorldSelectorPage(p, page);

        int startIndex = (page - 1) * WORLD_ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + WORLD_ITEMS_PER_PAGE, worlds.size());
        int worldsOnPage = endIndex - startIndex;

        // One full row is always reserved for navigation and the exit button.
        int worldRows = Math.max(1, (worldsOnPage + 8) / 9);
        int inventoryRows = Math.min(6, worldRows + 1);
        int inventorySize = inventoryRows * 9;

        GuiInventoryHolder holder = new GuiInventoryHolder(GuiInventoryHolder.MenuType.WORLD_SELECTOR, page);
        String title = ChatColor.AQUA + "" + ChatColor.BOLD + "World Selector";
        if (totalPages > 1) {
            title += ChatColor.GRAY + " (" + page + "/" + totalPages + ")";
        }

        Inventory worldSelector = Bukkit.createInventory(holder, inventorySize, title);
        holder.setInventory(worldSelector);

        // Fill only the content rows; the final row is kept free for navigation controls.
        int guiSlot = 0;
        for (int i = startIndex; i < endIndex; i++) {
            World world = worlds.get(i);
            String worldType = world.getEnvironment().toString();
            ItemStack worldItem = null;

            if (worldType.equalsIgnoreCase("NORMAL") || worldType.equalsIgnoreCase("CUSTOM")) {
                worldItem = worldCreator("NORMAL", world.getName());
            } else if (worldType.equalsIgnoreCase("NETHER")) {
                worldItem = worldCreator("NETHER", world.getName());
            } else if (worldType.equalsIgnoreCase("THE_END")) {
                worldItem = worldCreator("END", world.getName());
            }

            if (worldItem != null) {
                worldSelector.setItem(guiSlot, worldItem);
                guiSlot++;
            }
        }

        int controlRowStart = inventorySize - 9;

        if (page > 1) {
            worldSelector.setItem(controlRowStart, button.previousWorldPageButton());
        }

        if (page < totalPages) {
            worldSelector.setItem(inventorySize - 2, button.nextWorldPageButton());
        }

        worldSelector.setItem(inventorySize - 1, button.exitButton());
        return worldSelector;
    }

    private static ItemStack worldCreator(String type, String name){
        ItemStack world;
        ItemMeta worldMeta;

        if(type.equalsIgnoreCase("NORMAL")){
            world = new ItemStack(Material.GRASS_BLOCK);
            worldMeta = world.getItemMeta();
            if (worldMeta != null) {
                worldMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + name);
                worldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
                GuiItemData.setAction(worldMeta, "world", name);
            }
            world.setItemMeta(worldMeta);
            return world;
        }
        else if(type.equalsIgnoreCase("NETHER")){
            world = new ItemStack(Material.NETHERRACK);
            worldMeta = world.getItemMeta();
            if (worldMeta != null) {
                worldMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + name);
                worldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
                GuiItemData.setAction(worldMeta, "world", name);
            }
            world.setItemMeta(worldMeta);
            return world;
        }
        else if(type.equalsIgnoreCase("END")){
            world = new ItemStack(Material.END_STONE);
            worldMeta = world.getItemMeta();
            if (worldMeta != null) {
                worldMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + name);
                worldMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
                GuiItemData.setAction(worldMeta, "world", name);
            }
            world.setItemMeta(worldMeta);
            return world;
        }

        return null;
    }
}
