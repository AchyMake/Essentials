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
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (target == player) {
                        getUserdata(player).setBoolean("settings.frozen", !getUserdata(player).isFrozen());
                        if (getUserdata(player).isFrozen()) {
                            getMessage().send(player, "&6You froze&f " + target.getName());
                        } else getMessage().send(player, "&6You unfroze&f " + target.getName());
                    } else if (!target.hasPermission("players.command.freeze.exempt")) {
                        getUserdata(target).setBoolean("settings.frozen", !getUserdata(target).isFrozen());
                        if (getUserdata(target).isFrozen()) {
                            getMessage().send(player, "&6You froze&f " + target.getName());
                        } else getMessage().send(player, "&6You unfroze&f " + target.getName());
                    } else getMessage().send(player, command.getPermissionMessage());
                } else {
                    var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    if (getUserdata(offlinePlayer).exists()) {
                        getUserdata(offlinePlayer).setBoolean("settings.frozen", !getUserdata(offlinePlayer).isFrozen());
                        if (getUserdata(offlinePlayer).isFrozen()) {
                            getMessage().send(player, "&6You froze&f " + offlinePlayer.getName());
                        } else getMessage().send(player, "&6You unfroze&f " + offlinePlayer.getName());
                    } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                }
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getUserdata(target).setBoolean("settings.frozen", !getUserdata(target).isFrozen());
                    if (getUserdata(target).isFrozen()) {
                        consoleCommandSender.sendMessage("You froze " + target.getName());
                    } else consoleCommandSender.sendMessage("You unfroze " + target.getName());
                } else {
                    var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    if (getUserdata(offlinePlayer).exists()) {
                        getUserdata(offlinePlayer).setBoolean("settings.frozen", !getUserdata(offlinePlayer).isFrozen());
                        if (getUserdata(offlinePlayer).isFrozen()) {
                            consoleCommandSender.sendMessage("You froze " + offlinePlayer.getName());
                        } else consoleCommandSender.sendMessage("You unfroze " + offlinePlayer.getName());
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