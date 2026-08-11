package me.ted2001.gamerulesmanager.Listeners;

import me.ted2001.gamerulesmanager.GUI;
import me.ted2001.gamerulesmanager.Utils.ColorUtils;
import me.ted2001.gamerulesmanager.Utils.CopyGamerules;
import me.ted2001.gamerulesmanager.Utils.GameRuleRegistryUtil;
import me.ted2001.gamerulesmanager.Utils.GameruleCreator;
import me.ted2001.gamerulesmanager.Utils.GuiInventoryHolder;
import me.ted2001.gamerulesmanager.Utils.GuiItemData;
import me.ted2001.gamerulesmanager.Utils.PlayerSessionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static me.ted2001.gamerulesmanager.GamerulesManager.getPlugin;
import static org.bukkit.Bukkit.getServer;

@SuppressWarnings({"ConstantConditions", "rawtypes", "unchecked"})
public class GUIListener implements Listener {

    // Number of invalid chat inputs allowed before the pending gamerule edit is cancelled.
    private static final int MAX_INVALID_ATTEMPTS = 2;

    // Stores the integer gamerule edit currently waiting for chat input from each player.
    private final Map<UUID, PendingValueInput> pendingValueInputs = new ConcurrentHashMap<>();

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        if (!(e.getView().getTopInventory().getHolder() instanceof GuiInventoryHolder holder)) {
            return;
        }

        if (holder.getMenuType() != GuiInventoryHolder.MenuType.GAMERULE_PAGE_1
                && holder.getMenuType() != GuiInventoryHolder.MenuType.GAMERULE_PAGE_2) {
            return;
        }

