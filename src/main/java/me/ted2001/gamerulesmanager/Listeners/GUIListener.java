package me.ted2001.gamerulesmanager.Listeners;

import me.ted2001.gamerulesmanager.GUI;
import me.ted2001.gamerulesmanager.Utils.ColorUtils;
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

    // Number of invalid chat inputs allowed before the pending gamerule edit is cancelled.
    private static final int MAX_INVALID_ATTEMPTS = 2;

    // Stores the integer gamerule edit currently waiting for chat input from each player.
    private final Map<UUID, PendingValueInput> pendingValueInputs = new ConcurrentHashMap<>();

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {

        try {
            // Handle clicks on the first Gamerule Manager page.
            if (e.getView().getTitle().contains(ChatColor.DARK_PURPLE + "Gamerule Manager" + ChatColor.AQUA + " ")) {
                // Prevent players from moving GUI items into or out of the inventory.
                e.setCancelled(true);
                Inventory gui = e.getClickedInventory();
                Player p = (Player) e.getWhoClicked();
                if (e.getCurrentItem() == null)
                    return;
                World selectedWorld = PlayerSessionManager.getSelectedWorld(p);

                // A selected world is required before any gamerule can be edited.
                if (selectedWorld == null) {
                    sendConfiguredMessage(p, "noWorldSelected", "&cNo world selected.");
                    p.openInventory(GUI.guiBuilder(p));
                    return;
                }
                GameruleCreator creator = new GameruleCreator();
                String clickedItem = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                boolean flag = true;

                // First page contains gamerules stored in slots/indexes 0-35.
                for(int i = 0; i < 36; i++){
                    if (clickedItem.equalsIgnoreCase(gamerules[i])){
                        if(GameRule.getByName(gamerules[i]).getType() == Boolean.class){
                            // Boolean gamerules can be toggled immediately with a single click.
                            GameRule<Boolean> tempGamerule = (GameRule<Boolean>) GameRule.getByName(gamerules[i]);
                            booleanGameruleSet(tempGamerule, selectedWorld.getGameRuleValue(tempGamerule), selectedWorld,p);
                            gui.setItem(gamerulesSlots.get(gamerules[i]), creator.GamerulesCreator(gamerules[i], selectedWorld));
                            flag = false;
                            break;
                        }else {
                            // Integer gamerules require a value from the player's next chat message.
                            valueReceiver(p, gamerules[i], 1);
                            flag = false;
                            break;
                        }
                    }
                }if(flag)
                    // If no gamerule was clicked, check whether one of the navigation/action buttons was used.
                    EssentialsButtons(e, p, selectedWorld);
            }

            // Handle clicks on the second Gamerule Manager page.
            if (e.getView().getTitle().contains(ChatColor.DARK_PURPLE + "Gamerule Manager Page 2" + ChatColor.AQUA + " ")){
                // Prevent players from moving GUI items into or out of the inventory.
                e.setCancelled(true);
                Inventory gui = e.getClickedInventory();
                Player p = (Player) e.getWhoClicked();
                World selectedWorld = PlayerSessionManager.getSelectedWorld(p);

                // A selected world is required before any gamerule can be edited.
                if (selectedWorld == null) {
                    sendConfiguredMessage(p, "noWorldSelected", "&cNo world selected.");
                    p.openInventory(GUI.guiBuilder(p));
                    return;
                }
                GameruleCreator creator = new GameruleCreator();
                if (e.getCurrentItem() == null)
                    return;
                String clickedItem = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                boolean flag = true;

                // Second page contains the remaining gamerules starting from index 36.
                for(int i = 36; i < gamerules.length; i++){
                    if (clickedItem.equalsIgnoreCase(gamerules[i])){
                        if(GameRule.getByName(gamerules[i]).getType() == Boolean.class){
                            // Boolean gamerules can be toggled immediately with a single click.
                            GameRule<Boolean> tempGamerule = (GameRule<Boolean>) GameRule.getByName(gamerules[i]);
                            booleanGameruleSet(tempGamerule, selectedWorld.getGameRuleValue(tempGamerule), selectedWorld,p);
                            gui.setItem(gamerulesSlots.get(gamerules[i])-36, creator.GamerulesCreator(gamerules[i], selectedWorld));
                            flag = false;
                            break;
                        }else {
                            // Remember that this edit originated from page 2 so the same page can be reopened later.
                            valueReceiver(p, gamerules[i], 2);
                            flag = false;
                            break;
                        }
                    }
                }if(flag)
                    // If no gamerule was clicked, check whether one of the navigation/action buttons was used.
                    EssentialsButtons(e, p, selectedWorld);
            }
        } catch (NullPointerException exception) {
            getServer().getLogger().info("An error has occurred." + exception.getMessage());
        }
    }

    // Starts the chat-based input flow for an integer gamerule.
    private void valueReceiver(Player p, String gamerule, int page) {
        World world = PlayerSessionManager.getSelectedWorld(p);

        // The selected world is stored because the GUI closes while the player types the value.
        if (world == null) {
            sendConfiguredMessage(p, "noWorldSelected", "&cNo world selected.");
            p.openInventory(GUI.guiBuilder(p));
            return;
        }

        GameRule<?> rule = GameRule.getByName(gamerule);

        // Protect against a gamerule name that is no longer available in the current server version.
        if (rule == null) {
            sendConfiguredMessage(p, "integerInputUnknownRule", "&cUnknown gamerule: &f%gamerule%", gamerule);
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Chat input is only required for integer gamerules; booleans are handled directly by the GUI click.
        if (rule.getType() != Integer.class) {
            sendConfiguredMessage(p, "integerInputNotInteger", "&cThis gamerule is not an integer gamerule.");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Fetch Minecraft's default value so it can be exposed through the %default_value% placeholder.
        Integer defaultValue = world.getGameRuleDefault((GameRule<Integer>) rule);

        // Save all context needed to apply the value and return the player to the same world/page afterwards.
        pendingValueInputs.put(p.getUniqueId(), new PendingValueInput(gamerule, world, page, 0));
        p.closeInventory();

        // Ask the player for the new value. This message is configurable for localization.
        sendConfiguredMessage(
                p,
                "integerInputPrompt",
                "&eType the new integer value for &b%gamerule%&e in chat. &7Default value: &f%default_value%&e. Your message will not be sent to other players.",
                gamerule,
                String.valueOf(defaultValue)
        );
        sendConfiguredMessage(p, "integerInputCancelHint", "&7Type &ccancel&7 to go back without changing it.");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PendingValueInput pendingInput = pendingValueInputs.get(player.getUniqueId());

        // Players without a pending gamerule edit use chat normally.
        if (pendingInput == null) {
            return;
        }

        // Consume the message so the entered gamerule value is never broadcast to public chat.
        event.setCancelled(true);
        String input = event.getMessage().trim();

        // Manual cancellation immediately restores normal chat behaviour and returns to the previous GUI page.
        if (input.equalsIgnoreCase("cancel")) {
            pendingValueInputs.remove(player.getUniqueId());
            Bukkit.getScheduler().runTask(getPlugin(), () -> {
                if (!player.isOnline()) {
                    return;
                }

                sendConfiguredMessage(player, "integerInputCancelled", "&eGamerule change cancelled.");
                reopenGamerulePage(player, pendingInput);
            });
            return;
        }

        final int value;
        try {
            // Integer.parseInt also supports valid negative gamerule values where Minecraft allows them.
            value = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            int invalidAttempts = pendingInput.invalidAttempts() + 1;

            // After two invalid values, stop intercepting chat and cancel the edit completely.
            if (invalidAttempts >= MAX_INVALID_ATTEMPTS) {
                pendingValueInputs.remove(player.getUniqueId());
                Bukkit.getScheduler().runTask(getPlugin(), () -> {
                    if (!player.isOnline()) {
                        return;
                    }

                    sendConfiguredMessage(player, "integerInputTooManyInvalid", "&cToo many invalid attempts. Gamerule change cancelled and chat input restored.");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    reopenGamerulePage(player, pendingInput);
                });
                return;
            }

            // Keep waiting for one more value while preserving the original world/page context.
            pendingValueInputs.put(player.getUniqueId(), pendingInput.withInvalidAttempts(invalidAttempts));
            Bukkit.getScheduler().runTask(getPlugin(), () -> {
                if (!player.isOnline()) {
                    return;
                }

                sendConfiguredMessage(player, "integerInputInvalid", "&eYou didn't type an &cinteger number&e. Try again or type &ccancel&e.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            });
            return;
        }

        // A valid value completes the input flow. Bukkit world/GUI operations are scheduled on the main thread.
        pendingValueInputs.remove(player.getUniqueId());
        Bukkit.getScheduler().runTask(getPlugin(), () -> applyIntegerGamerule(player, pendingInput, value));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    // Applies the parsed integer gamerule value on the main server thread.
    private void applyIntegerGamerule(Player player, PendingValueInput pendingInput, int value) {
        // The scheduled task may run after the player disconnects, so do not reopen a GUI for an offline player.
        if (!player.isOnline()) {
            return;
        }

        // Resolve the gamerule again before applying it instead of assuming the stored name is still valid.
        GameRule<?> rule = GameRule.getByName(pendingInput.gamerule());
        if (rule == null || rule.getType() != Integer.class) {
            sendConfiguredMessage(player, "integerInputRuleUnavailable", "&cThat gamerule is no longer available.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            reopenGamerulePage(player, pendingInput);
            return;
        }

        integerGameruleSetter((GameRule<Integer>) rule, value, pendingInput.world(), player);
        reopenGamerulePage(player, pendingInput);
    }

    // Reopens the exact gamerule page and world the player was viewing before entering chat input.
    private void reopenGamerulePage(Player player, PendingValueInput pendingInput) {
        PlayerSessionManager.setSelectedWorld(player, pendingInput.world());

        if (pendingInput.page() == 2) {
            player.openInventory(GUI.gameruleSetterGuiPage2(player));
            return;
        }

        player.openInventory(GUI.gameruleSetterGui(player, pendingInput.world()));
    }

    // Sends a configurable localized message without placeholders.
    private void sendConfiguredMessage(Player player, String path, String fallback) {
        String message = getPlugin().getConfig().getString(path, fallback);
        player.sendMessage(getPlugin().getPluginPrefix() + ColorUtils.translateColorCodes(message));
    }

    // Sends a configurable localized message and replaces the %gamerule% placeholder.
    private void sendConfiguredMessage(Player player, String path, String fallback, String gamerule) {
        String message = getPlugin().getConfig().getString(path, fallback).replace("%gamerule%", gamerule);
        player.sendMessage(getPlugin().getPluginPrefix() + ColorUtils.translateColorCodes(message));
    }

    // Sends the integer-input prompt and replaces both %gamerule% and %default_value% placeholders.
    private void sendConfiguredMessage(Player player, String path, String fallback, String gamerule, String defaultValue) {
        String message = getPlugin().getConfig().getString(path, fallback)
                .replace("%gamerule%", gamerule)
                .replace("%default_value%", defaultValue);
        player.sendMessage(getPlugin().getPluginPrefix() + ColorUtils.translateColorCodes(message));
    }

    // Toggle a boolean gamerule to the opposite of its current value.
    private void booleanGameruleSet(GameRule<Boolean> gamerule, boolean value, World world, Player p){
        world.setGameRule(gamerule,!value);
        // Play feedback so the player knows the click successfully changed the gamerule.
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    // Apply an integer gamerule value received from chat input.
    private void integerGameruleSetter(GameRule<Integer> gamerule, int value, World world,Player p){
        world.setGameRule(gamerule, value);
        // Play feedback so the player knows the new value was accepted.
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    // Restore every gamerule in the selected world to Minecraft's default value.
    private void resetGamerules(World world){
        String[] gamerulesNames = world.getGameRules();
        for (String name : gamerulesNames) {
            GameRule gamerule;

            // Boolean and integer gamerules must be restored using values of their matching Java type.
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

    // Capture all gamerule values from the current world so they can later be pasted into another world.
    private void copyGamerules(Player player, World world) {
        List<CopyGamerules> copiedRules = new ArrayList<>();

        String[] gamerulesNames = world.getGameRules();
        Arrays.sort(gamerulesNames);

        for (String gamerule : gamerulesNames) {
            GameRule<?> rule = GameRule.getByName(gamerule);

            // Ignore any name that cannot be resolved to a Bukkit GameRule.
            if (rule == null) {
                continue;
            }

            String value = world.getGameRuleValue(rule).toString();
            copiedRules.add(new CopyGamerules(gamerule, value));
        }

        PlayerSessionManager.setCopiedGamerules(player, world, copiedRules);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    // Handles navigation and utility buttons that are not gamerule entries themselves.
    private void EssentialsButtons(InventoryClickEvent e, Player p, World selectedWorld) {
        ItemStack clickedItem = e.getCurrentItem();

        // Ignore empty slots or items without metadata/display names.
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = clickedItem.getItemMeta();

        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return;
        }

        String displayName = itemMeta.getDisplayName();

        // Utility actions also require an active selected world.
        if (selectedWorld == null) {
            sendConfiguredMessage(p, "noWorldSelected", "&cNo world selected.");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            p.openInventory(GUI.guiBuilder(p));
            return;
        }

        // Return from the gamerule list to the world-selection GUI.
        if (displayName.equals(ChatColor.RED + "Get Back in World Selection.")) {
            p.openInventory(GUI.guiBuilder(p));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
            return;
        }

        // Close the GUI completely.
        if (displayName.equals(ChatColor.RED + "EXIT")) {
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
            return;
        }

        // Navigate from page 1 to page 2.
        if (displayName.equals(ChatColor.RED + "Next page with Gamerules.")) {
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            p.openInventory(GUI.gameruleSetterGuiPage2(p));
            return;
        }

        // Navigate from page 2 back to page 1.
        if (displayName.equals(ChatColor.RED + "Previous page with Gamerules.")) {
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            p.openInventory(GUI.gameruleSetterGui(p, selectedWorld));
            return;
        }

        // Restore all gamerules in this world to their Minecraft defaults.
        if (displayName.equals(ChatColor.RED + "Reset all " + ChatColor.YELLOW + "Gamerules")) {
            resetGamerules(selectedWorld);
            p.openInventory(GUI.gameruleSetterGui(p, selectedWorld));
            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
            return;
        }

        // Copy every gamerule and its current value from the selected world into the player's session.
        if (displayName.equals(ChatColor.DARK_BLUE + "Copy " + ChatColor.YELLOW + "Gamerules")) {
            copyGamerules(p, selectedWorld);
            p.openInventory(GUI.guiBuilder(p));
            p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1);
            return;
        }

        // Paste the previously copied gamerule values into the currently selected world.
        if (displayName.equals(ChatColor.DARK_RED + "Paste " + ChatColor.YELLOW + "Gamerules")) {
            // Do not attempt a paste until the player has copied gamerules from another world.
            if (!PlayerSessionManager.hasCopiedGamerules(p)) {
                p.sendMessage(getPlugin().getPluginPrefix()
                        + ChatColor.RED + "" + ChatColor.BOLD + "You didn't copy any world.");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            for (CopyGamerules copiedGamerule : PlayerSessionManager.getCopiedGamerules(p)) {

                GameRule gamerule = GameRule.getByName(copiedGamerule.getGameRule());

                // Skip copied entries that no longer resolve to a valid gamerule.
                if (gamerule == null) {
                    continue;
                }

                String value = copiedGamerule.getValue();

                // Restore each copied value using the type expected by the gamerule.
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

            // Refresh the GUI and notify the player which worlds were involved in the copy/paste operation.
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
        // Clear both a pending chat edit and the normal GUI session when a player leaves the server.
        pendingValueInputs.remove(event.getPlayer().getUniqueId());
        PlayerSessionManager.clear(event.getPlayer());
    }

    // Immutable snapshot of the context required while waiting for an integer value in chat.
    private record PendingValueInput(String gamerule, World world, int page, int invalidAttempts) {
        // Records another invalid attempt without mutating the existing pending-input snapshot.
        private PendingValueInput withInvalidAttempts(int attempts) {
            return new PendingValueInput(gamerule, world, page, attempts);
        }
    }
}
