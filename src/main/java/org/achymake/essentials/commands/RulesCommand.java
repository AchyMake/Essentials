package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RulesCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public RulesCommand() {
        getInstance().getCommand("rules").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().sendStringList(player, getInstance().getConfig().getStringList("message-of-the-day.rules"));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.rules.other")) {
                    var target = player.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            getMessage().sendStringList(target, getInstance().getConfig().getStringList("message-of-the-day.rules"));
                            player.sendMessage(getMessage().get("commands.rules.sender", target.getName()));
                        } else if (!target.hasPermission("essentials.command.rules.exempt")) {
                            getMessage().sendStringList(target, getInstance().getConfig().getStringList("message-of-the-day.rules"));
                            player.sendMessage(getMessage().get("commands.rules.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.rules.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                getMessage().sendStringList(consoleCommandSender, getInstance().getConfig().getStringList("message-of-the-day.rules"));
                return true;
            } else if (args.length == 1) {
                var target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getMessage().sendStringList(target, getInstance().getConfig().getStringList("message-of-the-day.rules"));
                    consoleCommandSender.sendMessage(getMessage().get("commands.rules.sender", target.getName()));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (sender.hasPermission("essentials.command.rules.other")) {
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