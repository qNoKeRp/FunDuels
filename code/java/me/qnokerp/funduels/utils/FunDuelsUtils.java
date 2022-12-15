package me.qnokerp.funduels.utils;

import me.qnokerp.funduels.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FunDuelsUtils {

    public static boolean isDueling(Player player) {
        return Main.duelists.containsKey(player);
    }

    public static HashMap<String, Boolean> listToHashmap(ArrayList<String> list) {
        HashMap<String, Boolean> locations = new HashMap<String, Boolean>();
        for(String key: list) {
            locations.put(key, false);
        }

        return locations;
    }

    public static TextComponent getSendComponent(Player player) {

        @NonNull TextComponent textComponent = Component.text("   [Принять]")
                .clickEvent(ClickEvent.runCommand("/Duel accept " + player.getName()))
                .hoverEvent(HoverEvent.showText(Component.text("Нажмите что бы принять запрос").color(NamedTextColor.GREEN)))
                .color(NamedTextColor.GREEN)
                .append(Component.text("   |   ").color(NamedTextColor.WHITE))
                .append(
                        Component.text("[Отклонить]")
                                .clickEvent(ClickEvent.runCommand("/Duel deny " + player.getName()))
                                .hoverEvent(HoverEvent.showText(Component.text("Нажмите что бы отклонить запрос").color(NamedTextColor.RED)))
                                .color(NamedTextColor.RED)
                );

        return textComponent;
    }

    public static void bossBarTpToArenaScheduler(Player player, final double delayStartTP, boolean reverse, String key, String spawn) {
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
                    player.teleport(Objects.requireNonNull(Main.getLocationsData().getConfig().getLocation("locations." + key + "." + spawn)));
                    player.sendMessage(String.format(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fТелепортация на &4арену")));
                    Main.duelists.put(player, key);


                    Bukkit.getScheduler().cancelTask(Main.tasks.get(player).getTaskId());

                }
            }
        }, 20L, 20L));
    }
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

                    Bukkit.getScheduler().cancelTask(Main.tasks.get(player).getTaskId());

                }
            }
        }, 20L, 20L));
    }
}
