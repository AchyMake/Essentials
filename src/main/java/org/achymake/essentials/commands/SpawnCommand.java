package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Spawn;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Spawn getSpawn() {
        return getInstance().getSpawn();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
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
                    getWorldHandler().teleport(player, getSpawn().getLocation(), "spawn", getInstance().getConfig().getInt("teleport.delay"));
                } else player.sendMessage(getMessage().get("commands.spawn.invalid"));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.spawn.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        var spawn = getSpawn().getLocation();
                        if (spawn != null) {
                            if (target == player) {
                                getWorldHandler().teleport(target, spawn, "spawn", 0);
                            } else if (!target.hasPermission("essentials.command.spawn.exempt")) {
                                getWorldHandler().teleport(target, spawn, "spawn", 0);
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
                        getWorldHandler().teleport(target, getSpawn().getLocation(), "spawn", getInstance().getConfig().getInt("teleport.delay"));
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
                        if (!getUserdata().isVanished(target)) {
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