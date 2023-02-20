package me.ted2001.gamerulesmanager;

import me.ted2001.gamerulesmanager.Commands.CommandCompleter;
import me.ted2001.gamerulesmanager.Commands.GuiCommand;
import me.ted2001.gamerulesmanager.Listeners.GUIListener;
import me.ted2001.gamerulesmanager.Listeners.UpdateChecker;
import me.ted2001.gamerulesmanager.Listeners.WorldSelectorListener;
import me.ted2001.gamerulesmanager.Utils.GameruleDisplayItem;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public final class GamerulesManager extends JavaPlugin {

    private static GamerulesManager plugin;
    public static String serverVersion;
    public static HashMap<GameRule<?>, ItemStack> gamerulesDisplayItems = new HashMap<>();

    @Override
    public void onEnable() {
        //get server Version
        String rawVersion = Bukkit.getBukkitVersion();
        serverVersion = new String(new char[]{rawVersion.charAt(2), rawVersion.charAt(3)});
        //get instance of the plugin
        plugin = this;
        //get the config.yml
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        //set gamerules display items
        GameRule<?>[] gamerules = GameRule.values();
        GameruleDisplayItem displayItem = new GameruleDisplayItem();
        for (GameRule<?> gamerule : gamerules) {
            gamerulesDisplayItems.put(gamerule, displayItem.gameruleDisplayItem(gamerule.getName()));
        }
        //register command and listeners
        Objects.requireNonNull(getCommand("gamerule")).setExecutor(new GuiCommand());
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSelectorListener(), this);
        //set the tab completer for gamerule command
        TabCompleter tc = new CommandCompleter();
        Objects.requireNonNull(getPlugin().getCommand("gamerule")).setTabCompleter(tc);
        //create the update checker
        new UpdateChecker(this, 102215).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else if (Double.parseDouble(this.getDescription().getVersion()) < Double.parseDouble(version)){
                getLogger().info("There is a new update available.");
            }
        });
        //metrics for bstats for the plugin
        Metrics metrics = new Metrics(this,15346);

    }

    public static GamerulesManager getPlugin() {
        return plugin;
    }

}
