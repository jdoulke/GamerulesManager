package me.ted2001.gamerulesmanager.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args) {
        if (args.length != 1 || !command.getName().equalsIgnoreCase("gamerule")) {
            return Collections.emptyList();
        }

        List<String> options = new ArrayList<>();
        if (sender.hasPermission("gamerulemanager.reload")) {
            options.add("reload");
        }

        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], options, completions);
        Collections.sort(completions);
        return completions;
    }
}
