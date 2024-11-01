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
    private DateHandler getDateHandler() {
        return getInstance().getDateHandler();
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
            if (args.length == 3) {
                var target = sender.getServer().getPlayerExact(args[0]);
                var value = Integer.parseInt(args[1]);
                var date = args[2];
                if (value > 0) {
                    if (isDates(date)) {
                        if (target != null) {
                            var userdataTarget = getUserdata(target);
                            if (target == player) {
                                if (date.equalsIgnoreCase("d")) {
                                    userdataTarget.setLong("settings.ban-expire",  getDateHandler().addDays(value));
                                } else if (date.equalsIgnoreCase("m")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (date.equalsIgnoreCase("y")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                }
                                userdataTarget.setBoolean("settings.banned", true);
                                player.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), date));
                                target.kickPlayer(userdataTarget.getConfig().getString("settings.ban-reason"));
                            } else if (target.hasPermission("essentials.command.ban.exempt")) {
                                player.sendMessage(getMessage().get("commands.ban.exempt", target.getName()));
                            } else if (!userdataTarget.isBanned()) {
                                if (date.equalsIgnoreCase("d")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addDays(value));
                                } else if (date.equalsIgnoreCase("m")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (date.equalsIgnoreCase("y")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                }
                                userdataTarget.setBoolean("settings.banned", true);
                                player.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), date));
                                target.kickPlayer(userdataTarget.getConfig().getString("settings.ban-reason"));
                            } else player.sendMessage(getMessage().get("commands.ban.banned", target.getName()));
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                if (!userdataOffline.isBanned()) {
                                    if (date.equalsIgnoreCase("d")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (date.equalsIgnoreCase("m")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (date.equalsIgnoreCase("y")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    userdataOffline.setBoolean("settings.banned", true);
                                    player.sendMessage(getMessage().get("commands.ban.success", offlinePlayer.getName(), String.valueOf(value), date));
                                } else player.sendMessage(getMessage().get("commands.ban.banned", offlinePlayer.getName()));
                            } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        }
                        return true;
                    }
                }
            } else if (args.length > 3) {
                var target = sender.getServer().getPlayerExact(args[0]);
                var value = Integer.parseInt(args[1]);
                var date = args[2];
                if (value > 0) {
                    if (isDates(date)) {
                        var reason = getMessage().getBuilder(args, 3);
                        if (target != null) {
                            var userdataTarget = getUserdata(target);
                            if (target == player) {
                                if (date.equalsIgnoreCase("d")) {
                                    userdataTarget.setLong("settings.ban-expire",  getDateHandler().addDays(value));
                                } else if (date.equalsIgnoreCase("m")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (date.equalsIgnoreCase("y")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                }
                                userdataTarget.setBoolean("settings.banned", true);
                                userdataTarget.setString("settings.ban-reason", reason);
                                player.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), date));
                                target.kickPlayer(userdataTarget.getConfig().getString("settings.ban-reason"));
                            } else if (!target.hasPermission("essentials.command.ban.exempt")) {
                                if (!userdataTarget.isBanned()) {
                                    if (date.equalsIgnoreCase("d")) {
                                        userdataTarget.setLong("settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (date.equalsIgnoreCase("m")) {
                                        userdataTarget.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (date.equalsIgnoreCase("y")) {
                                        userdataTarget.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    userdataTarget.setBoolean("settings.banned", true);
                                    userdataTarget.setString("settings.ban-reason", reason);
                                    player.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), date));
                                    target.kickPlayer(userdataTarget.getConfig().getString("settings.ban-reason"));
                                } else player.sendMessage(getMessage().get("commands.ban.banned", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.ban.exempt", target.getName()));
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                if (!userdataOffline.isBanned()) {
                                    if (date.equalsIgnoreCase("d")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (date.equalsIgnoreCase("m")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (date.equalsIgnoreCase("y")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    userdataOffline.setBoolean("settings.banned", true);
                                    userdataOffline.setString("settings.ban-reason", reason);
                                    player.sendMessage(getMessage().get("commands.ban.success", offlinePlayer.getName(), String.valueOf(value), date));
                                } else player.sendMessage(getMessage().get("commands.ban.banned", offlinePlayer.getName()));
                            } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 3) {
                var target = sender.getServer().getPlayerExact(args[0]);
                var value = Integer.parseInt(args[1]);
                var date = args[2];
                if (value > 0) {
                    if (isDates(date)) {
                        if (target != null) {
                            var userdataTarget = getUserdata(target);
                            if (!userdataTarget.isBanned()) {
                                if (date.equalsIgnoreCase("d")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addDays(value));
                                } else if (date.equalsIgnoreCase("m")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (date.equalsIgnoreCase("y")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                }
                                userdataTarget.setBoolean("settings.banned", true);
                                consoleCommandSender.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), date));
                                target.kickPlayer(userdataTarget.getConfig().getString("settings.ban-reason"));
                            } else consoleCommandSender.sendMessage(getMessage().get("commands.ban.banned", target.getName()));
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                if (!userdataOffline.isBanned()) {
                                    if (date.equalsIgnoreCase("d")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (date.equalsIgnoreCase("m")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (date.equalsIgnoreCase("y")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    userdataOffline.setBoolean("settings.banned", true);
                                    consoleCommandSender.sendMessage(getMessage().get("commands.ban.success", offlinePlayer.getName(), String.valueOf(value), date));
                                } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                            }
                        }
                        return true;
                    }
                }
            } else if (args.length > 3) {
                var target = sender.getServer().getPlayerExact(args[0]);
                var value = Integer.parseInt(args[1]);
                var date = args[2];
                if (value > 0) {
                    if (isDates(date)) {
                        var reason = getMessage().getBuilder(args, 3);
                        if (target != null) {
                            var userdataTarget = getUserdata(target);
                            if (!userdataTarget.isBanned()) {
                                if (date.equalsIgnoreCase("d")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addDays(value));
                                } else if (date.equalsIgnoreCase("m")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                } else if (date.equalsIgnoreCase("y")) {
                                    userdataTarget.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                }
                                userdataTarget.setBoolean("settings.banned", true);
                                userdataTarget.setString("settings.ban-reason", reason);
                                consoleCommandSender.sendMessage(getMessage().get("commands.ban.success", target.getName(), String.valueOf(value), date));
                                target.kickPlayer(userdataTarget.getConfig().getString("settings.ban-reason"));
                            } else consoleCommandSender.sendMessage(getMessage().get("commands.ban.banned", target.getName()));
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                if (!userdataOffline.isBanned()) {
                                    if (date.equalsIgnoreCase("d")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addDays(value));
                                    } else if (date.equalsIgnoreCase("m")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addMonths(value));
                                    } else if (date.equalsIgnoreCase("y")) {
                                        userdataOffline.setLong("settings.ban-expire", getDateHandler().addYears(value));
                                    }
                                    userdataOffline.setBoolean("settings.banned", true);
                                    userdataOffline.setString("settings.ban-reason", reason);
                                    consoleCommandSender.sendMessage(getMessage().get("commands.ban.success", offlinePlayer.getName(), String.valueOf(value), date));
                                } else consoleCommandSender.sendMessage(getMessage().get("commands.ban.banned", offlinePlayer.getName()));
                            } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        }
                        return true;
                    }
                }
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
    private boolean isDates(String date) {
        if (date.equalsIgnoreCase("d")) {
            return true;
        } else if (date.equalsIgnoreCase("m")) {
            return true;
        } else return date.equalsIgnoreCase("y");
    }
}