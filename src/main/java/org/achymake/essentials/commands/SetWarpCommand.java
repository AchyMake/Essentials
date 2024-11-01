package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Warps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetWarpCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Warps getWarps() {
        return getInstance().getWarps();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SetWarpCommand() {
        getInstance().getCommand("setwarp").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var warpName = args[0].toLowerCase();
                if (getWarps().getLocation(warpName) != null) {
                    getWarps().setLocation(warpName, player.getLocation());
                    player.sendMessage(getMessage().get("commands.setwarp.relocated", warpName));
                } else {
                    getWarps().setLocation(warpName, player.getLocation());
                    player.sendMessage(getMessage().get("commands.setwarp.set", warpName));
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
                commands.addAll(getWarps().getListed());
            }
        }
        return commands;
    }
}