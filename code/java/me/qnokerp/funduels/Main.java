package me.qnokerp.funduels;

import me.qnokerp.funduels.commands.DuelsCommands;
import me.qnokerp.funduels.events.EventsListener;
import me.qnokerp.funduels.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import static me.qnokerp.funduels.utils.FunDuelsUtils.listToHashmap;

public final class Main extends JavaPlugin {
    private static Main instance;
    private ConfigManager configData;
    private ConfigManager locationsData;
    public static HashMap<Player, Location> beforeLocation = new HashMap<Player, Location>();
    public static HashMap<Player, String> duelists = new HashMap<Player, String>();

    public static HashMap<String, Boolean> locations;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        configData = new ConfigManager("config.yml");

        locationsData = new ConfigManager("locations.yml");
        ConfigManager.saveDefaultLocations();

        locations = listToHashmap( new ArrayList<>(Main.getLocationsData().getConfig().getConfigurationSection("locations").getKeys(false)));

        Bukkit.getPluginManager().registerEvents(new EventsListener(), this);
        getServer().getPluginCommand("duel").setExecutor(new DuelsCommands());
        getServer().getPluginCommand("duel").setTabCompleter(new DuelsCommands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static Main getInstance() {
        return instance;
    }

    public static ConfigManager getConfigData() { return instance.configData; }

    public static ConfigManager getLocationsData() { return instance.locationsData; }
}
