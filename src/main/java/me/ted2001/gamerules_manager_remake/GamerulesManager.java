package me.ted2001.gamerules_manager_remake;

import me.ted2001.gamerules_manager_remake.Commands.CommandCompleter;
import me.ted2001.gamerules_manager_remake.Commands.GuiCommand;
import me.ted2001.gamerules_manager_remake.Listeners.UpdateChecker;
import me.ted2001.gamerules_manager_remake.Listeners.WorldSelectorListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class GamerulesManager extends JavaPlugin {

    private static GamerulesManager plugin;
    public static String serverVersion;

    @Override
    public void onEnable() {
        serverVersion = Bukkit.getBukkitVersion();
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("gamerule").setExecutor(new GuiCommand());
        TabCompleter tc = new CommandCompleter();
        Objects.requireNonNull(getPlugin().getCommand("gamerule")).setTabCompleter(tc);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSelectorListener(), this);
        new UpdateChecker(this, 102215).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else if (Double.parseDouble(this.getDescription().getVersion()) < Double.parseDouble(version)){
                getLogger().info("There is a new update available.");
            } else {
                getLogger().info("You are using a version newer than spigot uploaded version.");
            }
        });
        Metrics metrics = new Metrics(this,15346);

    }

    public static GamerulesManager getPlugin() {
        return plugin;
    }

}
