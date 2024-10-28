package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MOTDCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public MOTDCommand() {
        getInstance().getCommand("motd").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day.welcome"));
                return true;
            } else if (args.length == 1) {
                getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day." + args[0]));
                return true;
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.motd.other")) {
                    var target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        if (target == player) {
                            getMessage().sendStringList(target, getConfig().getStringList("message-of-the-day." + args[0]));
                        } else if (!target.hasPermission("essentials.command.motd.exempt")) {
                            getMessage().sendStringList(target, getConfig().getStringList("message-of-the-day." + args[0]));
                        } else getMessage().send(player, command.getPermissionMessage());
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().sendStringList(consoleCommandSender, getInstance().getConfig().getStringList("message-of-the-day.welcome"));
                return true;
            } else if (args.length == 1) {
                getMessage().sendStringList(consoleCommandSender, getInstance().getConfig().getStringList("message-of-the-day." + args[0]));
                return true;
            } else if (args.length == 2) {
                var target = sender.getServer().getPlayerExact(args[1]);
                if (target != null) {
                    getMessage().sendStringList(target, getInstance().getConfig().getStringList("message-of-the-day." + args[0]));
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
                commands.addAll(getConfig().getConfigurationSection("message-of-the-day").getKeys(false));
            }
            if (args.length == 2) {
                if (player.hasPermission("essentials.command.motd.other")) {
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
}