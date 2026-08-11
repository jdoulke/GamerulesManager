package me.ted2001.gamerulesmanager.Listeners;

import me.ted2001.gamerulesmanager.GUI;
import me.ted2001.gamerulesmanager.Utils.GuiInventoryHolder;
import me.ted2001.gamerulesmanager.Utils.GuiItemData;
import me.ted2001.gamerulesmanager.Utils.PlayerSessionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WorldSelectorListener implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        if (!(e.getView().getTopInventory().getHolder() instanceof GuiInventoryHolder holder)
                || holder.getMenuType() != GuiInventoryHolder.MenuType.WORLD_SELECTOR) {
            return;
        }

        // Prevent items from being moved while the world selector is open.
        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        String action = GuiItemData.getAction(clickedItem);
        if (action == null) {
            return;
        }

        // Move to the next world-selector page while keeping the current page in the player's session.
        if (action.equals("next_world_page")) {
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            player.openInventory(GUI.guiBuilder(player, holder.getPage() + 1));
            return;
        }

        // Move back one page. guiBuilder also clamps the requested page if the world list changed meanwhile.
        if (action.equals("previous_world_page")) {
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
            player.openInventory(GUI.guiBuilder(player, holder.getPage() - 1));
            return;
        }

        if (action.equals("exit")) {
            PlayerSessionManager.clear(player);
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
            return;
        }

        if (!action.equals("world")) {
            return;
        }

        // The world name is stored internally on the GUI item, so visible names can change safely later.
        String worldName = GuiItemData.getValue(clickedItem);
        if (worldName == null) {
            return;
        }

        World world = Bukkit.getServer().getWorld(worldName);

        if (world == null) {
            player.sendMessage(ChatColor.RED + "World not found.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        // Remember which selector page this world came from so the Gamerule GUI Back button returns here.
        PlayerSessionManager.setWorldSelectorPage(player, holder.getPage());
        PlayerSessionManager.setSelectedWorld(player, world);

        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
        player.openInventory(GUI.gameruleSetterGui(player, world));
    }
}
