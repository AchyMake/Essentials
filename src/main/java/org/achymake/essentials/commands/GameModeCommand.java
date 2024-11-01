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
            if (args.length == 1) {
                var mode = args[0].toLowerCase();
                if (player.hasPermission("essentials.command.gamemode." + mode)) {
                    if (mode.equalsIgnoreCase("adventure")) {
                        player.setGameMode(GameMode.ADVENTURE);
                        getMessage().sendActionBar(player, getMessage().get("commands.gamemode.adventure"));
                        return true;
                    } else if (mode.equalsIgnoreCase("creative")) {
                        player.setGameMode(GameMode.CREATIVE);
                        getMessage().sendActionBar(player, getMessage().get("commands.gamemode.creative"));
                        return true;
                    } else if (mode.equalsIgnoreCase("spectator")) {
                        player.setGameMode(GameMode.SPECTATOR);
                        getMessage().sendActionBar(player, getMessage().get("commands.gamemode.spectator"));
                        return true;
                    } else if (mode.equalsIgnoreCase("survival")) {
                        player.setGameMode(GameMode.SURVIVAL);
                        getMessage().sendActionBar(player, getMessage().get("commands.gamemode.survival"));
                        return true;
                    }
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.gamemode.other")) {
                    var target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        var mode = args[0].toLowerCase();
                        if (target == player) {
                            setMode(target, mode);
                        } else if (!target.hasPermission("essentials.command.gamemode.exempt")) {
                            setMode(target, mode);
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
                    setMode(target, mode);
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
    private void setMode(Player target, String mode) {
        if (mode.equalsIgnoreCase("adventure")) {
            target.setGameMode(GameMode.ADVENTURE);
            getMessage().sendActionBar(target, getMessage().get("commands.gamemode.adventure"));
        } else if (mode.equalsIgnoreCase("creative")) {
            target.setGameMode(GameMode.CREATIVE);
            getMessage().sendActionBar(target, getMessage().get("commands.gamemode.creative"));
        } else if (mode.equalsIgnoreCase("spectator")) {
            target.setGameMode(GameMode.SPECTATOR);
            getMessage().sendActionBar(target, getMessage().get("commands.gamemode.spectator"));
        } else if (mode.equalsIgnoreCase("survival")) {
            target.setGameMode(GameMode.SURVIVAL);
            getMessage().sendActionBar(target, getMessage().get("commands.gamemode.survival"));
        }
    }
}