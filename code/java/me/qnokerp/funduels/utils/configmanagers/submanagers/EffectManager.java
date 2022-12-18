package me.qnokerp.funduels.utils.configmanagers.submanagers;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectManager {
    private PotionEffectType effectType;
    private int duration;
    private  int amplifier;
    private boolean particles;
    private final PotionEffect effect;


    public EffectManager(Object effectName, int duration, int amplifier, boolean particles) {


        if(!(effectName instanceof Boolean)) {
            this.effectType = PotionEffectType.getByName((String) effectName);
            this.duration = duration * 20;
            this.amplifier = amplifier;
            this.particles = particles;
            assert this.effectType != null;
            this.effect = new PotionEffect(this.effectType, this.duration, this.amplifier, this.particles, false);
        } else {
            this.effectType = PotionEffectType.LUCK;
            this.duration = 0;
            this.amplifier = 0;
            this.particles = false;
            this.effect = new PotionEffect(this.effectType, this.duration, this.amplifier, false, false);
        }

    }

    public PotionEffect getPotionEffect() {
        return this.effect;
    }

}

