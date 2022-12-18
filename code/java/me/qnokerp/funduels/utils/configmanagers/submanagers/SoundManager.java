package me.qnokerp.funduels.utils.configmanagers.submanagers;

import org.bukkit.Sound;

public class SoundManager {
    private final Sound sound;
    private final float volume;

    private final float pitch;


    public SoundManager(String soundName, float volume, float pitch) {
        this.sound = Sound.valueOf(soundName);
        this.volume = volume;
        this.pitch = pitch;
    }


    public Sound getSound() {
        return this.sound;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

}
