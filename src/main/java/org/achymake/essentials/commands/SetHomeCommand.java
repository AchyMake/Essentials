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

public class SetHomeCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SetHomeCommand() {
        getInstance().getCommand("sethome").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                if (userdata.setHome("home")) {
                    getMessage().send(player, "home&6 has been set");
                } else getMessage().send(player, "&cYou have reach your limit of&f " + userdata.getHomes().size() + "&c homes");
                return true;
            } else if (args.length == 1) {
                var homeName = args[0];
                if (!homeName.equalsIgnoreCase("bed")) {
                    if (userdata.setHome(homeName)) {
                        getMessage().send(player, homeName + "&6 has been set");
                    } else getMessage().send(player, "&cYou have reach your limit of&f " + userdata.getHomes().size() + "&c homes");
                } else getMessage().send(player, "&cYou can not set home for&f " + homeName);
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
                commands.addAll(getUserdata(player).getHomes());
            }
        }
        return commands;
    }
}