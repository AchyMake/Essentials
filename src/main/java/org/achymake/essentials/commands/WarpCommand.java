package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.data.Warps;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Warps getWarps() {
        return getInstance().getWarps();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public WarpCommand() {
        getInstance().getCommand("warp").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (!getWarps().getListed().isEmpty()) {
                    player.sendMessage(getMessage().get("commands.warp.title"));
                    getMessage().send(player, "&6Warps:");
                    getWarps().getListed().forEach(warps -> {
                        if (player.hasPermission("essentials.command.warp." + warps)) {
                            player.sendMessage(getMessage().get("commands.warp.listed", warps));
                        }
                    });
                } else player.sendMessage(getMessage().get("commands.warp.empty"));
                return true;
            } else if (args.length == 1) {
                var warpName = args[0].toLowerCase();
                if (player.hasPermission("essentials.command.warp." + warpName)) {
                    var warp = getWarps().getLocation(warpName);
                    if (warp != null) {
                        getUserdata(player).teleport(warp, warpName, getInstance().getConfig().getInt("teleport.delay"));
                    } else player.sendMessage(getMessage().get("commands.warp.invalid", warpName));
                    return true;
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.warp.other")) {
                    var target = player.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        var warpName = args[0].toLowerCase();
                        var warp = getWarps().getLocation(warpName);
                        if (target == player) {
                            if (warp != null) {
                                getUserdata(target).teleport(warp, warpName, getInstance().getConfig().getInt("teleport.delay"));
                            } else player.sendMessage(getMessage().get("commands.warp.invalid", warpName));
                        } else if (!target.hasPermission("essentials.command.warp.exempt")) {
                            if (warp != null) {
                                getUserdata(target).teleport(warp, warpName, getInstance().getConfig().getInt("teleport.delay"));
                            } else player.sendMessage(getMessage().get("commands.warp.invalid", warpName));
                        } else player.sendMessage(getMessage().get("commands.warp.exempt", target.getName()));
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var target = consoleCommandSender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    var userdataTarget = getUserdata(target);
                    var warpName = args[0].toLowerCase();
                    var warp = getWarps().getLocation(warpName);
                    if (warp != null) {
                        userdataTarget.teleport(warp, warpName, getInstance().getConfig().getInt("teleport.delay"));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.warp.invalid", warpName));
                    return true;
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
                getWarps().getListed().forEach(warps -> {
                    if (player.hasPermission("essentials.command.warp." + warps)) {
                        if (warps.startsWith(args[0])) {
                            commands.add(warps);
                        }
                    }
                });
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.warp.other")) {
                    getInstance().getOnlinePlayers().forEach(players -> {
                        if (!getUserdata(players).isVanished()) {
                            if (players.getName().startsWith(args[0])) {
                                commands.add(players.getName());
                            }
                        }
                    });
                }
            }
        }
        return commands;
    }
}