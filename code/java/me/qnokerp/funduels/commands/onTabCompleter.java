package me.qnokerp.funduels.commands;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class onTabCompleter implements TabCompleter, TabExecutor {
    List<String> firstCommands = Arrays.asList("send", "accept", "deny", "leave", "help", "cancel");
    List<String> firstCommandsAdm = Arrays.asList("send", "accept", "deny", "leave", "help", "cancel", "reload", "addloc");

    public static String addLocPermission;
    List<String> spawns = Arrays.asList("spawn1", "spawn2");


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> flist = Lists.newArrayList();
        Player player = (Player) sender;

        if(player.hasPermission(addLocPermission)) {

            if(args.length == 2) {
                if(args[0].equals("addloc")) {
                    flist.add("arena");
                    return flist;
                }
            }
            if(args.length == 3) {
                if(args[0].equals("addloc")) {
                    for(String s : spawns) {
                        if(s.toLowerCase().startsWith(args[2])) {
                            flist.add(s);
                        }
                    }
                    return flist;
                }
            }
        }

        if (args.length == 1) {
            if (!player.hasPermission(addLocPermission)) {
                for (String s : firstCommands) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        flist.add(s);

                    }
                }
                return flist;
            } else {
                for (String s : firstCommandsAdm) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        flist.add(s);
                    }
                }
                return flist;
            }
        }

        if (args.length == 2) {
            if(args[0].equals("send") | args[0].equals("accept") | args[0].equals("deny")) {
                Player[] ps = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                Bukkit.getServer().getOnlinePlayers().toArray(ps);
                for (Player p : ps) {
                    if(p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        flist.add(p.getName());
                    }
                }

                return flist;
            }

        }


        return flist;
    }
}
