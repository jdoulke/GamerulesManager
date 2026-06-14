package me.ted2001.gamerulesmanager.Commands;

import me.ted2001.gamerulesmanager.GUI;
import me.ted2001.gamerulesmanager.Utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.ted2001.gamerulesmanager.GamerulesManager.getPlugin;

public class GuiCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             String[] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be executed from a player.");
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("gamerulemanager.use")) {
                player.sendMessage(ColorUtils.translateColorCodes(
                        getPlugin().getConfig().getString(
                                "noPermission",
                                "&cYou don't have permission to use this command."
                        )
                ));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return true;
            }

            player.openInventory(GUI.guiBuilder(player));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_WORK_WEAPONSMITH, 1, 1);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("gamerulemanager.reload")) {
                sender.sendMessage(ColorUtils.translateColorCodes(
                        getPlugin().getConfig().getString(
                                "noPermission",
                                "&cYou don't have permission to use this command."
                        )
                ));
                return true;
            }

            reloadCommand();
            sender.sendMessage(ChatColor.YELLOW + "You reloaded config.yml.");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Usage: /" + label + " [reload]");
        return true;
    }

    private void reloadCommand() {
        getPlugin().reloadConfig();
        getPlugin().reloadPlugin();
    }
}