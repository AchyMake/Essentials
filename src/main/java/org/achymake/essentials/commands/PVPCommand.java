package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PVPCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public PVPCommand() {
        getInstance().getCommand("pvp").setExecutor(this);
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
                    userdata.setBoolean("settings.pvp", !userdata.isPVP());
                    if (userdata.isPVP()) {
                        getMessage().sendActionBar(player, "&6&lPVP:&a Enabled");
                    } else getMessage().sendActionBar(player, "&6&lPVP:&c Disabled");
                    return true;
                } else if (args.length == 1) {
                    if (player.hasPermission("essentials.command.pvp.other")) {
                        var target = sender.getServer().getPlayerExact(args[0]);
                        if (target != null) {
                            var userdataTarget = getUserdata(target);
                            if (target == player) {
                                userdataTarget.setBoolean("settings.pvp", !userdataTarget.isPVP());
                                if (userdataTarget.isPVP()) {
                                    getMessage().send(target, player.getName() + "&6 enabled pvp for you");
                                    getMessage().sendActionBar(target, "&6&lPVP:&a Enabled");
                                    getMessage().send(player, "&6You enabled pvp for&f " + target.getName());
                                } else {
                                    getMessage().send(target, player.getName() + "&6 disabled pvp for you");
                                    getMessage().sendActionBar(target, "&6&lPVP:&c Disabled");
                                    getMessage().send(player, "&6You disabled pvp for&f " + target.getName());
                                }
                            } else if (!target.hasPermission("essentials.command.pvp.exempt")) {
                                userdataTarget.setBoolean("settings.pvp", !userdataTarget.isPVP());
                                if (userdataTarget.isPVP()) {
                                    getMessage().send(target, player.getName() + "&6 enabled pvp for you");
                                    getMessage().sendActionBar(target, "&6&lPVP:&a Enabled");
                                    getMessage().send(player, "&6You enabled pvp for&f " + target.getName());
                                } else {
                                    getMessage().send(target, player.getName() + "&6 disabled pvp for you");
                                    getMessage().sendActionBar(target, "&6&lPVP:&c Disabled");
                                    getMessage().send(player, "&6You disabled pvp for&f " + target.getName());
                                }
                            }
                        } else {
                            var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                userdataOffline.setBoolean("settings.pvp", !userdataOffline.isPVP());
                                if (userdataOffline.isPVP()) {
                                    getMessage().send(player, "&6You enabled pvp for&f " + offlinePlayer.getName());
                                } else getMessage().send(player, "&6You disabled pvp for&f " + offlinePlayer.getName());
                            } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    var userTarget = getUserdata(target);
                    userTarget.setBoolean("settings.pvp", !userTarget.isPVP());
                    if (userTarget.isPVP()) {
                        getMessage().send(target, consoleCommandSender.getName() + "&6 enabled pvp for you");
                        getMessage().sendActionBar(target, "&6&lPVP:&a Enabled");
                        consoleCommandSender.sendMessage("You enabled pvp for " + target.getName());
                    } else {
                        getMessage().send(target, consoleCommandSender.getName() + "&6 disabled pvp for you");
                        getMessage().sendActionBar(target, "&6&lPVP:&c Disabled");
                        consoleCommandSender.sendMessage("You disabled pvp for " + target.getName());
                    }
                } else {
                    var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setBoolean("settings.pvp", !userdataOffline.isPVP());
                        if (getUserdata(offlinePlayer).isPVP()) {
                            consoleCommandSender.sendMessage("You enabled pvp for " + offlinePlayer.getName());
                        } else consoleCommandSender.sendMessage("You disabled pvp for " + offlinePlayer.getName());
                    } else consoleCommandSender.sendMessage(offlinePlayer.getName() + " has never joined");
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
                if (player.hasPermission("essentials.command.pvp.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata(target).isVanished()) {
                            if (target.getName().startsWith(args[0])) {
                                commands.add(target.getName());
                            }
                        }
                    });
                }
            }
        }
        return commands;
    }
}