        // Prevent players from moving items while a GamerulesManager GUI is open.
        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        World selectedWorld = PlayerSessionManager.getSelectedWorld(p);
        if (selectedWorld == null) {
            p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "No world selected.");
            p.openInventory(GUI.guiBuilder(p));
            return;
        }

        String action = GuiItemData.getAction(clickedItem);
        if (action == null) {
            return;
        }

        int page = holder.getMenuType() == GuiInventoryHolder.MenuType.GAMERULE_PAGE_2 ? 2 : 1;

        if (action.equals("gamerule")) {
            handleGameruleClick(e, p, selectedWorld, page, clickedItem);
            return;
        }

        handleActionButton(action, p, selectedWorld);
    }

    // Handles a gamerule item independently from its visible display name.
    private void handleGameruleClick(InventoryClickEvent e, Player player, World selectedWorld, int page, ItemStack clickedItem) {
        String gameruleName = GuiItemData.getValue(clickedItem);
        if (gameruleName == null) {
            return;
        }

        GameRule<?> gamerule = GameRuleRegistryUtil.getByName(gameruleName);
        if (gamerule == null) {
            player.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "Unknown gamerule: " + ChatColor.WHITE + gameruleName);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        if (gamerule.getType() == Boolean.class) {
            GameRule<Boolean> booleanRule = (GameRule<Boolean>) gamerule;
            booleanGameruleSet(booleanRule, selectedWorld.getGameRuleValue(booleanRule), selectedWorld, player);

            // Refresh only the clicked GUI slot so its lore immediately shows the new value.
            int rawSlot = e.getRawSlot();
            Inventory topInventory = e.getView().getTopInventory();
            if (rawSlot >= 0 && rawSlot < topInventory.getSize()) {
                topInventory.setItem(rawSlot, new GameruleCreator().GamerulesCreator(gameruleName, selectedWorld));
            }
            return;
        }

        // Integer gamerules are edited through the player's next chat message.
        valueReceiver(player, gameruleName, page);
    }

    // Starts the chat-based input flow for an integer gamerule.
    private void valueReceiver(Player p, String gamerule, int page) {
        World world = PlayerSessionManager.getSelectedWorld(p);

        // The selected world is stored because the GUI closes while the player types the value.
        if (world == null) {
            p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "No world selected.");
            p.openInventory(GUI.guiBuilder(p));
            return;
        }

        GameRule<?> rule = GameRuleRegistryUtil.getByName(gamerule);

        // Protect against a gamerule name that is no longer available in the current server version.
        if (rule == null) {
            p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "Unknown gamerule: " + ChatColor.WHITE + gamerule);
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Chat input is only required for integer gamerules; booleans are handled directly by the GUI click.
        if (rule.getType() != Integer.class) {
            p.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "This gamerule is not an integer gamerule.");
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
                "valuePrompt",
                "&eType the new integer value for &b%gamerule%&e in chat. &7Default value: &f%default_value%&e.",
                gamerule,
                String.valueOf(defaultValue)
        );
        sendConfiguredMessage(p, "cancelHint", "&7Type &ccancel&7 to go back without changing it.");
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

                sendConfiguredMessage(player, "changeCancelled", "&eGamerule change cancelled.");
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

                    sendConfiguredMessage(player, "tooManyInvalid", "&cToo many invalid attempts. Gamerule change cancelled.");
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

                sendConfiguredMessage(player, "invalidValue", "&eYou didn't type an &cinteger number&e. Try again or type &ccancel&e.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            });
            return;
        }

        // A valid value completes the input flow. Bukkit world/GUI operations are scheduled on the main thread.
        pendingValueInputs.remove(player.getUniqueId());
        Bukkit.getScheduler().runTask(getPlugin(), () -> applyIntegerGamerule(player, pendingInput, value));
    }

    // Applies the parsed integer gamerule value on the main server thread.
    private void applyIntegerGamerule(Player player, PendingValueInput pendingInput, int value) {
        // The scheduled task may run after the player disconnects, so do not reopen a GUI for an offline player.
        if (!player.isOnline()) {
            return;
        }

        // Resolve the gamerule again before applying it instead of assuming the stored name is still valid.
        GameRule<?> rule = GameRuleRegistryUtil.getByName(pendingInput.gamerule());
        if (rule == null || rule.getType() != Integer.class) {
            player.sendMessage(getPlugin().getPluginPrefix() + ChatColor.RED + "That gamerule is no longer available.");
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

    // Sends the value-input prompt and replaces both %gamerule% and %default_value% placeholders.
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

    // Restore every registered gamerule in the selected world to Minecraft's default value.
    private void resetGamerules(World world){
        for (GameRule<?> gamerule : Registry.GAME_RULE) {
            if (gamerule.getType() == Boolean.class) {
                GameRule<Boolean> booleanRule = (GameRule<Boolean>) gamerule;
                world.setGameRule(booleanRule, world.getGameRuleDefault(booleanRule));
            } else if (gamerule.getType() == Integer.class) {
                GameRule<Integer> integerRule = (GameRule<Integer>) gamerule;
                world.setGameRule(integerRule, world.getGameRuleDefault(integerRule));
            }
        }
    }

    // Capture all gamerule values from the current world so they can later be pasted into another world.
    private void copyGamerules(Player player, World world) {
        List<CopyGamerules> copiedRules = new ArrayList<>();

        for (GameRule<?> gamerule : GameRuleRegistryUtil.getSortedGameRules()) {
            Object currentValue = world.getGameRuleValue(gamerule);
            if (currentValue != null) {
                copiedRules.add(new CopyGamerules(GameRuleRegistryUtil.getName(gamerule), currentValue.toString()));
            }
        }

        PlayerSessionManager.setCopiedGamerules(player, world, copiedRules);
    }

    // Handles navigation and utility buttons using internal action IDs rather than visible display names.
    private void handleActionButton(String action, Player p, World selectedWorld) {
        switch (action) {
            case "back" -> {
                p.openInventory(GUI.guiBuilder(p));
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
            }
            case "exit" -> {
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
            }
            case "next_page" -> {
                p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                p.openInventory(GUI.gameruleSetterGuiPage2(p));
            }
            case "previous_page" -> {
                p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                p.openInventory(GUI.gameruleSetterGui(p, selectedWorld));
            }
            case "reset" -> {
                resetGamerules(selectedWorld);
                p.openInventory(GUI.gameruleSetterGui(p, selectedWorld));
                p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
            }
            case "copy" -> {
                copyGamerules(p, selectedWorld);
                p.openInventory(GUI.guiBuilder(p));
                p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1);
            }
            case "paste" -> pasteGamerules(p, selectedWorld);
            default -> {
                // Unknown internal GUI action: ignore it safely.
            }
        }
    }

    // Pastes the gamerule snapshot stored in this player's session into the selected world.
    private void pasteGamerules(Player p, World selectedWorld) {
        if (!PlayerSessionManager.hasCopiedGamerules(p)) {
            p.sendMessage(getPlugin().getPluginPrefix()
                    + ChatColor.RED + "" + ChatColor.BOLD + "You didn't copy any world.");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        for (CopyGamerules copiedGamerule : PlayerSessionManager.getCopiedGamerules(p)) {
            GameRule gamerule = GameRuleRegistryUtil.getByName(copiedGamerule.getGameRule());

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

        World copiedFrom = PlayerSessionManager.getCopiedFromWorld(p);
        if (copiedFrom != null) {
            p.sendMessage(getPlugin().getPluginPrefix()
                    + ChatColor.YELLOW + "You copied all "
                    + ChatColor.AQUA + "Gamerules "
                    + ChatColor.YELLOW + "from "
                    + ChatColor.BLUE + copiedFrom.getName()
                    + ChatColor.YELLOW + " to "
                    + ChatColor.RED + selectedWorld.getName()
                    + ChatColor.YELLOW + ".");
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
