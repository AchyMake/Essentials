package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.GameMode;
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
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 1) {
                var mode = args[0].toLowerCase();
                if (player.hasPermission("essentials.command.gamemode." + mode)) {
                    return setGameMode(player, mode);
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.gamemode.other")) {
                    var target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (target == player) {
                            var mode = args[0].toLowerCase();
                            return setGameMode(target, mode);
                        } else if (target.hasPermission("essentials.command.gamemode.exempt")) {
                            getMessage().send(player, command.getPermissionMessage());
                            return true;
                        } else {
                            var mode = args[0].toLowerCase();
                            return setGameMode(target, mode);
                        }
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 2) {
                var target = sender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    var mode = args[0].toLowerCase();
                    return setGameMode(target, mode);
                }
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
    private boolean setGameMode(Player player, String gameMode) {
        if (gameMode.equalsIgnoreCase("adventure")) {
            player.setGameMode(GameMode.ADVENTURE);
            return true;
        } else if (gameMode.equalsIgnoreCase("creative")) {
            player.setGameMode(GameMode.CREATIVE);
            return true;
        } else if (gameMode.equalsIgnoreCase("spectator")) {
            player.setGameMode(GameMode.SPECTATOR);
            return true;
        } else if (gameMode.equalsIgnoreCase("survival")) {
            player.setGameMode(GameMode.SURVIVAL);
            return true;
        } else return false;
    }
}