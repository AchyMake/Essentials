package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MuteCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public MuteCommand() {
        getInstance().getCommand("mute").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var userdataTarget = getUserdata(target);
                    if (target == player) {
                        userdataTarget.setBoolean("settings.muted", !userdataTarget.isMuted());
                        if (userdataTarget.isMuted()) {
                            player.sendMessage(getMessage().get("commands.mute.enable", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.mute.disable", target.getName()));
                    } else if (!target.hasPermission("essentials.command.mute.exempt")) {
                        userdataTarget.setBoolean("settings.muted", !userdataTarget.isMuted());
                        if (userdataTarget.isMuted()) {
                            player.sendMessage(getMessage().get("commands.mute.enable", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.mute.disable", target.getName()));
                    } else player.sendMessage(getMessage().get("commands.mute.exempt", target.getName()));
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setBoolean("settings.muted", !userdataOffline.isMuted());
                        if (userdataOffline.isMuted()) {
                            player.sendMessage(getMessage().get("commands.mute.enable", offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("commands.mute.disable", offlinePlayer.getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var userdataTarget = getUserdata(target);
                    userdataTarget.setBoolean("settings.muted", !userdataTarget.isMuted());
                    if (userdataTarget.isMuted()) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.mute.enable", target.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.mute.disable", target.getName()));
                } else {
                    var offlinePlayer = getInstance().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setBoolean("settings.muted", !userdataOffline.isMuted());
                        if (userdataOffline.isMuted()) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.mute.enable", offlinePlayer.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.mute.disable", offlinePlayer.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
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
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata(target).isVanished()) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            }
        }
        return commands;
    }
}