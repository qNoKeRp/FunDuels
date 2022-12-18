package me.qnokerp.funduels.utils.configmanagers.submanagers;

import me.qnokerp.funduels.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import java.util.Objects;

public class ConfigInits {
    private static final FileConfiguration config = Main.getConfigData().getConfig();

    public static int delayFinishTp = 0;
    public static double delayStartTP = 0;
    public static int figthTime = 0;
    public static String perm;
    public static int duelQueryTime = 0;

    public static void configElementsInit() {
        perm = config.getString("permission");
        delayFinishTp = config.getInt("delay_finish_tp");
        delayStartTP = config.getInt("delay_start_tp", (int) 100d);
        figthTime = config.getInt("fight_time");
        duelQueryTime = config.getInt("duel_query_time");
    }

    public static SoundManager getQuerySound;
    public static SoundManager acceptQuerySound;
    public static SoundManager denyQuerySound;
    public static SoundManager arenaTpSound;
    public static SoundManager winSound;
    public static SoundManager loseSound;
    public static SoundManager drawSound;
    public static SoundManager fromArenaTpSound;

    public static void configSoundInit() {
        getQuerySound = new SoundManager(config.getString("sounds.getQuerySound.sound"),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.fromArenaTpSound.volume"))),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.fromArenaTpSound.pitch"))));

        acceptQuerySound = new SoundManager(config.getString("sounds.acceptQuerySound.sound"),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.acceptQuerySound.volume"))),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.acceptQuerySound.pitch"))));

        denyQuerySound = new SoundManager(config.getString("sounds.denyQuerySound.sound"),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.denyQuerySound.volume"))),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.denyQuerySound.pitch"))));

        arenaTpSound = new SoundManager(config.getString("sounds.arenaTpSound.sound"),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.arenaTpSound.volume"))),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.arenaTpSound.pitch"))));

        winSound = new SoundManager(config.getString("sounds.winSound.sound"),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.winSound.volume"))),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.winSound.pitch"))));

        loseSound = new SoundManager(config.getString("sounds.loseSound.sound"),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.loseSound.volume"))),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.loseSound.pitch"))));

        drawSound = new SoundManager(config.getString("sounds.drawSound.sound"),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.drawSound.volume"))),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.drawSound.pitch"))));

        fromArenaTpSound = new SoundManager(config.getString("sounds.fromArenaTpSound.sound"),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.fromArenaTpSound.volume"))),
                Float.parseFloat(Objects.requireNonNull(config.getString("sounds.fromArenaTpSound.pitch"))));

    }

    public static PotionEffect arenaTpEffect;
    public static PotionEffect winEffect;
    public static PotionEffect loseEffect;
    public static PotionEffect drawEffect;
    public static PotionEffect fromArenaTpEffect;


    public static void configEffectInit() {
        arenaTpEffect = new EffectManager(config.get("effects.arenaTpEffect.effect"),
                config.getInt("effects.arenaTpEffect.duration"),
                config.getInt("effects.arenaTpEffect.amplifier"),
                config.getBoolean("effects.arenaTpEffect.particles")).getPotionEffect();

        winEffect = new EffectManager(config.get("effects.winEffect.effect"),
                config.getInt("effects.winEffect.duration"),
                config.getInt("effects.winEffect.amplifier"),
                config.getBoolean("effects.winEffect.particles")).getPotionEffect();

        loseEffect = new EffectManager(config.get("effects.loseEffect.effect"),
                config.getInt("effects.loseEffect.duration"),
                config.getInt("effects.loseEffect.amplifier"),
                config.getBoolean("effects.loseEffect.particles")).getPotionEffect();

        drawEffect = new EffectManager(config.get("effects.drawEffect.effect"),
                config.getInt("effects.drawEffect.duration"),
                config.getInt("effects.drawEffect.amplifier"),
                config.getBoolean("effects.drawEffect.particles")).getPotionEffect();

        fromArenaTpEffect = new EffectManager(config.get("effects.fromArenaTpEffect.effect"),
                config.getInt("effects.fromArenaTpEffect.duration"),
                config.getInt("effects.fromArenaTpEffect.amplifier"),
                config.getBoolean("effects.fromArenaTpEffect.particles")).getPotionEffect();

    }


}
