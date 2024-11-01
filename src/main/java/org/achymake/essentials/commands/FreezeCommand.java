package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FreezeCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public FreezeCommand() {
        getInstance().getCommand("freeze").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var target = player.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    var userdataTarget = getUserdata(target);
                    if (target == player) {
                        userdataTarget.setBoolean("settings.frozen", !userdataTarget.isFrozen());
                        if (userdataTarget.isFrozen()) {
                            player.sendMessage(getMessage().get("commands.freeze.enable", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.freeze.disable", target.getName()));
                    } else if (!target.hasPermission("essentials.command.freeze.exempt")) {
                        userdataTarget.setBoolean("settings.frozen", !userdataTarget.isFrozen());
                        if (userdataTarget.isFrozen()) {
                            player.sendMessage(getMessage().get("commands.freeze.enable", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.freeze.disable", target.getName()));
                    } else getMessage().send(player, command.getPermissionMessage());
                } else {
                    var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setBoolean("settings.frozen", !userdataOffline.isFrozen());
                        if (userdataOffline.isFrozen()) {
                            player.sendMessage(getMessage().get("commands.freeze.enable", offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("commands.freeze.disable", offlinePlayer.getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    var userdataTarget = getUserdata(target);
                    userdataTarget.setBoolean("settings.frozen", !userdataTarget.isFrozen());
                    if (userdataTarget.isFrozen()) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.freeze.enable", target.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.freeze.disable", target.getName()));
                } else {
                    var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setBoolean("settings.frozen", !userdataOffline.isFrozen());
                        if (getUserdata(offlinePlayer).isFrozen()) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.freeze.enable", offlinePlayer.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.freeze.disable", offlinePlayer.getName()));
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
        if (sender instanceof Player) {
            if (args.length == 1) {
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata(target).isVanished()) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            }
        }
        return commands;
    }
}