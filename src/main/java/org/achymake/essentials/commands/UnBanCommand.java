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
            if (args.length == 1) {
                var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                var userdataOffline = getUserdata(offlinePlayer);
                if (userdataOffline.exists()) {
                    if (userdataOffline.isBanned()) {
                        userdataOffline.setBoolean("settings.banned", false);
                        userdataOffline.setString("settings.ban-reason", null);
                        userdataOffline.setInt("settings.ban-expire", 0);
                        player.sendMessage(getMessage().get("commands.unban.banned", offlinePlayer.getName()));
                    } else player.sendMessage(getMessage().get("commands.unban.unbanned", offlinePlayer.getName()));
                }
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                var userdataOffline = getUserdata(offlinePlayer);
                if (userdataOffline.exists()) {
                    if (userdataOffline.isBanned()) {
                        userdataOffline.setBoolean("settings.banned", false);
                        userdataOffline.setString("settings.ban-reason", null);
                        userdataOffline.setInt("settings.ban-expire", 0);
                        consoleCommandSender.sendMessage(getMessage().get("commands.unban.banned", offlinePlayer.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.unban.unbanned", offlinePlayer.getName()));
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                getInstance().getOfflinePlayers().forEach(offlinePlayer -> {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.isBanned()) {
                        if (userdataOffline.getName().startsWith(args[0])) {
                            commands.add(userdataOffline.getName());
                        }
                    }
                });
            }
        }
        return commands;
    }
}