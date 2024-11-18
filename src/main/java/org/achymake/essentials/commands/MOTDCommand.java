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
            if (args.length == 0) {
                getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day.welcome"));
                return true;
            } else if (args.length == 1) {
                var motd = args[0];
                if (getConfig().isList("message-of-the-day." + motd)) {
                    getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day." + motd));
                } else player.sendMessage(getMessage().get("commands.motd.invalid", motd));
                return true;
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.motd.other")) {
                    var target = getInstance().getPlayer(args[1]);
                    var motd = args[0];
                    if (target != null) {
                        if (target == player) {
                            if (getConfig().isList("message-of-the-day." + motd)) {
                                getMessage().sendStringList(target, getConfig().getStringList("message-of-the-day." + motd));
                            } else player.sendMessage(getMessage().get("commands.motd.invalid", motd));
                        } else if (!target.hasPermission("essentials.command.motd.exempt")) {
                            if (getConfig().isList("message-of-the-day." + motd)) {
                                getMessage().sendStringList(target, getConfig().getStringList("message-of-the-day." + motd));
                            } else player.sendMessage(getMessage().get("commands.motd.invalid", motd));
                        } else player.sendMessage(getMessage().get("commands.motd.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[1]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().sendStringList(consoleCommandSender, getConfig().getStringList("message-of-the-day.welcome"));
                return true;
            } else if (args.length == 1) {
                var motd = args[0];
                if (getConfig().isList("message-of-the-day." + motd)) {
                    getMessage().sendStringList(consoleCommandSender, getConfig().getStringList("message-of-the-day." + motd));
                } else consoleCommandSender.sendMessage(getMessage().get("commands.motd.invalid", motd));
                return true;
            } else if (args.length == 2) {
                var target = getInstance().getPlayer(args[1]);
                if (target != null) {
                    var motd = args[0];
                    if (getConfig().isList("message-of-the-day." + motd)) {
                        getMessage().sendStringList(target, getConfig().getStringList("message-of-the-day." + motd));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.motd.invalid", motd));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[1]));
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
                commands.addAll(getConfig().getConfigurationSection("message-of-the-day").getKeys(false));
            } else if (args.length == 2) {
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