package me.qnokerp.funduels.utils.configmanagers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import me.qnokerp.funduels.Main;

import java.io.File;
import java.io.IOException;

public class ConfigLocationsManager {
    private File file;
    private static FileConfiguration config;

    public ConfigLocationsManager(String name) {
        file = new File(Main.getInstance().getDataFolder(), name);
        try {
            if(!file.exists() && !file.createNewFile()) throw new IOException();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать конфиг файл");
        }

        config = YamlConfiguration.loadConfiguration(file);

    }
    public FileConfiguration getConfig() {
        return config;

    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить конфиг файл");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void saveDefaultLocations() {

        if(Main.getLocationsData().getConfig().getKeys(false).isEmpty()) {

            Main.getLocationsData().getConfig().set("locations.arena.spawn1", "Location");
            Main.getLocationsData().getConfig().set("locations.arena.spawn2", "Location");

            Main.getLocationsData().save();
        }
    }

}