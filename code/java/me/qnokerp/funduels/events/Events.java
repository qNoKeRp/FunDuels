package me.qnokerp.funduels.events;

import me.qnokerp.funduels.Main;
import me.qnokerp.funduels.commands.DuelsCommands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;


public class Events {



    public static void teleportEvent(Player player1, Player player2) {
        for (String key : Main.locations.keySet()) {
            if (!Main.locations.get(key)) {

                Main.beforeLocation.put(player1, player1.getLocation());
                player1.teleport(Objects.requireNonNull(Main.getLocationsData().getConfig().getLocation("locations." + key + ".spawn1")));
                player1.sendMessage("Телепортация на арену");
                Main.duelists.put(player1, key);

                Main.beforeLocation.put(player2, player2.getLocation());
                player2.teleport(Objects.requireNonNull(Main.getLocationsData().getConfig().getLocation("locations." + key + ".spawn2")));
                player2.sendMessage("Телепортация на арену");
                Main.duelists.put(player2, key);

                Main.locations.put(key, true);
            }
        }
    }

    public static void finishEvent(Player winner, Player loser) {


        Main.duelists.remove(loser);
        Main.beforeLocation.remove(loser);
        loser.sendMessage("Вы потерпели поражение от рук игрока " + winner.getName());

        winner.sendMessage("Вы одержали победу над игроком " + loser.getName());
        winner.sendMessage("Телепортация назад через 10 секунд");
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {

                winner.teleport(Main.beforeLocation.get(winner));
                Main.locations.put(Main.duelists.get(winner), false);
                Main.duelists.remove(winner);
                Main.beforeLocation.remove(winner);
                winner.sendMessage("Телепортация с арены");
            }
        }, 10 * 20);



    }
}
