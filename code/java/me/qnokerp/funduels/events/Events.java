package me.qnokerp.funduels.events;

import me.qnokerp.funduels.Main;
import me.qnokerp.funduels.utils.configmanagers.submanagers.ConfigInits;
import me.qnokerp.funduels.utils.FunDuelsUtils;
import me.qnokerp.funduels.utils.bossbarevents.FromArenaBossbar;
import me.qnokerp.funduels.utils.bossbarevents.ToArenaBossbar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Events {
    public static HashMap<String, Integer> taskId = new HashMap<String, Integer>();

    public static void teleportEvent(Player player1, Player player2) {
        for (String location : Main.locations.keySet()) {
            if (!Main.locations.get(location)) {

            ToArenaBossbar.bossBarTpToArenaScheduler(player1, ConfigInits.delayStartTP, false, location, "spawn1");

            ToArenaBossbar.bossBarTpToArenaScheduler(player2, ConfigInits.delayStartTP, false, location, "spawn2");

            Main.locations.put(location, true);

            int delayId = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    String message = "&6[⚔] &fВремя вышло, &eничья! &fтелепортация назад..";
                    drawTpEvent(player1, player2, message, message);
                }
            }, ConfigInits.figthTime * 20L + (int) ConfigInits.delayStartTP * 20L);
            taskId.put(location, delayId);
            }
        }
    }

    public static void drawTpEvent(Player player1, Player player2, String message1, String message2) {
        Main.locations.put(Main.duelists.get(player1), false);


        player1.sendMessage(ChatColor.translateAlternateColorCodes('&', message1));
        player1.teleport(Main.beforeLocation.get(player1));
        Main.duelists.remove(player1);
        Main.beforeLocation.remove(player1);
        player1.playSound(player1.getLocation(), ConfigInits.drawSound.getSound(), ConfigInits.drawSound.getVolume(), ConfigInits.drawSound.getPitch());
        player1.addPotionEffect(ConfigInits.drawEffect);


        player2.sendMessage(ChatColor.translateAlternateColorCodes('&', message2));
        player2.teleport(Main.beforeLocation.get(player2));
        Main.duelists.remove(player2);
        Main.beforeLocation.remove(player2);
        player2.playSound(player2.getLocation(), ConfigInits.drawSound.getSound(), ConfigInits.drawSound.getVolume(), ConfigInits.drawSound.getPitch());
        player2.addPotionEffect(ConfigInits.drawEffect);
    }


    public static void finishEvent(Player winner, Player loser) {

        Bukkit.getScheduler().cancelTask(taskId.get(Main.duelists.get(winner)));

        Main.duelists.remove(loser);
        Main.beforeLocation.remove(loser);
        loser.playSound(loser.getLocation(), ConfigInits.loseSound.getSound(), ConfigInits.loseSound.getVolume(), ConfigInits.loseSound.getPitch());

        loser.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fВы потерпели &cпоражение &fот рук игрока &6" + winner.getName()));
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                loser.addPotionEffect(ConfigInits.loseEffect);
            }
        }, 20);

        winner.playSound(winner.getLocation(), ConfigInits.winSound.getSound(), ConfigInits.winSound.getVolume(), ConfigInits.winSound.getPitch());
        winner.addPotionEffect(ConfigInits.winEffect);
        winner.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fВы одержали &aпобеду &fнад игроком &6" + loser.getName()));
        winner.sendMessage(String.format(ChatColor.translateAlternateColorCodes('&', String.format("&6[⚔] &fТелепортация назад через %s секунд", ConfigInits.delayFinishTp))));
        winner.sendMessage(FunDuelsUtils.getFinishComponent(winner));
        Main.winners.add(winner);
        FromArenaBossbar.bossBarTpBackScheduler(winner, ConfigInits.delayFinishTp, true);
    }

    public static void tpBackEvent(Player player1, Boolean isWinner) {
        Main.locations.put(Main.duelists.get(player1), false);


        if(!isWinner) {
            Player player2 = FunDuelsUtils.getPlayerRival(player1);
            assert player2 != null;
            drawTpEvent(player2, player1, String.format("&6[⚔] &fИгрок %s покинул арену, вы одержали победу!", player2.getName()), "&6[⚔] &fВы проиграли покинув арену"  );


        } else {
            player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fТелепортация назад"));
            player1.teleport(Main.beforeLocation.get(player1));
            player1.playSound(player1.getLocation(), ConfigInits.fromArenaTpSound.getSound(), ConfigInits.fromArenaTpSound.getVolume(), ConfigInits.fromArenaTpSound.getPitch());
            player1.addPotionEffect(ConfigInits.fromArenaTpEffect);
            Main.duelists.remove(player1);
            Main.beforeLocation.remove(player1);
            Main.winners.remove(player1);
            Bukkit.getScheduler().cancelTask(Main.tasks.get(player1).getTaskId());
            Main.bossBars.get(player1).removePlayer(player1);

        }

    }

}
