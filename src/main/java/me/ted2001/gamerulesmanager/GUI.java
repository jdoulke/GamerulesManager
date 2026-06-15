package me.ted2001.gamerulesmanager;

import me.ted2001.gamerulesmanager.Utils.Buttons;
import me.ted2001.gamerulesmanager.Utils.GameruleCreator;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.ted2001.gamerulesmanager.Utils.PlayerSessionManager;

public class GUI {

    private static final Buttons button = new Buttons();
    public static String[] gamerules;
    public static HashMap<String, Integer> gamerulesSlots = new HashMap<>();
    public static Inventory gameruleSetterGui(Player p, World world) {

        //sizes 9,18,27,36,45,54
        Inventory gui = Bukkit.createInventory(p, 54, ChatColor.DARK_PURPLE + "Gamerule Manager" + ChatColor.AQUA + " " + ChatColor.BOLD + world.getName());
        gamerules = world.getGameRules();
        gamerulesSlots.clear();
        Arrays.sort(gamerules);
        for(int i = 0; i < gamerules.length;i++){
            gamerulesSlots.put(gamerules[i],i);
        }
        GameruleCreator creator = new GameruleCreator();

        for (int i = 0; i < gamerules.length && i < 36; i++) {
            ItemStack item = creator.GamerulesCreator(gamerules[i], world);

            if (item != null) {
                gui.setItem(i, item);
            } else {
                gui.setItem(i, new ItemStack(Material.BARRIER));
            }
        }

        gui.setItem(45, button.backButton());
        gui.setItem(48, button.copyButton(world));
        gui.setItem(49, button.pasteButton());
        if(gamerules.length > 36)
            gui.setItem(51, button.nextPageButton());
        gui.setItem(52, button.resetButton());
        gui.setItem(53, button.exitButton());
        return gui;
    }

    public static Inventory gameruleSetterGuiPage2(Player p) {

        World selectedWorld = PlayerSessionManager.getSelectedWorld(p);

        if (selectedWorld == null) {
            return guiBuilder(p);
        }

        Inventory gui = Bukkit.createInventory(p, 54, ChatColor.DARK_PURPLE + "Gamerule Manager Page 2" + ChatColor.AQUA + " " + ChatColor.BOLD + selectedWorld.getName());

        GameruleCreator creator = new GameruleCreator();

        int guiSlot = 0;
        for (int i = 36; i < gamerules.length; i++) {
            ItemStack item = creator.GamerulesCreator(gamerules[i], selectedWorld);

            if (item != null) {
                gui.setItem(guiSlot, item);
            } else {
                gui.setItem(guiSlot, new ItemStack(Material.BARRIER));
            }

            guiSlot++;
        }

        //buttons
        gui.setItem(45, button.backButton());
        gui.setItem(48, button.copyButton(selectedWorld));
        gui.setItem(49, button.pasteButton());
        gui.setItem(50, button.previousPageButton());
        gui.setItem(52, button.resetButton());
        gui.setItem(53, button.exitButton());

        return gui;
    }

    public static Inventory guiBuilder(Player p) {
        Inventory world_selector = Bukkit.createInventory(p, 36, ChatColor.AQUA + "" + ChatColor.BOLD + "World Selector");
        List<World> worlds = Bukkit.getWorlds();
        String worldType;
        for (World world : worlds) {
            worldType = world.getEnvironment().toString();
            if (worldType.equalsIgnoreCase("NORMAL") || worldType.equalsIgnoreCase("CUSTOM"))
                world_selector.addItem(worldCreator("NORMAL", world.getName()));
            else if (worldType.equalsIgnoreCase("NETHER"))
                world_selector.addItem(worldCreator("NETHER", world.getName()));
            else if (worldType.equalsIgnoreCase("THE_END"))
                world_selector.addItem(worldCreator("END", world.getName()));
        }

        world_selector.setItem(35, button.exitButton());


        return world_selector;
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
            }
            world.setItemMeta(worldMeta);
            return world;
        }

        return null;
    }

}
