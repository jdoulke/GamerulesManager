package me.ted2001.gamerulesmanager.Listeners;

import me.ted2001.gamerulesmanager.GUI;

import me.ted2001.gamerulesmanager.GamerulesManager;
import me.ted2001.gamerulesmanager.Utils.CopyGamerules;
import me.ted2001.gamerulesmanager.Utils.GameruleCreator;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static me.ted2001.gamerulesmanager.GUI.*;
import static me.ted2001.gamerulesmanager.GamerulesManager.getPlugin;
import static me.ted2001.gamerulesmanager.GamerulesManager.serverVersion;
import static me.ted2001.gamerulesmanager.Listeners.WorldSelectorListener.WorldSelected;

@SuppressWarnings({"ConstantConditions", "rawtypes", "unchecked"})
public class GUIListener implements Listener {

    public static String prefix = ChatColor.RED + "" + "[" + ChatColor.GREEN + "" +"Ultimate Gamerules  Manager" + ChatColor.RED + "" + "] ";

    public static World chosenWorld = null;
    private final ArrayList<CopyGamerules> gamerulesList = new ArrayList();




    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {

        try {
            //check if the user is clicking on the Gamerule Manager first page.
            if (e.getView().getTitle().contains(ChatColor.DARK_PURPLE + "Gamerule Manager" + ChatColor.AQUA + " ")) {
                //players can not move items
                e.setCancelled(true);
                Inventory gui = e.getClickedInventory();
                Player p = (Player) e.getWhoClicked();
                if (e.getCurrentItem() == null)
                    return;
                World selectedWorld = WorldSelected;
                GameruleCreator creator = new GameruleCreator();
                String clickedItem = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                boolean flag = true;
                for(int i = 0; i < 36; i++){
                    if (clickedItem.equalsIgnoreCase(gamerules[i])){
                        if(GameRule.getByName(gamerules[i]).getType() == Boolean.class){
                            GameRule<Boolean> tempGamerule = (GameRule<Boolean>) GameRule.getByName(gamerules[i]);
                            booleanGameruleSet(tempGamerule, selectedWorld.getGameRuleValue(tempGamerule), selectedWorld,p);
                            gui.setItem(gamerulesSlots.get(gamerules[i]), creator.GamerulesCreator(gamerules[i], selectedWorld));
                            flag = false;
                            break;
                        }else {
                            p.closeInventory();
                            valueReceiver(p, gamerules[i]);
                        }
                    }
                }if(flag)
                    EssentialsButtons(e, p, selectedWorld);
            }
            if (e.getView().getTitle().contains(ChatColor.DARK_PURPLE + "Gamerule Manager Page 2" + ChatColor.AQUA + " ")){
                //players can not move items
                e.setCancelled(true);
                Inventory gui = e.getClickedInventory();
                Player p = (Player) e.getWhoClicked();
                World selectedWorld = WorldSelected;
                GameruleCreator creator = new GameruleCreator();
                if (e.getCurrentItem() == null)
                    return;
                String clickedItem = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                boolean flag = true;
                for(int i = 36; i < gamerules.length; i++){
                    if (clickedItem.equalsIgnoreCase(gamerules[i])){
                        if(GameRule.getByName(gamerules[i]).getType() == Boolean.class){
                            GameRule<Boolean> tempGamerule = (GameRule<Boolean>) GameRule.getByName(gamerules[i]);
                            booleanGameruleSet(tempGamerule, selectedWorld.getGameRuleValue(tempGamerule), selectedWorld,p);
                            gui.setItem(gamerulesSlots.get(gamerules[i])-36, creator.GamerulesCreator(gamerules[i], selectedWorld));
                            flag = false;
                            break;
                        }else {
                            p.closeInventory();
                            valueReceiver(p, gamerules[i]);
                        }
                    }
                }if(flag)
                    EssentialsButtons(e, p, selectedWorld);
            }
        //for 1.13 NullPointerException if you click air
        } catch (NullPointerException ignored) {}
    }

