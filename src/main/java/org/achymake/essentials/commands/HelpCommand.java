package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public HelpCommand() {
        getInstance().getCommand("help").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                getMessage().sendStringList(player, getInstance().getConfig().getStringList("help"));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.help.others")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            getMessage().sendStringList(target, getInstance().getConfig().getStringList("help"));
                        } else if (!target.hasPermission("essentials.command.help.exempt")) {
                            getMessage().sendStringList(target, getInstance().getConfig().getStringList("help"));
                        } else getMessage().send(player, command.getPermissionMessage());
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().sendStringList(consoleCommandSender, getInstance().getConfig().getStringList("help"));
                return true;
            } else if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getMessage().sendStringList(target, getInstance().getConfig().getStringList("help"));
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
                if (player.hasPermission("essentials.command.help.other")) {
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