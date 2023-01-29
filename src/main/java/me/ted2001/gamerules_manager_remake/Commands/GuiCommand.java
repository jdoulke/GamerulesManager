package me.ted2001.gamerules_manager_remake.Commands;

import me.ted2001.gamerules_manager_remake.GUI;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.ted2001.gamerules_manager_remake.GamerulesManager.serverVersion;
import static me.ted2001.gamerules_manager_remake.GamerulesManager.getPlugin;

public class GuiCommand implements CommandExecutor{
    GUI gui = new GUI();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            //get player
            Player p = (Player) sender;
            if (args.length == 0) {
                if (p.hasPermission("gamerulemanager.use")) {
                    p.openInventory(gui.guiBuilder(p));
                    if (Integer.parseInt(serverVersion) >= 14)
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_WORK_WEAPONSMITH, 1, 1);
                    else
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                } else
                    p.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            } else if (args.length == 1) {
                String arg0 = args[0];
                if (arg0.equalsIgnoreCase("reload")) {
                    if (p.hasPermission("gamerulemanager.reload")) {
                        reloadCommand();
                        p.sendMessage(ChatColor.YELLOW + "You reload config.yml.");
                    }
                    else
                        p.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                }
            }
            return false;
        }
        return false;
    }

    private void reloadCommand(){ getPlugin().reloadConfig();}
}
