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

    private static final int MAX_INVALID_ATTEMPTS = 2;
    private final Map<UUID, PendingValueInput> pendingValueInputs = new ConcurrentHashMap<>();

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {

        try {
            if (e.getView().getTitle().contains(ChatColor.DARK_PURPLE + "Gamerule Manager" + ChatColor.AQUA + " ")) {
                e.setCancelled(true);
                Inventory gui = e.getClickedInventory();
                Player p = (Player) e.getWhoClicked();
                if (e.getCurrentItem() == null)
                    return;
                World selectedWorld = PlayerSessionManager.getSelectedWorld(p);

                if (selectedWorld == null) {
                    sendConfiguredMessage(p, "noWorldSelected", "&cNo world selected.");
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
                e.setCancelled(true);
                Inventory gui = e.getClickedInventory();
                Player p = (Player) e.getWhoClicked();
                World selectedWorld = PlayerSessionManager.getSelectedWorld(p);

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
            sendConfiguredMessage(p, "noWorldSelected", "&cNo world selected.");
            p.openInventory(GUI.guiBuilder(p));
            return;
        }

        GameRule<?> rule = GameRule.getByName(gamerule);

        if (rule == null) {
            sendConfiguredMessage(p, "integerInputUnknownRule", "&cUnknown gamerule: &f%gamerule%", gamerule);
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        if (rule.getType() != Integer.class) {
            sendConfiguredMessage(p, "integerInputNotInteger", "&cThis gamerule is not an integer gamerule.");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        Integer defaultValue = world.getGameRuleDefault((GameRule<Integer>) rule);

        pendingValueInputs.put(p.getUniqueId(), new PendingValueInput(gamerule, world, page, 0));
        p.closeInventory();
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

                sendConfiguredMessage(player, "integerInputCancelled", "&eGamerule change cancelled.");
                reopenGamerulePage(player, pendingInput);
            });
            return;
        }

        final int value;
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            int invalidAttempts = pendingInput.invalidAttempts() + 1;

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
            sendConfiguredMessage(player, "integerInputRuleUnavailable", "&cThat gamerule is no longer available.");
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

    private void sendConfiguredMessage(Player player, String path, String fallback) {
        String message = getPlugin().getConfig().getString(path, fallback);
        player.sendMessage(getPlugin().getPluginPrefix() + ColorUtils.translateColorCodes(message));
    }

    private void sendConfiguredMessage(Player player, String path, String fallback, String gamerule) {
        String message = getPlugin().getConfig().getString(path, fallback).replace("%gamerule%", gamerule);
        player.sendMessage(getPlugin().getPluginPrefix() + ColorUtils.translateColorCodes(message));
    }

    private void sendConfiguredMessage(Player player, String path, String fallback, String gamerule, String defaultValue) {
        String message = getPlugin().getConfig().getString(path, fallback)
                .replace("%gamerule%", gamerule)
                .replace("%default_value%", defaultValue);
        player.sendMessage(getPlugin().getPluginPrefix() + ColorUtils.translateColorCodes(message));
    }

    private void booleanGameruleSet(GameRule<Boolean> gamerule, boolean value, World world, Player p){
        world.setGameRule(gamerule,!value);
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
            sendConfiguredMessage(p, "noWorldSelected", "&cNo world selected.");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            p.openInventory(GUI.guiBuilder(p));
            return;
        }

        if (displayName.equals(ChatColor.RED + "Get Back in World Selection.")) {
            p.openInventory(GUI.guiBuilder(p));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
            return;
        }

        if (displayName.equals(ChatColor.RED + "EXIT")) {
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
            return;
        }

        if (displayName.equals(ChatColor.RED + "Next page with Gamerules.")) {
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            p.openInventory(GUI.gameruleSetterGuiPage2(p));
            return;
        }

        if (displayName.equals(ChatColor.RED + "Previous page with Gamerules.")) {
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            p.openInventory(GUI.gameruleSetterGui(p, selectedWorld));
            return;
        }

        if (displayName.equals(ChatColor.RED + "Reset all " + ChatColor.YELLOW + "Gamerules")) {
            resetGamerules(selectedWorld);
            p.openInventory(GUI.gameruleSetterGui(p, selectedWorld));
            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
            return;
        }

        if (displayName.equals(ChatColor.DARK_BLUE + "Copy " + ChatColor.YELLOW + "Gamerules")) {
            copyGamerules(p, selectedWorld);
            p.openInventory(GUI.guiBuilder(p));
            p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1);
            return;
        }

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

    private record PendingValueInput(String gamerule, World world, int page, int invalidAttempts) {
        private PendingValueInput withInvalidAttempts(int attempts) {
            return new PendingValueInput(gamerule, world, page, attempts);
        }
    }
}
