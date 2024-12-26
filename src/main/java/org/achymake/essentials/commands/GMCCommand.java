package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GMCCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public GMCCommand() {
        getInstance().getCommand("gmc").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getUserdata().setGameMode(player, "creative");
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.gamemode.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            getUserdata().setGameMode(target, "creative");
                            player.sendMessage(getMessage().get("commands.gamemode.sender", target.getName(), getMessage().get("gamemode.creative")));
                        } else if (!target.hasPermission("essentials.command.gamemode.exempt")) {
                            getUserdata().setGameMode(target, "creative");
                            player.sendMessage(getMessage().get("commands.gamemode.sender", target.getName(), getMessage().get("gamemode.creative")));
                        } else player.sendMessage(getMessage().get("commands.gamemode.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    getUserdata().setGameMode(target, "creative");
                    consoleCommandSender.sendMessage(getMessage().get("commands.gamemode.sender", target.getName(), getMessage().get("gamemode.creative")));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", args[0]));
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
                if (player.hasPermission("essentials.command.gamemode.other")) {
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