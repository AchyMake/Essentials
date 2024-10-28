package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BackCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public BackCommand() {
        getInstance().getCommand("back").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                teleportBack(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.back.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            teleportBack(player);
                        } else if (target.hasPermission("essentials.command.back.exempt")) {
                            getMessage().send(player, command.getPermissionMessage());
                        } else {
                            teleportBack(target);
                            getMessage().send(player, target.getName() + "&6 has been teleported back");
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender commandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getUserdata(target).isDisabled()) {
                        commandSender.sendMessage(command.getPermissionMessage());
                    } else teleportBack(target);
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
                if (player.hasPermission("essentials.command.back.other")) {
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
    private void teleportBack(Player player) {
        var userdata = getUserdata(player);
        var recent = userdata.getLocation("recent");
        var delay = getInstance().getConfig().getInt("teleport.delay");
        if (player.hasPermission("essentials.command.back.death")) {
            var deathLocation = userdata.getLocation("death");
            if (deathLocation != null) {
                userdata.teleport(deathLocation, "death", delay);
                userdata.setString("locations.death", null);
            } else if (recent != null) {
                var worldName = recent.getWorld().getName();
                if (!player.hasPermission("essentials.command.back.world." + worldName))return;
                userdata.teleport(recent, "recent", delay);
            }
        } else if (recent != null) {
            var worldName = recent.getWorld().getName();
            if (!player.hasPermission("essentials.command.back.world." + worldName))return;
            userdata.teleport(recent, "recent", delay);
        }
    }
}