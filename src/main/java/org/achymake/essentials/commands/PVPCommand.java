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
            if (args.length == 0) {
                userdata.setBoolean("settings.pvp", !userdata.isPVP());
                if (userdata.isPVP()) {
                    getMessage().sendActionBar(player, getMessage().get("commands.pvp.self", getMessage().get("enable")));
                } else getMessage().sendActionBar(player, getMessage().get("commands.pvp.self", getMessage().get("disable")));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.pvp.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        if (target == player) {
                            userdataTarget.setBoolean("settings.pvp", !userdataTarget.isPVP());
                            if (userdataTarget.isPVP()) {
                                getMessage().sendActionBar(target, getMessage().get("commands.pvp.self", getMessage().get("enable")));
                                player.sendMessage(getMessage().get("commands.pvp.sender", target.getName(), getMessage().get("enable")));
                            } else {
                                getMessage().sendActionBar(target, getMessage().get("commands.pvp.self", getMessage().get("disable")));
                                player.sendMessage(getMessage().get("commands.pvp.sender", target.getName(), getMessage().get("disable")));
                            }
                        } else if (!target.hasPermission("essentials.command.pvp.exempt")) {
                            userdataTarget.setBoolean("settings.pvp", !userdataTarget.isPVP());
                            if (userdataTarget.isPVP()) {
                                getMessage().sendActionBar(target, getMessage().get("commands.pvp.self", getMessage().get("enable")));
                                player.sendMessage(getMessage().get("commands.pvp.sender", target.getName(), getMessage().get("enable")));
                            } else {
                                getMessage().sendActionBar(target, getMessage().get("commands.pvp.self", getMessage().get("disable")));
                                player.sendMessage(getMessage().get("commands.pvp.sender", target.getName(), getMessage().get("disable")));
                            }
                        } else player.sendMessage(getMessage().get("commands.pvp.exempt", target.getName()));
                    } else {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                        var userdataOffline = getUserdata(offlinePlayer);
                        if (userdataOffline.exists()) {
                            userdataOffline.setBoolean("settings.pvp", !userdataOffline.isPVP());
                            if (userdataOffline.isPVP()) {
                                player.sendMessage(getMessage().get("commands.pvp.sender", offlinePlayer.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("commands.pvp.sender", offlinePlayer.getName(), getMessage().get("disable")));
                        } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    }
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var userTarget = getUserdata(target);
                    userTarget.setBoolean("settings.pvp", !userTarget.isPVP());
                    if (userTarget.isPVP()) {
                        getMessage().sendActionBar(target, getMessage().get("commands.pvp.self", getMessage().get("enable")));
                        consoleCommandSender.sendMessage(getMessage().get("commands.pvp.sender", target.getName(), getMessage().get("enable")));
                    } else {
                        getMessage().sendActionBar(target, getMessage().get("commands.pvp.self", getMessage().get("disable")));
                        consoleCommandSender.sendMessage(getMessage().get("commands.pvp.sender", "Disable", target.getName()));
                    }
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setBoolean("settings.pvp", !userdataOffline.isPVP());
                        if (getUserdata(offlinePlayer).isPVP()) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.pvp.sender", offlinePlayer.getName(), getMessage().get("enable")));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.pvp.sender", offlinePlayer.getName(), getMessage().get("disable")));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
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