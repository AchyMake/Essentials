package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.data.Warps;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DelWarpCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Warps getWarps() {
        return getInstance().getWarps();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public DelWarpCommand() {
        getInstance().getCommand("delwarp").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 1) {
                var warpName = args[0].toLowerCase();
                if (getWarps().isListed(warpName)) {
                    getWarps().setLocation(warpName, null);
                    getMessage().send(player, warpName + "&6 has been deleted");
                } else getMessage().send(player, warpName + "&c does not exist");
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var warpName = args[0].toLowerCase();
                if (getWarps().isListed(warpName)) {
                    getWarps().setLocation(warpName, null);
                    consoleCommandSender.sendMessage(warpName + " has been deleted");
                } else consoleCommandSender.sendMessage(warpName + " does not exist");
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
                commands.addAll(getWarps().getListed());
            }
        }
        return commands;
    }
}