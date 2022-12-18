package me.qnokerp.funduels.utils.bossbarevents;

import me.qnokerp.funduels.Main;
import me.qnokerp.funduels.utils.configmanagers.submanagers.ConfigInits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

public class FromArenaBossbar {
    public static void bossBarTpBackScheduler(Player player, final double delayFinishTP, boolean reverse) {
        final int[] num = {0};


        Main.bossBars.put(player, Bukkit.getServer().createBossBar(String.format("До телепортации: %s секунд", (int) delayFinishTP), BarColor.GREEN, BarStyle.SOLID));
        Main.bossBars.get(player).setProgress(reverse ? (delayFinishTP - num[0]) / delayFinishTP : num[0] / delayFinishTP);
        Main.bossBars.get(player).addPlayer(player);

        Main.tasks.put(player, Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                num[0] = num[0] + 1;
                if(!(num[0] == delayFinishTP)) {
                    Main.bossBars.get(player).setProgress(reverse ? (delayFinishTP - num[0]) / delayFinishTP : num[0] / delayFinishTP  );
                    Main.bossBars.get(player).setTitle(String.format("До телепортации: %s секунд", (int) delayFinishTP - num[0]));

                } else {
                    Main.bossBars.get(player).removePlayer(player);
                    Main.bossBars.remove(player);

                    player.teleport(Main.beforeLocation.get(player));
                    Main.locations.put(Main.duelists.get(player), false);
                    Main.duelists.remove(player);
                    Main.beforeLocation.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fТелепортация с арены"));
                    player.addPotionEffect(ConfigInits.fromArenaTpEffect);
                    player.playSound(player.getLocation(), ConfigInits.fromArenaTpSound.getSound(), ConfigInits.fromArenaTpSound.getVolume(), ConfigInits.fromArenaTpSound.getPitch());

                    Bukkit.getScheduler().cancelTask(Main.tasks.get(player).getTaskId());

                }
            }
        }, 20L, 20L));
    }
}
