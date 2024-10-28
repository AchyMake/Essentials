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
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                var name = player.getName();
                var displayName = userdata.getDisplayName();
                if (!displayName.equals(name)) {
                    userdata.setString("display-name", name);
                    getMessage().send(player, "&6You reset your nickname");
                    return true;
                }
            } else if (args.length == 1) {
                var rename = args[0];
                var displayName = userdata.getDisplayName();
                if (!displayName.equals(rename)) {
                    userdata.setString("display-name", rename);
                    getMessage().send(player, "&6You changed your nickname to&f " + rename);
                } else getMessage().send(player, "&cYou already have&f " + rename + "&c as nickname");
                return true;
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.nickname.other")) {
                    var target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        var displayName = userdataTarget.getDisplayName();
                        if (!target.hasPermission("essentials.command.nickname.exempt")) {
                            if (!displayName.equals(args[0])) {
                                userdataTarget.setString("display-name", args[0]);
                                getMessage().send(player, "&6You changed " + target.getName() + " nickname to&f " + args[0]);
                            } else getMessage().send(player, target.getName() + "&c already have&f " + args[0] + "&c as nickname");
                        } else getMessage().send(player, command.getPermissionMessage());
                    } else getMessage().send(player, args[1] + "&c is currently offline");
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