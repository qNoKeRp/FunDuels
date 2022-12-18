package me.qnokerp.funduels.utils.bossbarevents;

import me.qnokerp.funduels.Main;
import me.qnokerp.funduels.utils.configmanagers.submanagers.ConfigInits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ToArenaBossbar {
    public static void bossBarTpToArenaScheduler(Player player, final double delayStartTP, boolean reverse, String location, String spawn) {
        final int[] num = {0};


        Main.bossBars.put(player, Bukkit.getServer().createBossBar(String.format("До телепортации: %s секунд", (int) delayStartTP), BarColor.GREEN, BarStyle.SOLID));
        Main.bossBars.get(player).setProgress(reverse ? (delayStartTP - num[0]) / delayStartTP : num[0] / delayStartTP);
        Main.bossBars.get(player).addPlayer(player);

        Main.tasks.put(player, Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                num[0] = num[0] + 1;
                if(!(num[0] == delayStartTP)) {
                    Main.bossBars.get(player).setProgress(reverse ? (delayStartTP - num[0]) / delayStartTP : num[0] / delayStartTP  );
                    Main.bossBars.get(player).setTitle(String.format("До телепортации: %s секунд", (int) delayStartTP - num[0]));

                } else {
                    Main.bossBars.get(player).removePlayer(player);
                    Main.bossBars.remove(player);

                    Main.beforeLocation.put(player, player.getLocation());
                    player.sendMessage(String.format(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fТелепортация на &4арену")));
                    player.teleport(Objects.requireNonNull(Main.getLocationsData().getConfig().getLocation("locations." + location + "." + spawn)));
                    player.addPotionEffect(ConfigInits.arenaTpEffect);
                    player.playSound(player.getLocation(), ConfigInits.arenaTpSound.getSound(), ConfigInits.arenaTpSound.getVolume(), ConfigInits.arenaTpSound.getPitch());
                    Main.duelists.put(player, location);


                    Bukkit.getScheduler().cancelTask(Main.tasks.get(player).getTaskId());

                }
            }
        }, 20L, 20L));
    }

}
