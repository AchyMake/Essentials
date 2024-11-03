package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameModeCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public GameModeCommand() {
        getInstance().getCommand("gamemode").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var mode = args[0].toLowerCase();
                if (player.hasPermission("essentials.command.gamemode." + mode)) {
                    if (!getUserdata(player).setGameMode(mode)) {
                        player.sendMessage(getMessage().get("commands.gamemode.invalid", mode));
                    }
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.gamemode.other")) {
                    var target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        var mode = args[0].toLowerCase();
                        if (target == player) {
                            if (getUserdata(target).setGameMode(mode)) {
                                player.sendMessage(getMessage().get("commands.gamemode.sender", target.getName(), getMessage().toTitleCase(mode)));
                            } else player.sendMessage(getMessage().get("commands.gamemode.invalid", mode));
                        } else if (!target.hasPermission("essentials.command.gamemode.exempt")) {
                            if (getUserdata(target).setGameMode(mode)) {
                                player.sendMessage(getMessage().get("commands.gamemode.sender", target.getName(), getMessage().toTitleCase(mode)));
                            } else player.sendMessage(getMessage().get("commands.gamemode.invalid", mode));
                        } else player.sendMessage(getMessage().get("commands.gamemode.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var target = sender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    var mode = args[0].toLowerCase();
                    if (getUserdata(target).setGameMode(mode)) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.gamemode.sender", target.getName(), getMessage().toTitleCase(mode)));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.gamemode.invalid", mode));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
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
                if (player.hasPermission("essentials.command.gamemode.adventure")) {
                    commands.add("adventure");
                }
                if (player.hasPermission("essentials.command.gamemode.creative")) {
                    commands.add("creative");
                }
                if (player.hasPermission("essentials.command.gamemode.spectator")) {
                    commands.add("spectator");
                }
                if (player.hasPermission("essentials.command.gamemode.survival")) {
                    commands.add("survival");
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.gamemode.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata(target).isVanished()) {
                            if (target.getName().startsWith(args[1])) {
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