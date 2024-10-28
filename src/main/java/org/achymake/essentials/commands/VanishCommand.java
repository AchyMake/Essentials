package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private VanishHandler getVanish() {
        return getInstance().getVanishHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public VanishCommand() {
        getInstance().getCommand("vanish").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else {
                if (args.length == 0) {
                    getVanish().toggleVanish(player);
                    return true;
                } else if (args.length == 1) {
                    if (player.hasPermission("essentials.command.vanish.other")) {
                        var target = sender.getServer().getPlayerExact(args[0]);
                        if (target != null) {
                            if (target == player) {
                                getVanish().toggleVanish(target);
                                if (getVanish().isVanish(target)) {
                                    getMessage().send(target, player.getName() + "&6 made you vanish");
                                    getMessage().send(player, target.getName() + "&6 is now vanished");
                                } else {
                                    getMessage().send(target, player.getName() + "&6 made you no longer vanish");
                                    getMessage().send(player, target.getName() + "&6 is no longer vanished");
                                }
                            } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                                getVanish().toggleVanish(target);
                                if (getVanish().isVanish(target)) {
                                    getMessage().send(target, player.getName() + "&6 made you vanish");
                                    getMessage().send(player, target.getName() + "&6 is now vanished");
                                } else {
                                    getMessage().send(target, player.getName() + "&6 made you no longer vanish");
                                    getMessage().send(player, target.getName() + "&6 is no longer vanished");
                                }
                            } else getMessage().send(player, command.getPermissionMessage());
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                getVanish().toggleVanish(offlinePlayer);
                                if (getVanish().isVanish(offlinePlayer)) {
                                    getMessage().send(player, offlinePlayer.getName() + "&6 is now vanished");
                                } else getMessage().send(player, offlinePlayer.getName() + "&6 is no longer vanished");
                            } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                        return true;
                    }
                } else if (args.length == 2) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    var value = Boolean.parseBoolean(args[1]);
                    if (value) {
                        if (target != null) {
                            if (!getUserdata(target).isVanished()) {
                                if (target == player) {
                                    getVanish().setVanish(target, true);
                                    getMessage().send(target, player.getName() + "&6 made you vanish");
                                    getMessage().send(player, target.getName() + "&6 is now vanished");
                                } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                                    getVanish().setVanish(target, true);
                                    getMessage().send(target, player.getName() + "&6 made you vanish");
                                    getMessage().send(player, target.getName() + "&6 is now vanished");
                                } else getMessage().send(player, command.getPermissionMessage());
                            }
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                if (!getVanish().isVanish(offlinePlayer)) {
                                    getVanish().setVanish(offlinePlayer, true);
                                    getMessage().send(player, offlinePlayer.getName() + "&6 is now vanished");
                                }
                            } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    } else {
                        if (target != null) {
                            if (getVanish().isVanish(target)) {
                                if (target == player) {
                                    getVanish().setVanish(target, false);
                                    getMessage().send(target, player.getName() + "&6 made you no longer vanish");
                                    getMessage().send(player, target.getName() + "&6 is no longer vanished");
                                } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                                    getVanish().setVanish(target, false);
                                    getMessage().send(target, player.getName() + "&6 made you no longer vanish");
                                    getMessage().send(player, target.getName() + "&6 is no longer vanished");
                                } else getMessage().send(player, command.getPermissionMessage());
                            }
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                if (getVanish().isVanish(offlinePlayer)) {
                                    getVanish().setVanish(offlinePlayer, false);
                                    getMessage().send(player, offlinePlayer.getName() + "&6 is no longer vanished");
                                }
                            } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                    }
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getVanish().toggleVanish(target);
                    if (getVanish().isVanish(target)) {
                        consoleCommandSender.sendRawMessage(target.getName() + " is now vanished");
                    } else {
                        consoleCommandSender.sendMessage(target.getName() + " is no longer vanished");
                    }
                } else {
                    var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        getVanish().toggleVanish(offlinePlayer);
                        if (getVanish().isVanish(offlinePlayer)) {
                            consoleCommandSender.sendRawMessage(offlinePlayer.getName() + " is now vanished");
                        } else {
                            consoleCommandSender.sendMessage(offlinePlayer.getName() + " is no longer vanished");
                        }
                    } else {
                        consoleCommandSender.sendRawMessage(offlinePlayer.getName() + " has never joined");
                    }
                }
                return true;
            } else if (args.length == 2) {
                var target = sender.getServer().getPlayerExact(args[0]);
                var value = Boolean.parseBoolean(args[1]);
                if (value) {
                    if (target != null) {
                        if (!getVanish().isVanish(target)) {
                            getVanish().setVanish(target, true);
                            consoleCommandSender.sendMessage(target.getName() + " is now vanished");
                            return true;
                        }
                    } else {
                        var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                        var userdataOffline = getUserdata(offlinePlayer);
                        if (userdataOffline.exists()) {
                            if (!getVanish().isVanish(offlinePlayer)) {
                                getVanish().setVanish(offlinePlayer, true);
                                consoleCommandSender.sendMessage(offlinePlayer.getName() + " is now vanished");
                            }
                        } else consoleCommandSender.sendMessage(offlinePlayer.getName() + " has never joined");
                        return true;
                    }
                } else {
                    if (target != null) {
                        if (getVanish().isVanish(target)) {
                            getVanish().setVanish(target, false);
                            consoleCommandSender.sendMessage(target.getName() + " is no longer vanished");
                            return true;
                        }
                    } else {
                        var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                        var userdataOffline = getUserdata(offlinePlayer);
                        if (userdataOffline.exists()) {
                            if (getVanish().isVanish(offlinePlayer)) {
                                getVanish().setVanish(offlinePlayer, false);
                                consoleCommandSender.sendMessage(offlinePlayer.getName() + " is no longer vanished");
                            }
                        } else consoleCommandSender.sendMessage(offlinePlayer.getName() + " has never joined");
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
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.vanish.other")) {
                    for (var players : getInstance().getOnlinePlayers()) {
                        if (players.getName().startsWith(args[0])) {
                            commands.add(players.getName());
                        }
                    }
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.vanish.other")) {
                    commands.add("true");
                    commands.add("false");
                }
            }
        }
        return commands;
    }
}