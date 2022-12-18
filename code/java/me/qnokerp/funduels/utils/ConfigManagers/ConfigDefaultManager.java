package me.qnokerp.funduels.utils.configmanagers;

import me.qnokerp.funduels.Main;
import me.qnokerp.funduels.utils.configmanagers.submanagers.ConfigInits;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigDefaultManager {


    private File file;
    private FileConfiguration config;

    public ConfigDefaultManager(String name) {
        file = new File(Main.getInstance().getDataFolder(), name);
        try {
            if(!file.exists() && !file.createNewFile()) throw new IOException();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать " + name);
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
            throw new RuntimeException("Не удалось сохранить конфиг");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
        ConfigInits.configSoundInit();
        ConfigInits.configElementsInit();
    }




}