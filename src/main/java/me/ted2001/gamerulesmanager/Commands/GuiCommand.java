package me.ted2001.gamerulesmanager.Commands;

import me.ted2001.gamerulesmanager.GUI;
import me.ted2001.gamerulesmanager.Utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static me.ted2001.gamerulesmanager.GamerulesManager.serverVersion;
import static me.ted2001.gamerulesmanager.GamerulesManager.getPlugin;

public class GuiCommand implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            //get player
            Player p = (Player) sender;
            if (args.length == 0) {
                if (p.hasPermission("gamerulemanager.use")) {
                    p.openInventory(GUI.guiBuilder(p));
                    if (Integer.parseInt(serverVersion) >= 14)
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_WORK_WEAPONSMITH, 1, 1);
                    else
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                } else
                    p.sendMessage(ColorUtils.translateColorCodes(Objects.requireNonNull(getPlugin().getConfig().getString("no-permission"))));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            } else if (args.length == 1) {
                String arg0 = args[0];
                if (arg0.equalsIgnoreCase("reload")) {
                    if (p.hasPermission("gamerulemanager.reload")) {
                        reloadCommand();
                        p.sendMessage(ChatColor.YELLOW + "You reload config.yml.");
                    }
                    else
                        p.sendMessage(ColorUtils.translateColorCodes(Objects.requireNonNull(getPlugin().getConfig().getString("no-permission"))));

                }
            }
            return false;
        }else {
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    if(sender.hasPermission("gamerulemanager.reload")) {
                        reloadCommand();
                        sender.sendMessage(ChatColor.YELLOW + "You reload config.yml.");
                    }
                }
            }else
                sender.sendMessage(ChatColor.RED + "This command can only be executed from a player.");
        }
        return false;
    }

    private void reloadCommand(){

        getPlugin().reloadConfig();
        getPlugin().reloadPluginPrefix();
    }
}
