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

public class HomeCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public HomeCommand() {
        getInstance().getCommand("home").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var delay = getInstance().getConfig().getInt("teleport.delay");
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                var location = getUserdata(player).getHome("home");
                if (location != null) {
                    userdata.teleport(location, "home", delay);
                } else getMessage().send(player, "home&c does not exist");
                return true;
            } else if (args.length == 1) {
                var homeName = args[0].toLowerCase();
                if (homeName.equalsIgnoreCase("bed")) {
                    if (player.hasPermission("essentials.command.home.bed")) {
                        var location = player.getBedSpawnLocation();
                        if (location != null) {
                            userdata.teleport(location, "bed", delay);
                        } else getMessage().send(player, "bed&c does not exist");
                        return true;
                    }
                } else {
                    var location = getUserdata(player).getHome(homeName);
                    if (location != null) {
                        userdata.teleport(location, homeName, delay);
                    } else getMessage().send(player, homeName + "&c does not exist");
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
                if (player.hasPermission("essentials.command.home.bed")) {
                    commands.add("bed");
                }
                commands.addAll(getUserdata(player).getHomes());
            }
        }
        return commands;
    }
}