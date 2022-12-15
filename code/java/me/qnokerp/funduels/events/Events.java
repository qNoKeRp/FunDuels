package me.qnokerp.funduels.events;

import me.qnokerp.funduels.Main;
import me.qnokerp.funduels.utils.FunDuelsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Events {
    public static HashMap<String, Integer> taskId = new HashMap<String, Integer>();

    private static final double delayStartTP = Main.getConfigData().getConfig().getInt("DelayStartTP", (int) 100d);

    public static void teleportEvent(Player player1, Player player2) {
        for (String key : Main.locations.keySet()) {
            if (!Main.locations.get(key)) {

            FunDuelsUtils.bossBarTpToArenaScheduler(player1, delayStartTP, false, key, "spawn1");

            FunDuelsUtils.bossBarTpToArenaScheduler(player2, delayStartTP, false, key, "spawn2");

            Main.locations.put(key, true);

            int delayId = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Main.locations.put(Main.duelists.get(player1), false);

                    player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fВремя вышло, &eничья! &fтелепортация назад.."));
                    player1.teleport(Main.beforeLocation.get(player1));
                    Main.duelists.remove(player1);
                    Main.beforeLocation.remove(player1);

                    player2.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fВремя вышло, &eничья! &fтелепортация назад.."));
                    player2.teleport(Main.beforeLocation.get(player2));
                    Main.duelists.remove(player2);
                    Main.beforeLocation.remove(player2);
                }
            }, Main.getConfigData().getConfig().getInt("FightTime") * 20L + (int) delayStartTP * 20L);
            taskId.put(key, delayId);
            }
        }
    }


    public static void finishEvent(Player winner, Player loser) {
        int num = Main.getConfigData().getConfig().getInt("DelayFinishTP");
        Bukkit.getScheduler().cancelTask(taskId.get(Main.duelists.get(winner)));

        Main.duelists.remove(loser);
        Main.beforeLocation.remove(loser);
        loser.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fВы потерпели &cпоражение &fот рук игрока &6" + winner.getName()));

        winner.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fВы одержали &aпобеду &fнад игроком &6" + loser.getName()));
        winner.sendMessage(String.format(ChatColor.translateAlternateColorCodes('&', String.format("&6[⚔] &fТелепортация назад через %s секунд", num))));
        FunDuelsUtils.bossBarTpBackScheduler(winner, num, true);
    }
}
