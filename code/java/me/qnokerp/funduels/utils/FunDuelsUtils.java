package me.qnokerp.funduels.utils;

import me.qnokerp.funduels.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


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

    public static TextComponent getFinishComponent(Player player) {
        @NonNull TextComponent textComponent = Component.text("   [Покинуть арену]")
                .clickEvent(ClickEvent.runCommand("/Duel leave"))
                .hoverEvent(HoverEvent.showText(Component.text("Нажмите что бы покинуть арену").color(NamedTextColor.GREEN)))
                .color(NamedTextColor.GREEN);
        return textComponent;
    }



    public static Player getPlayerRival(Player player) {

        if(isDueling(player)) {
            String needLocation = Main.duelists.get(player);
            Set<Player> players =  Main.duelists.keySet();
            for(Player p : players) {
                if(Main.duelists.get(p).equals(needLocation)) {
                    if(!p.equals(player)) {
                        return p;
                    }
                }
            }


        }

        return null;
    }
}
