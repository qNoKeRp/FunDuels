package me.qnokerp.funduels.commands;

import me.qnokerp.funduels.Main;
import me.qnokerp.funduels.events.Events;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static me.qnokerp.funduels.utils.FunDuelsUtils.isDueling;

public class DuelsCommands implements CommandExecutor, TabCompleter {
    public static Map<Player, Player> queries = new HashMap<Player, Player>();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) commandSender;

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("info")) {
                player.sendMessage(String.valueOf(Main.getConfigData().getConfig().get("DelayTP")));
            }
        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("send")) {

                if (!queries.containsKey(player)) {

                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player recipient = Bukkit.getPlayer(args[1]);
                        queries.put(player, recipient);

                        player.sendMessage(String.format("Вы успешно отправили запрос на дуэль игроку %s", args[1]));
                        recipient.sendMessage(String.format("Вы получили запрос на дуэль от игрока %s, \n/duel accept <ник> - принять\n/duel deny <ник> - отклонить", player.getName()));

                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                            if(isDueling(player)) {
                                queries.remove(player);
                                player.sendMessage("Время запроса вышло");
                            }
                        }, 60 * 20);

                        return true;
                    }
                    player.sendMessage("Вы уже отправили запрос на дуэль!");

                    return true;
                }

            }

            if(args[0].equalsIgnoreCase("accept")) {
                if (queries.containsValue(player)) {
                    for(Player sender: queries.keySet()) {
                        if(queries.get(sender).equals(player)) {

                            queries.remove(sender);
                            player.sendMessage("Вы успешно приняли запрос на дуэль!");
                            Events.teleportEvent(sender, player);

                            return true;
                        }
                    }
                }
            }

        }
        if(args.length == 3) {
            if(player.hasPermission("ffa.admin.permission")) {
                if(args[0].equalsIgnoreCase("addloc")) {
                    if(args[1].equalsIgnoreCase("help")) {
                        player.sendMessage("/duel addloc <имя локации> <spawn1/spawn2>");
                        return true;
                    }
                if(args[2].equalsIgnoreCase("spawn1")) {
                    Main.getLocationsData().getConfig().set("locations." + args[1] + ".spawn1", player.getLocation());
                }

                if(args[2].equalsIgnoreCase("spawn2")) {
                    Main.getLocationsData().getConfig().set("locations." + args[1] + ".spawn2", player.getLocation());
                }

                player.sendMessage(String.format("Успешно установлена локация " + args[1] + " (%s)", args[2]));
                Main.getLocationsData().save();
                return true;
                }
            }

        }


        player.sendMessage("/duel help");
        return true;
    }




    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
