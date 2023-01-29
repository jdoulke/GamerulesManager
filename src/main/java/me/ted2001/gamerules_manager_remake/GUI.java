package me.ted2001.gamerules_manager_remake;

import me.ted2001.gamerules_manager_remake.Utils.Buttons;
import me.ted2001.gamerules_manager_remake.Utils.GameruleCreator;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static me.ted2001.gamerules_manager_remake.Listeners.WorldSelectorListener.WorldSelected;

public class GUI {

    public static Inventory gameruleSetterGuiPage2;
    private Buttons button = new Buttons();
    private GameruleCreator creator;
    public Inventory gameruleSetterGui(Player p, World w) {

        //sizes 9,18,27,36,45,54
        Inventory gui = Bukkit.createInventory(p, 54, ChatColor.DARK_PURPLE + "Gamerule Manager" + ChatColor.AQUA + " " +ChatColor.BOLD+ WorldSelected.getName());

        GameRule<?>[] gamerules = GameRule.values();

            for (int i = 0; i < gamerules.length; i++) {
                p.sendMessage(gamerules[i].getName());
                gui.setItem(i, creator.GamerulesCreator(gamerules[i].getName(), p.getWorld()));
            }


        gui.setItem(45, button.backButton());
        gui.setItem(48, button.copyButton(WorldSelected));
        gui.setItem(49, button.pasteButton());
        gui.setItem(52, button.resetButton());
        gui.setItem(53, button.exitButton());
        return gui;
    }

    public Inventory guiBuilder(Player p) {
        Inventory world_selector = Bukkit.createInventory(p, 36, ChatColor.AQUA + "" + ChatColor.BOLD + "World Selector");
        List<World> worlds = Bukkit.getWorlds();
        String worldtype;
        for (World world : worlds) {
            worldtype = world.getEnvironment().toString();
            if (worldtype.equalsIgnoreCase("NORMAL") || worldtype.equalsIgnoreCase("CUSTOM"))
                world_selector.addItem(worldCreator("NORMAL", world.getName()));
            else if (worldtype.equalsIgnoreCase("NETHER"))
                world_selector.addItem(worldCreator("NETHER", world.getName()));
            else if (worldtype.equalsIgnoreCase("THE_END"))
                world_selector.addItem(worldCreator("END", world.getName()));
        }

        world_selector.setItem(35, button.exitButton());


        return world_selector;
    }

    private ItemStack worldCreator(String type, String name){
        ItemStack world;
        ItemMeta worldmeta;
        if(type.equalsIgnoreCase("NORMAL")){
            world = new ItemStack(Material.GRASS_BLOCK);
            worldmeta = world.getItemMeta();
            if (worldmeta != null) {
                worldmeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + name);
                worldmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
            }
            world.setItemMeta(worldmeta);
            return world;
        }
        else if(type.equalsIgnoreCase("NETHER")){
            world = new ItemStack(Material.NETHERRACK);
            worldmeta = world.getItemMeta();
            if (worldmeta != null) {
                worldmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + name);
                worldmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
            }
            world.setItemMeta(worldmeta);
            return world;
        }
        else if(type.equalsIgnoreCase("END")){
            world = new ItemStack(Material.END_STONE);
            worldmeta = world.getItemMeta();
            if (worldmeta != null) {
                worldmeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + name);
                worldmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
            }
            world.setItemMeta(worldmeta);
            return world;
        }

        return null;
    }

}
