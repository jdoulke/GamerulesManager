package me.ted2001.gamerulesmanager.Listeners;

import me.ted2001.gamerulesmanager.GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

import static me.ted2001.gamerulesmanager.GamerulesManager.serverVersion;

public class WorldSelectorListener implements Listener {
    public static World WorldSelected;



    public void setWorldSelected(World worldSelected) {
        WorldSelected = worldSelected;
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        try {
            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.AQUA + "" + ChatColor.BOLD + "World Selector")) {
                if (e.getCurrentItem() == null)
                    return;
                e.setCancelled(true);
                Player p = (Player) e.getWhoClicked();
                String world_name;
                String clickItem = e.getCurrentItem().getType().toString();
                if(clickItem.equalsIgnoreCase("GRASS_BLOCK") || clickItem.equalsIgnoreCase("NETHERRACK") || clickItem.equalsIgnoreCase("END_STONE")) {
                    world_name = ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName());
                    World world = Bukkit.getServer().getWorld(world_name);
                    setWorldSelected(world);
                    if(world != null) {
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
                        p.openInventory(GUI.gameruleSetterGui(p, world));
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "World not found.");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    }
                }
                if(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(ChatColor.RED + "EXIT")) {
                    p.closeInventory();
                    if(Integer.parseInt(serverVersion) >= 14)
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1,1);
                    else
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1,1);
                }

            }
        } catch (NullPointerException ignored) {}
    }
}
