package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UnBanCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public UnBanCommand() {
        getInstance().getCommand("unban").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 1) {
                var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                var userdataOffline = getUserdata(offlinePlayer);
                if (userdataOffline.isBanned()) {
                    userdataOffline.setBoolean("settings.banned", false);
                    userdataOffline.setString("settings.ban-reason", "");
                    getMessage().send(player, offlinePlayer.getName() + "&6 is no longer banned");
                } else getMessage().send(player, offlinePlayer.getName() + "&c is not banned");
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                var userdataOffline = getUserdata(offlinePlayer);
                if (userdataOffline.isBanned()) {
                    userdataOffline.setBoolean("settings.banned", false);
                    userdataOffline.setString("settings.ban-reason", "null");
                    consoleCommandSender.sendMessage(offlinePlayer.getName() + " is no longer banned");
                } else consoleCommandSender.sendMessage(offlinePlayer.getName() + " is not banned");
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
                for (var offlinePlayer : sender.getServer().getOfflinePlayers()) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.isBanned()) {
                        if (offlinePlayer.getName().startsWith(args[0])) {
                            commands.add(offlinePlayer.getName());
                        }
                    }
                }
            }
        }
        return commands;
    }
}