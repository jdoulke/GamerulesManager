package me.ted2001.gamerules_manager_remake;

import me.ted2001.gamerules_manager_remake.listeners.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class GamerulesManager extends JavaPlugin {

    private static GamerulesManager plugin;
    public static String Serverversion;

    @Override
    public void onEnable() {
        Serverversion = Bukkit.getBukkitVersion();
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("gamerule").setExecutor(new GuiCommand());
        TabCompleter tc = new Tabcompleter();
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

}
