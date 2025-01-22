package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
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
                if (!getVanish().toggleVanish(player)) {
                    player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                }
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.vanish.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            if (getVanish().toggleVanish(target)) {
                                if (getVanish().isVanish(target)) {
                                    player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                                } else player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                            } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                        } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                            if (getVanish().toggleVanish(target)) {
                                if (getVanish().isVanish(target)) {
                                    player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                                } else player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                            } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                        } else player.sendMessage(getMessage().get("commands.vanish.exempt", target.getName()));
                    } else {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                        if (getUserdata().exists(offlinePlayer)) {
                            if (getVanish().toggleVanish(offlinePlayer)) {
                                if (getVanish().isVanish(offlinePlayer)) {
                                    player.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("enable")));
                                } else player.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("disable")));
                            } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
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
                                if (getVanish().setVanish(target, true)) {
                                    player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                            } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                                if (getVanish().setVanish(target, true)) {
                                    player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                            } else player.sendMessage(getMessage().get("commands.vanish.exempt", target.getName()));
                        } else {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                            if (getUserdata().exists(offlinePlayer)) {
                                if (getVanish().setVanish(offlinePlayer, true)) {
                                    player.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("enable")));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                            } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                        }
                    } else {
                        if (target != null) {
                            if (target == player) {
                                if (getVanish().setVanish(target, false)) {
                                    player.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                            } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                                if (getVanish().setVanish(target, false)) {
                                    player.sendMessage(getMessage().get("commands.vanish.disable", target.getName()));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                            } else player.sendMessage(getMessage().get("commands.vanish.exempt", target.getName()));
                        } else {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                            if (getUserdata().exists(offlinePlayer)) {
                                if (getVanish().setVanish(offlinePlayer, false)) {
                                    player.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("disable")));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
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
                    if (getVanish().toggleVanish(target)) {
                        if (getVanish().isVanish(target)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    if (getUserdata().exists(offlinePlayer)) {
                        if (getVanish().toggleVanish(offlinePlayer)) {
                            if (getVanish().isVanish(offlinePlayer)) {
                                consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("enable")));
                            } else consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("disable")));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
                return true;
            } else if (args.length == 2) {
                var target = getInstance().getPlayer(args[0]);
                var value = Boolean.parseBoolean(args[1]);
                if (value) {
                    if (target != null) {
                        if (getVanish().setVanish(target, true)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("enable")));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                    } else {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                        if (getUserdata().exists(offlinePlayer)) {
                            if (getVanish().setVanish(offlinePlayer, true)) {
                                consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("enable")));
                            } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    }
                } else {
                    if (target != null) {
                        if (getVanish().setVanish(target, false)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", target.getName(), getMessage().get("disable")));
                        } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(target).getName()));
                    } else {
                        var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                        if (getUserdata().exists(offlinePlayer)) {
                            if (getVanish().setVanish(offlinePlayer, false)) {
                                consoleCommandSender.sendMessage(getMessage().get("commands.vanish.sender", offlinePlayer.getName(), getMessage().get("disable")));
                            } else consoleCommandSender.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(offlinePlayer).getName()));
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
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    if (getUserdata().exists(offlinePlayer)) {
                        commands.add(String.valueOf(getUserdata().isVanished(offlinePlayer)));
                    }
                }
            }
        }
        return commands;
    }
}