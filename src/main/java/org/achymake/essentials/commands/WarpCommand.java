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
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                if (!getWarps().getListed().isEmpty()) {
                    getMessage().send(player, "&6Warps:");
                    getWarps().getListed().forEach(warps -> {
                        if (player.hasPermission("essentials.command.warp." + warps)) {
                            getMessage().send(player, "- " + warps);
                        }
                    });
                } else getMessage().send(player, "&cWarps is currently empty");
                return true;
            } else if (args.length == 1) {
                var warpName = args[0].toLowerCase();
                if (player.hasPermission("players.command.warp." + warpName)) {
                    var warp = getWarps().getLocation(warpName);
                    if (warp != null) {
                        userdata.teleport(warp, warpName, getInstance().getConfig().getInt("teleport.delay"));
                    } else getMessage().send(player, warpName + "&c does not exist");
                    return true;
                }
            } else if (args.length == 2) {
                if (player.hasPermission("players.command.warp.other")) {
                    var target = player.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (target == player) {
                            var userdataTarget = getUserdata(target);
                            if (!userdataTarget.isDisabled()) {
                                var warpName = args[0].toLowerCase();
                                var warp = getWarps().getLocation(warpName);
                                if (warp != null) {
                                    userdataTarget.teleport(warp, warpName, getInstance().getConfig().getInt("teleport.delay"));
                                } else getMessage().send(player, warpName + "&c does not exist");
                            }
                        } else if (!target.hasPermission("essentials.command.warp.exempt")) {
                            var userdataTarget = getUserdata(target);
                            if (!userdataTarget.isDisabled()) {
                                var warpName = args[0].toLowerCase();
                                var warp = getWarps().getLocation(warpName);
                                if (warp != null) {
                                    userdataTarget.teleport(warp, warpName, getInstance().getConfig().getInt("teleport.delay"));
                                } else getMessage().send(player, warpName + "&c does not exist");
                            }
                        } else getMessage().send(player, "&cYou are not allowed to warp&f " + target.getName());
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var target = consoleCommandSender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    var userdataTarget = getUserdata(target);
                    if (!userdataTarget.isDisabled()) {
                        var warpName = args[0].toLowerCase();
                        var warp = getWarps().getLocation(warpName);
                        if (warp != null) {
                            userdataTarget.teleport(warp, warpName, getInstance().getConfig().getInt("teleport.delay"));
                        } else consoleCommandSender.sendMessage(warpName + " does not exist");
                        return true;
                    }
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