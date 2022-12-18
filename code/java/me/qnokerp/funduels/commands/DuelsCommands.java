package me.qnokerp.funduels.commands;

import me.qnokerp.funduels.Main;
import me.qnokerp.funduels.events.Events;
import me.qnokerp.funduels.utils.configmanagers.submanagers.ConfigInits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static me.qnokerp.funduels.utils.FunDuelsUtils.getSendComponent;
import static me.qnokerp.funduels.utils.FunDuelsUtils.isDueling;

public class DuelsCommands implements CommandExecutor, TabCompleter {
    public static Map<Player, Player> queries = new HashMap<Player, Player>();


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) commandSender;


        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("cancel")) {

                if(!queries.containsKey(player)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fВы не отправляли запрос на дуэль"));
                    return true;
                }

                queries.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fВаш запрос на дуэль был отменен"));

                return true;
            }
            if(args[0].equalsIgnoreCase("leave")) {
                if(!isDueling(player)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fВы не сражаетесь на данный момент"));
                    return true;
                }

                if(Main.winners == null) {
                    Events.tpBackEvent(player, false);
                    return true;
                }
                Events.tpBackEvent(player, Main.winners.contains(player));
                return true;

            }

            if(player.hasPermission(ConfigInits.perm)) {
                if(args[0].equalsIgnoreCase("test")) {
                    player.playSound(player.getLocation(), ConfigInits.getQuerySound.getSound(), ConfigInits.getQuerySound.getVolume(), ConfigInits.getQuerySound.getPitch());
                    player.addPotionEffect(ConfigInits.arenaTpEffect);
                    return true;
                }
                if(args[0].equalsIgnoreCase("reload")) {

                    Main.getConfigData().reload();
                    Main.getLocationsData().reload();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fКонфиг перезагружен"));
                    return true;
                }
            }



        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("send")) {

                if (!queries.containsKey(player)) {
                    if (Bukkit.getPlayer(args[1]) != null) {

                        if (player.equals(Bukkit.getPlayer(args[1]))) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fНельзя кидать запрос на дуэль самому себе"));
                            return true;
                        }
                        Player recipient = Bukkit.getPlayer(args[1]);

                        if (isDueling(recipient)) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fЭтот игрок уже сражается на арене"));
                            return true;

                        } else {
                            queries.put(player, recipient);

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&6[⚔] &fВы успешно отправили запрос на дуэль игроку &6%s", args[1])));

                            assert recipient != null;


                            recipient.sendMessage(ChatColor.translateAlternateColorCodes('&',String.format("&6[⚔] &fВы получили запрос на дуэль от игрока &6%s", player.getName())));
                            recipient.playSound(recipient.getLocation(), ConfigInits.getQuerySound.getSound(), ConfigInits.getQuerySound.getVolume(), ConfigInits.getQuerySound.getPitch());

                            recipient.sendMessage(getSendComponent(player));

                            System.out.println(ConfigInits.duelQueryTime * 20L);
                            Main.tasks.put(player, Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                                @Override
                                public void run() {
                                    if(!isDueling(player)) {
                                        queries.remove(player);
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[⚔] &fВремя запроса вышло"));
                                    }
                                }
                            }, ConfigInits.duelQueryTime * 20L ));

                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fДанного игрока не существует"));
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fВы уже отправили запрос на дуэль"));
                    return true;
                }


            }

            if(args[0].equalsIgnoreCase("accept")) {
                if (queries.containsValue(player)) {
                    for(Player sender: queries.keySet()) {
                        if(queries.get(sender).equals(player)) {

                            queries.remove(sender);
                            Bukkit.getScheduler().cancelTask(Main.tasks.get(sender).getTaskId());
                            player.playSound(player.getLocation(), ConfigInits.acceptQuerySound.getSound(), ConfigInits.acceptQuerySound.getVolume(), ConfigInits.acceptQuerySound.getPitch());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fВы успешно приняли запрос на дуэль"));

                            sender.playSound(sender.getLocation(), ConfigInits.acceptQuerySound.getSound(), ConfigInits.acceptQuerySound.getVolume(), ConfigInits.acceptQuerySound.getPitch());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&6[⚔] &f%sПринял ваш запрос, идет телепортация на арену..", player.getName())));

                            Events.teleportEvent(sender, player);

                            return true;
                        }
                    }
                }
            }
            if(args[0].equalsIgnoreCase("deny")) {
                if(queries.containsValue(player)) {
                    for(Player sender: queries.keySet()) {
                        if (queries.get(sender).equals(player)) {
                            Bukkit.getScheduler().cancelTask(Main.tasks.get(sender).getTaskId());

                            queries.remove(sender);
                            player.playSound(player.getLocation(), ConfigInits.denyQuerySound.getSound(), ConfigInits.denyQuerySound.getVolume(), ConfigInits.denyQuerySound.getPitch());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fВы успешно отклонили запрос на дуель"));

                            sender.playSound(sender.getLocation(), ConfigInits.denyQuerySound.getSound(), ConfigInits.denyQuerySound.getVolume(), ConfigInits.denyQuerySound.getPitch());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6[⚔] &fВаш запрос был отклонен"));

                            return true;
                        }
                    }
                }
            }

        }
        if(args.length == 3) {
            if(player.hasPermission(onTabCompleter.addLocPermission)) {
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
