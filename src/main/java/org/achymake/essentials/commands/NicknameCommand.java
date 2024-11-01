package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NicknameCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public NicknameCommand() {
        getInstance().getCommand("nickname").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (args.length == 0) {
                var name = userdata.getName();
                var displayName = userdata.getDisplayName();
                if (!displayName.equals(name)) {
                    userdata.setString("display-name", name);
                    player.sendMessage(getMessage().get("commands.nickname.self", name));
                    return true;
                }
            } else if (args.length == 1) {
                var rename = args[0];
                userdata.setString("display-name", rename);
                player.sendMessage(getMessage().get("commands.nickname.self", rename));
                return true;
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.nickname.other")) {
                    var target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        var rename = args[0];
                        var userdataTarget = getUserdata(target);
                        if (!target.hasPermission("essentials.command.nickname.exempt")) {
                            userdataTarget.setString("display-name", rename);
                            player.sendMessage(getMessage().get("commands.nickname.sender", target.getName(), rename));
                        } else player.sendMessage(getMessage().get("commands.nickname.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[1]));
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
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata(target).isVanished()) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.nickname.other")) {
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