    private void valueReceiver(Player p, String gamerule) {
        World world = WorldSelected;
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setLore(Arrays.asList("Enter your value."));
        paper.setItemMeta(meta);
        new AnvilGUI.Builder()
                .itemLeft(paper)
                .onClick((slot, stateSnapshot) -> {
                    String text = stateSnapshot.getText();
                    int value;
                    try{
                        value = Integer.parseInt(text);
                    }catch (NumberFormatException ex){
                        p.sendMessage(prefix + ChatColor.YELLOW + "You didn't type an " + ChatColor.RED + "integer number" + ChatColor.YELLOW +".");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGui(p, WorldSelected)));
                    }
                    switch (gamerule) {
                        case "randomTickSpeed":
                            integerGameruleSetter(GameRule.RANDOM_TICK_SPEED, value, world, p);
                            if(gamerulesSlots.get(gamerule) < 36) {
                                Collections.singletonList(AnvilGUI.ResponseAction.close());
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGui(p, WorldSelected)));
                            }
                            else{
                                gameruleSetterGuiPage2(p);
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGuiPage2));
                            }
                        case "spawnRadius":
                            integerGameruleSetter(GameRule.SPAWN_RADIUS, value, world, p);
                            if(gamerulesSlots.get(gamerule) < 36)
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGui(p, WorldSelected)));
                            else{
                                gameruleSetterGuiPage2(p);
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGuiPage2));
                            }
                        case "maxEntityCramming":
                            integerGameruleSetter(GameRule.MAX_ENTITY_CRAMMING, value, world, p);
                            if(gamerulesSlots.get(gamerule) < 36)
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGui(p, WorldSelected)));
                            else{
                                gameruleSetterGuiPage2(p);
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGuiPage2));
                            }
                        case "maxCommandChainLength":
                            integerGameruleSetter(GameRule.MAX_COMMAND_CHAIN_LENGTH, value, world, p);
                            if(gamerulesSlots.get(gamerule) < 36)
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGui(p, WorldSelected)));
                            else{
                                gameruleSetterGuiPage2(p);
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGuiPage2));
                            }
                        case "playersSleepingPercentage":
                            integerGameruleSetter(GameRule.PLAYERS_SLEEPING_PERCENTAGE, value, world, p);
                            if(gamerulesSlots.get(gamerule) < 36)
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGui(p, WorldSelected)));
                            else{
                                gameruleSetterGuiPage2(p);
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGuiPage2));
                            }
                        case "snowAccumulationHeight":
                            integerGameruleSetter(GameRule.SNOW_ACCUMULATION_HEIGHT, value, world, p);
                            if(gamerulesSlots.get(gamerule) < 36)
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGui(p, WorldSelected)));
                            else{
                                gameruleSetterGuiPage2(p);
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGuiPage2));
                            }
                        case "commandModificationBlockLimit":
                            integerGameruleSetter(GameRule.COMMAND_MODIFICATION_BLOCK_LIMIT, value, world, p);
                            if(gamerulesSlots.get(gamerule) < 36)
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGui(p, WorldSelected)));
                            else{
                                gameruleSetterGuiPage2(p);
                                return Collections.singletonList(AnvilGUI.ResponseAction.openInventory(gameruleSetterGuiPage2));
                            }
                    }
                    return null;
                })
                .text("Read paper's info.")
                .title("Enter your value.")
                .plugin(getPlugin())
                .open(p);

    }



    private void booleanGameruleSet(GameRule<Boolean> gamerule, boolean value, World world, Player p){
        //here set the gamerule value to !value
        world.setGameRule(gamerule,!value);
        //play a sound to the player, so he realizes something happens.
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    private void integerGameruleSetter(GameRule<Integer> gamerule, int value, World world,Player p){
        world.setGameRule(gamerule, value);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }
    private ItemStack getItem(){
        ArrayList<String> lore = new ArrayList<>();
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName("Read paper's info.");
        lore.add("Rename this to your wanted value.");
        lore.add("Only integer numbers.");
        paperMeta.setLore(lore);
        paper.setItemMeta(paperMeta);
        return paper;
    }

    private void resetGamerules(World world){
        String[] gamerulesNames = world.getGameRules();
        for (String name : gamerulesNames) {
            GameRule gamerule;
            if (GameRule.getByName(name).getType() == Boolean.class) {
                gamerule = GameRule.getByName(name);
                Boolean defaultValue = (Boolean) world.getGameRuleDefault(gamerule);
                world.setGameRule(gamerule, defaultValue);
            } else if (GameRule.getByName(name).getType() == Integer.class) {
                gamerule = GameRule.getByName(name);
                int defaultValue = (Integer) world.getGameRuleDefault(gamerule);
                world.setGameRule(gamerule, defaultValue);
            }
        }
    }

    private void copyGamerules(World world){
        String[] gamerulesNames = world.getGameRules();
        Arrays.sort(gamerulesNames);
        for (String gamerule : gamerulesNames) {
            String value = world.getGameRuleValue(GameRule.getByName(gamerule)).toString();
            gamerulesList.add(new CopyGamerules(gamerule, value));
        }
        chosenWorld = world;
    }

    private void EssentialsButtons(InventoryClickEvent e, Player p, World selectedWorld){
        //get back option
        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Get Back in World Selection.")) {
            p.openInventory(GUI.guiBuilder(p));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
            //exit option
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "EXIT")) {
            p.closeInventory();
            if(Integer.parseInt(serverVersion) >= 14)
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1,1);
            else
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1,1);
            //next page option
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Next page with Gamerules.")) {
            GUI.gameruleSetterGuiPage2(p);
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN,1,1);
            p.openInventory(gameruleSetterGuiPage2);
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Previous page with Gamerules.")) {
            GUI.gameruleSetterGui(p,WorldSelected);
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN,1,1);
            p.openInventory(GUI.gameruleSetterGui(p,WorldSelected));
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Reset all " + ChatColor.YELLOW + "Gamerules")) {
            resetGamerules(selectedWorld);
            p.openInventory(GUI.gameruleSetterGui(p,selectedWorld));
            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1,1);
            //copy option
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_BLUE + "Copy " + ChatColor.YELLOW + "Gamerules")) {
            copyGamerules(selectedWorld);
            p.openInventory(GUI.guiBuilder(p));
            if(Integer.parseInt(serverVersion) >= 14)
                p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1,1);
            else
                p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1,1);
            //paste option
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Paste " + ChatColor.YELLOW + "Gamerules")) {
            if(chosenWorld != null) {
                for (int i = 0; i < WorldSelected.getGameRules().length; i++){
                    GameRule gamerule = GameRule.getByName(gamerulesList.get(i).getGameRule());
                    int intValue;
                    boolean booleanValue;
                    try {
                        intValue = Integer.parseInt(gamerulesList.get(i).getValue());
                        WorldSelected.setGameRule(gamerule, intValue);
                    }catch (NumberFormatException ex){
                        booleanValue = Boolean.parseBoolean(gamerulesList.get(i).getValue());
                        WorldSelected.setGameRule(gamerule, booleanValue);
                    }
                }
                if(Integer.parseInt(serverVersion) >= 14)
                    p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1,1);
                else
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1,1);
                p.openInventory(GUI.gameruleSetterGui(p,selectedWorld));
                p.sendMessage(prefix + ChatColor.YELLOW +"You copied all " + ChatColor.AQUA + "Gamerules " +
                        ChatColor.YELLOW +"from "  + ChatColor.BLUE + chosenWorld.getName()
                        + ChatColor.YELLOW + " to " + ChatColor.RED + WorldSelected.getName() + ChatColor.YELLOW + ".");
            }else{
                p.sendMessage(prefix + ChatColor.RED + "" + ChatColor.BOLD +"You didn't copy any world.");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1,1);
            }
        }
    }
}
