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
            if (args.length == 0) {
                if (getSpawn().getLocation() != null) {
                    getUserdata(player).teleport(getSpawn().getLocation(), "spawn", getInstance().getConfig().getInt("teleport.delay"));
                } else player.sendMessage(getMessage().get("commands.spawn.invalid"));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.spawn.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        var spawn = getSpawn().getLocation();
                        if (spawn != null) {
                            if (target == player) {
                                getUserdata(target).teleport(spawn, "spawn");
                            } else if (!target.hasPermission("essentials.command.spawn.exempt")) {
                                getUserdata(target).teleport(spawn, "spawn");
                            } else player.sendMessage(getMessage().get("commands.spawn.exempt", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.spawn.invalid"));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (getSpawn().getLocation() != null) {
                        getUserdata(target).teleport(getSpawn().getLocation(), "spawn", getInstance().getConfig().getInt("teleport.delay"));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.spawn.invalid"));
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