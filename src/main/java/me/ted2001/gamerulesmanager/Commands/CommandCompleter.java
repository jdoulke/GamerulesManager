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
    List<String> results = new ArrayList<>();
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length ==1){
            if(command.getLabel().equalsIgnoreCase("gamerule")){
                if(sender.hasPermission("gamerulemanager.reload"))
                    results.add("reload");
            }
        }
        return sortedResults(args[0]);

    }

    public List < String > sortedResults(String arg) {
        final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(arg, results, completions);
        Collections.sort(completions);
        results.clear();
        results.addAll(completions);
        return results;
    }
}
