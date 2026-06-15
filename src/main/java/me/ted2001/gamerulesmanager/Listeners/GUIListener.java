package me.ted2001.gamerulesmanager.Listeners;

import me.ted2001.gamerulesmanager.GUI;



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
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.ted2001.gamerulesmanager.GUI.*;
import static me.ted2001.gamerulesmanager.GamerulesManager.*;
import me.ted2001.gamerulesmanager.Utils.PlayerSessionManager;
import static org.bukkit.Bukkit.getServer;

@SuppressWarnings({"ConstantConditions", "rawtypes", "unchecked"})
public class GUIListener implements Listener {



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
                World selectedWorld = PlayerSessionManager.getSelectedWorld(p);

                if (selectedWorld == null) {
                    p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "No world selected.");
                    p.openInventory(GUI.guiBuilder(p));
                    return;
                }
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
                World selectedWorld = PlayerSessionManager.getSelectedWorld(p);

                if (selectedWorld == null) {
                    p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "No world selected.");
                    p.openInventory(GUI.guiBuilder(p));
                    return;
                }
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
        } catch (NullPointerException exception) {
            getServer().getLogger().info("An error has occurred." + exception.getMessage());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void valueReceiver(Player p, String gamerule) {
        World world = PlayerSessionManager.getSelectedWorld(p);

        if (world == null) {
            p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "No world selected.");
            p.openInventory(GUI.guiBuilder(p));
            return;
        }

        GameRule<?> rule = GameRule.getByName(gamerule);

        if (rule == null) {
            p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "Unknown gamerule: " + gamerule);
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        if (rule.getType() != Integer.class) {
            p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "This gamerule is not an integer gamerule.");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        new AnvilGUI.Builder()
                .itemLeft(getItem())
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText().trim();
                    int value;

                    try {
                        value = Integer.parseInt(text);
                    } catch (NumberFormatException ex) {
                        p.sendMessage(getPlugin().getPluginPrefix()
                                + ChatColor.YELLOW + "You didn't type an "
                                + ChatColor.RED + "integer number"
                                + ChatColor.YELLOW + ".");

                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);

                        return Collections.singletonList(
                                AnvilGUI.ResponseAction.replaceInputText("Enter integer number")
                        );
                    }

                    integerGameruleSetter((GameRule) rule, value, world, p);

                    if ((Integer) gamerulesSlots.get(gamerule) < 36) {
                        return Collections.singletonList(
                                AnvilGUI.ResponseAction.openInventory(gameruleSetterGui(p, world))
                        );
                    }

                    return Collections.singletonList(
                            AnvilGUI.ResponseAction.openInventory(GUI.gameruleSetterGuiPage2(p))
                    );
                })
                .text("0")
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

    private void copyGamerules(Player player, World world) {
        List<CopyGamerules> copiedRules = new ArrayList<>();

        String[] gamerulesNames = world.getGameRules();
        Arrays.sort(gamerulesNames);

        for (String gamerule : gamerulesNames) {
            GameRule<?> rule = GameRule.getByName(gamerule);

            if (rule == null) {
                continue;
            }

            String value = world.getGameRuleValue(rule).toString();
            copiedRules.add(new CopyGamerules(gamerule, value));
        }

        PlayerSessionManager.setCopiedGamerules(player, world, copiedRules);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void EssentialsButtons(InventoryClickEvent e, Player p, World selectedWorld) {
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = clickedItem.getItemMeta();

        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return;
        }

        String displayName = itemMeta.getDisplayName();

        if (selectedWorld == null) {
            p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "No world selected.");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            p.openInventory(GUI.guiBuilder(p));
            return;
        }

        // Get back option
        if (displayName.equals(ChatColor.RED + "Get Back in World Selection.")) {
            p.openInventory(GUI.guiBuilder(p));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
            return;
        }

        // Exit option
        if (displayName.equals(ChatColor.RED + "EXIT")) {
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
            return;
        }

        // Next page option
        if (displayName.equals(ChatColor.RED + "Next page with Gamerules.")) {
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            p.openInventory(GUI.gameruleSetterGuiPage2(p));
            return;
        }

        // Previous page option
        if (displayName.equals(ChatColor.RED + "Previous page with Gamerules.")) {
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            p.openInventory(GUI.gameruleSetterGui(p, selectedWorld));
            return;
        }

        // Reset option
        if (displayName.equals(ChatColor.RED + "Reset all " + ChatColor.YELLOW + "Gamerules")) {
            resetGamerules(selectedWorld);
            p.openInventory(GUI.gameruleSetterGui(p, selectedWorld));
            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
            return;
        }

        // Copy option
        if (displayName.equals(ChatColor.DARK_BLUE + "Copy " + ChatColor.YELLOW + "Gamerules")) {
            copyGamerules(p, selectedWorld);
            p.openInventory(GUI.guiBuilder(p));
            p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1);
            return;
        }

        // Paste option
        if (displayName.equals(ChatColor.DARK_RED + "Paste " + ChatColor.YELLOW + "Gamerules")) {
            if (!PlayerSessionManager.hasCopiedGamerules(p)) {
                p.sendMessage(getPlugin().getPluginPrefix()
                        + ChatColor.RED + "" + ChatColor.BOLD + "You didn't copy any world.");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            for (CopyGamerules copiedGamerule : PlayerSessionManager.getCopiedGamerules(p)) {

                GameRule gamerule = GameRule.getByName(copiedGamerule.getGameRule());

                if (gamerule == null) {
                    continue;
                }

                String value = copiedGamerule.getValue();

                if (gamerule.getType() == Integer.class) {
                    try {
                        selectedWorld.setGameRule(gamerule, Integer.parseInt(value));
                    } catch (NumberFormatException ignored) {
                        getServer().getLogger().warning("Could not parse integer value for gamerule: "
                                + copiedGamerule.getGameRule());
                    }
                } else if (gamerule.getType() == Boolean.class) {
                    selectedWorld.setGameRule(gamerule, Boolean.parseBoolean(value));
                }
            }

            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            p.openInventory(GUI.gameruleSetterGui(p, selectedWorld));

            p.sendMessage(getPlugin().getPluginPrefix()
                    + ChatColor.YELLOW + "You copied all "
                    + ChatColor.AQUA + "Gamerules "
                    + ChatColor.YELLOW + "from "
                    + ChatColor.BLUE + PlayerSessionManager.getCopiedFromWorld(p).getName()
                    + ChatColor.YELLOW + " to "
                    + ChatColor.RED + selectedWorld.getName()
                    + ChatColor.YELLOW + ".");

            return;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerSessionManager.clear(event.getPlayer());
    }
}
