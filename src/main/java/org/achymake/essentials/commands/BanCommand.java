package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.DateHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BanCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public BanCommand() {
        getInstance().getCommand("ban").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 3) {
                var target = sender.getServer().getPlayerExact(args[0]);
                var value = Integer.parseInt(args[1]);
                var date = args[2];
                if (value > 0) {
                    if (date.equalsIgnoreCase("d")) {
                        if (target != null) {
                            if (target == player) {
                                getUserdata(target).setLong("settings.ban-expire", new DateHandler().addDays(value));
                                getUserdata(target).setBoolean("settings.banned", true);
                                getMessage().send(player, "&6You banned&f " + target.getName() + "&6 for&f " + value + " " + date);
                                target.kickPlayer(getUserdata(target).getConfig().getString("settings.ban-reason"));
                            } else if (target.hasPermission("essentials.command.ban.exempt")) {
                                getMessage().send(player, "&cYou are not allowed to ban&f " + target.getName());
                            } else if (!getUserdata(target).isBanned()) {
                                getUserdata(target).setLong("settings.ban-expire", new DateHandler().addDays(value));
                                getUserdata(target).setBoolean("settings.banned", true);
                                getMessage().send(player, "&6You banned&f " + target.getName() + "&6 for&f " + value + " " + date);
                                target.kickPlayer(getUserdata(target).getConfig().getString("settings.ban-reason"));
                            } else getMessage().send(player, target.getName() + "&c is already banned");
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            if (!getUserdata(offlinePlayer).isBanned()) {
                                getUserdata(offlinePlayer).setLong("settings.ban-expire", new DateHandler().addDays(value));
                                getUserdata(offlinePlayer).setBoolean("settings.banned", true);
                                getMessage().send(player, "&6You banned&f " + offlinePlayer.getName() + "&6 for&f " + value + " " + date);
                            } else getMessage().send(player, offlinePlayer.getName() + "&c is already banned");
                        }
                    } else if (date.equalsIgnoreCase("m")) {
                        if (target != null) {
                            if (target == player) {
                                getUserdata(target).setLong("settings.ban-expire", new DateHandler().addMonths(value));
                                getUserdata(target).setBoolean("settings.banned", true);
                                getMessage().send(player, "&6You banned&f " + target.getName() + "&6 for&f " + value + " " + date);
                                target.kickPlayer(getUserdata(target).getConfig().getString("settings.ban-reason"));
                            } else if (target.hasPermission("essentials.command.ban.exempt")) {
                                getMessage().send(player, "&cYou are not allowed to ban&f " + target.getName());
                            } else if (!getUserdata(target).isBanned()) {
                                getUserdata(target).setLong("settings.ban-expire", new DateHandler().addMonths(value));
                                getUserdata(target).setBoolean("settings.banned", true);
                                getMessage().send(player, "&6You banned&f " + target.getName() + "&6 for&f " + value + " " + date);
                                target.kickPlayer(getUserdata(target).getConfig().getString("settings.ban-reason"));
                            } else getMessage().send(player, target.getName() + "&c is already banned");
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            if (!getUserdata(offlinePlayer).isBanned()) {
                                getUserdata(offlinePlayer).setLong("settings.ban-expire", new DateHandler().addMonths(value));
                                getUserdata(offlinePlayer).setBoolean("settings.banned", true);
                                getMessage().send(player, "&6You banned&f " + offlinePlayer.getName() + "&6 for&f " + value + " " + date);
                            } else getMessage().send(player, offlinePlayer.getName() + "&c is already banned");
                        }
                    } else if (date.equalsIgnoreCase("y")) {
                        if (target != null) {
                            if (target == player) {
                                getUserdata(target).setLong("settings.ban-expire", new DateHandler().addYears(value));
                                getUserdata(target).setBoolean("settings.banned", true);
                                getMessage().send(player, "&6You banned&f " + target.getName() + "&6 for&f " + value + " " + date);
                                target.kickPlayer(getUserdata(target).getConfig().getString("settings.ban-reason"));
                            } else if (target.hasPermission("essentials.command.ban.exempt")) {
                                getMessage().send(player, "&cYou are not allowed to ban&f " + target.getName());
                            } else if (!getUserdata(target).isBanned()) {
                                getUserdata(target).setLong("settings.ban-expire", new DateHandler().addYears(value));
                                getUserdata(target).setBoolean("settings.banned", true);
                                getMessage().send(player, "&6You banned&f " + target.getName() + "&6 for&f " + value + " " + date);
                                target.kickPlayer(getUserdata(target).getConfig().getString("settings.ban-reason"));
                            } else getMessage().send(player, target.getName() + "&c is already banned");
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            if (!getUserdata(offlinePlayer).isBanned()) {
                                getUserdata(offlinePlayer).setLong("settings.ban-expire", new DateHandler().addYears(value));
                                getUserdata(offlinePlayer).setBoolean("settings.banned", true);
                                getMessage().send(player, "&6You banned&f " + offlinePlayer.getName() + "&6 for&f " + value + " " + date);
                            } else getMessage().send(player, offlinePlayer.getName() + "&c is already banned");
                        }
                    }
                }
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender commandSender) {
            if (args.length == 3) {
                var target = sender.getServer().getPlayerExact(args[0]);
                var value = Integer.parseInt(args[1]);
                var date = args[2];
                if (value > 0) {
                    if (date.equalsIgnoreCase("d")) {
                        if (target != null) {
                            if (!getUserdata(target).isBanned()) {
                                getUserdata(target).setLong("settings.ban-expire", new DateHandler().addDays(value));
                                getUserdata(target).setBoolean("settings.banned", true);
                                commandSender.sendMessage("You banned " + target.getName() + " for " + value + " " + date);
                                target.kickPlayer(getUserdata(target).getConfig().getString("settings.ban-reason"));
                            } else commandSender.sendMessage(target.getName() + " is already banned");
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            if (!getUserdata(offlinePlayer).isBanned()) {
                                getUserdata(offlinePlayer).setLong("settings.ban-expire", new DateHandler().addDays(value));
                                getUserdata(offlinePlayer).setBoolean("settings.banned", true);
                                commandSender.sendMessage("You banned " + offlinePlayer.getName() + " for " + value + " " + date);
                            } else commandSender.sendMessage(offlinePlayer.getName() + " is already banned");
                        }
                    } else if (date.equalsIgnoreCase("m")) {
                        if (target != null) {
                            if (!getUserdata(target).isBanned()) {
                                getUserdata(target).setLong("settings.ban-expire", new DateHandler().addMonths(value));
                                getUserdata(target).setBoolean("settings.banned", true);
                                commandSender.sendMessage("You banned " + target.getName() + " for " + value + " " + date);
                                target.kickPlayer(getUserdata(target).getConfig().getString("settings.ban-reason"));
                            } else commandSender.sendMessage(target.getName() + " is already banned");
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            if (!getUserdata(offlinePlayer).isBanned()) {
                                getUserdata(offlinePlayer).setLong("settings.ban-expire", new DateHandler().addMonths(value));
                                getUserdata(offlinePlayer).setBoolean("settings.banned", true);
                                commandSender.sendMessage("You banned " + offlinePlayer.getName() + " for " + value + " " + date);
                            } else commandSender.sendMessage(offlinePlayer.getName() + " is already banned");
                        }
                    } else if (date.equalsIgnoreCase("y")) {
                        if (target != null) {
                            if (!getUserdata(target).isBanned()) {
                                getUserdata(target).setLong("settings.ban-expire", new DateHandler().addYears(value));
                                getUserdata(target).setBoolean("settings.banned", true);
                                commandSender.sendMessage("You banned " + target.getName() + " for " + value + " " + date);
                                target.kickPlayer(getUserdata(target).getConfig().getString("settings.ban-reason"));
                            } else commandSender.sendMessage(target.getName() + " is already banned");
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            if (!getUserdata(offlinePlayer).isBanned()) {
                                getUserdata(offlinePlayer).setLong("settings.ban-expire", new DateHandler().addYears(value));
                                getUserdata(offlinePlayer).setBoolean("settings.banned", true);
                                commandSender.sendMessage("You banned " + offlinePlayer.getName() + " for " + value + " " + date);
                            } else commandSender.sendMessage(offlinePlayer.getName() + " is already banned");
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata(target).isVanished()) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            } else if (args.length == 2) {
                commands.add("1");
                commands.add("2");
                commands.add("3");
                commands.add("4");
                commands.add("5");
                commands.add("6");
                commands.add("7");
            } else if (args.length == 3) {
                commands.add("d");
                commands.add("m");
                commands.add("y");
            }
        }
        return commands;
    }
}