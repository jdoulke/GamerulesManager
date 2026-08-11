package me.ted2001.gamerulesmanager.Listeners;

import me.ted2001.gamerulesmanager.GUI;
import me.ted2001.gamerulesmanager.Utils.CopyGamerules;
import me.ted2001.gamerulesmanager.Utils.GameruleCreator;
import me.ted2001.gamerulesmanager.Utils.PlayerSessionManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static me.ted2001.gamerulesmanager.GUI.*;
import static me.ted2001.gamerulesmanager.GamerulesManager.*;
import static org.bukkit.Bukkit.getServer;

@SuppressWarnings({"ConstantConditions", "rawtypes", "unchecked"})
public class GUIListener implements Listener {

    private final Map<UUID, PendingValueInput> pendingValueInputs = new ConcurrentHashMap<>();

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
                            valueReceiver(p, gamerules[i], 1);
                            flag = false;
                            break;
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
                            valueReceiver(p, gamerules[i], 2);
                            flag = false;
                            break;
                        }
                    }
                }if(flag)
                    EssentialsButtons(e, p, selectedWorld);
            }
        } catch (NullPointerException exception) {
            getServer().getLogger().info("An error has occurred." + exception.getMessage());
        }
    }

    private void valueReceiver(Player p, String gamerule, int page) {
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

        pendingValueInputs.put(p.getUniqueId(), new PendingValueInput(gamerule, world, page));
        p.closeInventory();
        p.sendMessage(getPlugin().getPluginPrefix()
                + ChatColor.YELLOW + "Type the new integer value for "
                + ChatColor.AQUA + gamerule
                + ChatColor.YELLOW + " in chat. Your message will not be sent to other players.");
        p.sendMessage(getPlugin().getPluginPrefix()
                + ChatColor.GRAY + "Type " + ChatColor.RED + "cancel" + ChatColor.GRAY + " to go back without changing it.");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PendingValueInput pendingInput = pendingValueInputs.get(player.getUniqueId());

        if (pendingInput == null) {
            return;
        }

        event.setCancelled(true);
        String input = event.getMessage().trim();

        if (input.equalsIgnoreCase("cancel")) {
            pendingValueInputs.remove(player.getUniqueId());
            Bukkit.getScheduler().runTask(getPlugin(), () -> {
                if (!player.isOnline()) {
                    return;
                }

                player.sendMessage(getPlugin().getPluginPrefix() + ChatColor.YELLOW + "Gamerule change cancelled.");
                reopenGamerulePage(player, pendingInput);
            });
            return;
        }

        final int value;
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            Bukkit.getScheduler().runTask(getPlugin(), () -> {
                if (!player.isOnline()) {
                    return;
                }

                player.sendMessage(getPlugin().getPluginPrefix()
                        + ChatColor.YELLOW + "You didn't type an "
                        + ChatColor.RED + "integer number"
                        + ChatColor.YELLOW + ". Try again or type "
                        + ChatColor.RED + "cancel"
                        + ChatColor.YELLOW + ".");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            });
            return;
        }

        pendingValueInputs.remove(player.getUniqueId());
        Bukkit.getScheduler().runTask(getPlugin(), () -> applyIntegerGamerule(player, pendingInput, value));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void applyIntegerGamerule(Player player, PendingValueInput pendingInput, int value) {
        if (!player.isOnline()) {
            return;
        }

        GameRule<?> rule = GameRule.getByName(pendingInput.gamerule());
        if (rule == null || rule.getType() != Integer.class) {
            player.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "That gamerule is no longer available.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            reopenGamerulePage(player, pendingInput);
            return;
        }

        integerGameruleSetter((GameRule<Integer>) rule, value, pendingInput.world(), player);
        reopenGamerulePage(player, pendingInput);
    }

    private void reopenGamerulePage(Player player, PendingValueInput pendingInput) {
        PlayerSessionManager.setSelectedWorld(player, pendingInput.world());

        if (pendingInput.page() == 2) {
            player.openInventory(GUI.gameruleSetterGuiPage2(player));
            return;
        }

        player.openInventory(GUI.gameruleSetterGui(player, pendingInput.world()));
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
        pendingValueInputs.remove(event.getPlayer().getUniqueId());
        PlayerSessionManager.clear(event.getPlayer());
    }

    private record PendingValueInput(String gamerule, World world, int page) {
    }
}
