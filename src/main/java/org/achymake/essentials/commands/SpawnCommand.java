package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Spawn;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Spawn getSpawn() {
        return getInstance().getSpawn();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SpawnCommand() {
        getInstance().getCommand("spawn").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                if (getSpawn().getLocation() != null) {
                    userdata.teleport(getSpawn().getLocation(), "spawn", getInstance().getConfig().getInt("teleport.delay"));
                } else getMessage().send(player, "Spawn&c does not exist");
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.spawn.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        if (!userdataTarget.isDisabled()) {
                            if (getSpawn().getLocation() != null) {
                                if (!target.hasPermission("essentials.command.spawn.exempt")) {
                                    userdataTarget.teleport(getSpawn().getLocation(), "spawn", getInstance().getConfig().getInt("teleport.delay"));
                                } else getMessage().send(player, "&cYou are not allowed to spawn&f " + target.getName());
                            } else getMessage().send(player, "Spawn&c does not exist");
                        } else getMessage().send(player, target.getName() + "&c is either in jail or frozen");
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    var userdataTarget = getUserdata(target);
                    if (!userdataTarget.isDisabled()) {
                        if (getSpawn().getLocation() != null) {
                            userdataTarget.teleport(getSpawn().getLocation(), "spawn", getInstance().getConfig().getInt("teleport.delay"));
                        } else consoleCommandSender.sendMessage(target.getName() + " is either in jail or frozen");
                    } else consoleCommandSender.sendMessage("Spawn does not exist");
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
                if (player.hasPermission("essentials.command.spawn.other")) {
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