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
            if (args.length == 0) {
                getVanish().toggleVanish(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.vanish.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            getVanish().toggleVanish(target);
                            if (getVanish().isVanish(target)) {
                                player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                        } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                            getVanish().toggleVanish(target);
                            if (getVanish().isVanish(target)) {
                                player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                        } else player.sendMessage(getMessage().get("commands.vanish.exempt", target.getName()));
                    } else {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                        var userdataOffline = getUserdata(offlinePlayer);
                        if (userdataOffline.exists()) {
                            getVanish().toggleVanish(offlinePlayer);
                            if (getVanish().isVanish(offlinePlayer)) {
                                player.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("disable")));
                        } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    }
                    return true;
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.vanish.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    var value = Boolean.parseBoolean(args[1]);
                    if (value) {
                        if (target != null) {
                            if (target == player) {
                                getVanish().setVanish(target, true);
                                player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                            } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                                getVanish().setVanish(target, true);
                                player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("commands.vanish.exempt", target.getName()));
                        } else {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                getVanish().setVanish(offlinePlayer, true);
                                player.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("enable")));
                            } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        }
                    } else {
                        if (target != null) {
                            if (target == player) {
                                getVanish().setVanish(target, false);
                                player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                            } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                                getVanish().setVanish(target, false);
                                player.sendMessage(getMessage().get("commands.vanish.disable", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.vanish.exempt", target.getName()));
                        } else {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                            var userdataOffline = getUserdata(offlinePlayer);
                            if (userdataOffline.exists()) {
                                getVanish().setVanish(offlinePlayer, false);
                                player.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("disable")));
                            } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        }
                    }
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    getVanish().toggleVanish(target);
                    if (getVanish().isVanish(target)) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        getVanish().toggleVanish(offlinePlayer);
                        if (getVanish().isVanish(offlinePlayer)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("enable")));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("disable")));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
                return true;
            } else if (args.length == 2) {
                var target = getInstance().getPlayer(args[0]);
                var value = Boolean.parseBoolean(args[1]);
                if (value) {
                    if (target != null) {
                        getVanish().setVanish(target, true);
                        consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                    } else {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                        var userdataOffline = getUserdata(offlinePlayer);
                        if (userdataOffline.exists()) {
                            getVanish().setVanish(offlinePlayer, true);
                            consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("enable")));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    }
                } else {
                    if (target != null) {
                        getVanish().setVanish(target, false);
                        consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                    } else {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                        var userdataOffline = getUserdata(offlinePlayer);
                        if (userdataOffline.exists()) {
                            getVanish().setVanish(offlinePlayer, false);
                            consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("disable")));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
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
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    });
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.vanish.others")) {
                    var userdata = getUserdata(getInstance().getOfflinePlayer(args[0]));
                    if (userdata.exists()) {
                        commands.add(String.valueOf(userdata.isVanished()));
                    }
                }
            }
        }
        return commands;
    }
}