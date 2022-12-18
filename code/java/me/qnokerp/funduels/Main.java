package me.qnokerp.funduels;

import me.qnokerp.funduels.commands.DuelsCommands;
import me.qnokerp.funduels.commands.onTabCompleter;
import me.qnokerp.funduels.events.EventsListener;
import me.qnokerp.funduels.utils.configmanagers.ConfigDefaultManager;
import me.qnokerp.funduels.utils.configmanagers.submanagers.ConfigInits;
import me.qnokerp.funduels.utils.configmanagers.ConfigLocationsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static me.qnokerp.funduels.utils.FunDuelsUtils.listToHashmap;

public final class Main extends JavaPlugin {
    private static Main instance;
    private ConfigDefaultManager configData;
    private ConfigLocationsManager locationsData;
    public static HashMap<Player, Location> beforeLocation = new HashMap<Player, Location>();
    public static HashMap<Player, String> duelists = new HashMap<Player, String>();
    public static HashMap<String, Boolean> locations;
    public static Map<Player, BossBar> bossBars = new HashMap<Player, BossBar>();
    public static Map<Player, BukkitTask> tasks = new HashMap<Player, BukkitTask>();
    public static List<Player> winners = new ArrayList<Player>();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        configData = new ConfigDefaultManager("config.yml");
        locationsData = new ConfigLocationsManager("locations.yml");

        ConfigInits.configElementsInit();
        ConfigInits.configSoundInit();
        ConfigInits.configEffectInit();
        ConfigLocationsManager.saveDefaultLocations();

        locations = listToHashmap( new ArrayList<>(Main.getLocationsData().getConfig().getConfigurationSection("locations").getKeys(false)));

        Bukkit.getPluginManager().registerEvents(new EventsListener(), this);
        Objects.requireNonNull(getServer().getPluginCommand("duel")).setExecutor(new DuelsCommands());
        Objects.requireNonNull(getServer().getPluginCommand("duel")).setTabCompleter(new onTabCompleter());

        onTabCompleter.addLocPermission = Main.getConfigData().getConfig().getString("permission");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static Main getInstance() {
        return instance;
    }

    public static ConfigDefaultManager getConfigData() { return instance.configData; }

    public static ConfigLocationsManager getLocationsData() { return instance.locationsData; }
}
