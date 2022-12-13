package me.qnokerp.funduels.utils;

import me.qnokerp.funduels.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

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
}
