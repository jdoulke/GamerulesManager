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

import static me.ted2001.gamerulesmanager.Listeners.WorldSelectorListener.WorldSelected;

public class GUI {

    private static final Buttons button = new Buttons();
    public static Inventory gameruleSetterGuiPage2;
    public static String[] gamerules;
    public static HashMap<String, Integer> gamerulesSlots = new HashMap<>();
    public static Inventory gameruleSetterGui(Player p, World world) {

        //sizes 9,18,27,36,45,54
        Inventory gui = Bukkit.createInventory(p, 54, ChatColor.DARK_PURPLE + "Gamerule Manager" + ChatColor.AQUA + " " +ChatColor.BOLD+ WorldSelected.getName());


        gamerules = world.getGameRules();
        Arrays.sort(gamerules);
        for(int i = 0; i < gamerules.length;i++){
            gamerulesSlots.put(gamerules[i],i);
        }
        for (int i = 0; i < gamerules.length && i < 36; i++) {
            GameruleCreator creator = new GameruleCreator();
            if(creator.GamerulesCreator(gamerules[i], world) != null)
                gui.setItem(i, creator.GamerulesCreator(gamerules[i], world));
            else
                gui.setItem(i , new ItemStack(Material.BARRIER));
        }
        gui.setItem(45, button.backButton());
        gui.setItem(48, button.copyButton(WorldSelected));
        gui.setItem(49, button.pasteButton());
        if(gamerules.length > 36)
            gui.setItem(51, button.nextPageButton());
        gui.setItem(52, button.resetButton());
        gui.setItem(53, button.exitButton());
        return gui;
    }

    public static void gameruleSetterGuiPage2(Player p) {


        Inventory gui = Bukkit.createInventory(p, 54, ChatColor.DARK_PURPLE + "Gamerule Manager Page 2" + ChatColor.AQUA + " " + ChatColor.BOLD + WorldSelected.getName());


        int guiSlot=0;
        for (int i = 36; i < gamerules.length; i++) {
            GameruleCreator creator = new GameruleCreator();
            gui.setItem(guiSlot , creator.GamerulesCreator(gamerules[i], p.getWorld()));
            guiSlot++;
        }

        //buttons
        gui.setItem(45, button.backButton());
        gui.setItem(48, button.copyButton(WorldSelected));
        gui.setItem(49, button.pasteButton());
        gui.setItem(50, button.previousPageButton());
        gui.setItem(52, button.resetButton());
        gui.setItem(53, button.exitButton());
        gameruleSetterGuiPage2 = gui;
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
