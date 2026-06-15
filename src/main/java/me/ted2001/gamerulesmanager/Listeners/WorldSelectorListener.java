package me.ted2001.gamerulesmanager.Listeners;

import me.ted2001.gamerulesmanager.GUI;
import me.ted2001.gamerulesmanager.Utils.PlayerSessionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WorldSelectorListener implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(ChatColor.AQUA + "" + ChatColor.BOLD + "World Selector")) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getWhoClicked();

        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta()) {
            return;
        }

        ItemMeta itemMeta = clickedItem.getItemMeta();

        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return;
        }

        String displayName = itemMeta.getDisplayName();

        if (displayName.equals(ChatColor.RED + "EXIT")) {
            PlayerSessionManager.clear(player);
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
            return;
        }

        Material clickedType = clickedItem.getType();

        if (clickedType != Material.GRASS_BLOCK
                && clickedType != Material.NETHERRACK
                && clickedType != Material.END_STONE) {
            return;
        }

        String worldName = ChatColor.stripColor(displayName);
        World world = Bukkit.getServer().getWorld(worldName);

        if (world == null) {
            player.sendMessage(ChatColor.RED + "World not found.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }

        PlayerSessionManager.setSelectedWorld(player, world);

        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
        player.openInventory(GUI.gameruleSetterGui(player, world));
    }
}