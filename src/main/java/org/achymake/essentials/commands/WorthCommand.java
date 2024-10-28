package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.data.Worth;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorthCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(Player player) {
        return getInstance().getUserdata(player);
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private Worth getWorth() {
        return getInstance().getWorth();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public WorthCommand() {
        getInstance().getCommand("worth").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 1) {
                var material = getMaterialHandler().getMaterial(args[0]);
                var materialName = getMessage().toTitleCase(material.toString());
                if (getWorth().isListed(material)) {
                    getMessage().send(player, materialName + "&6 is worth:&a " + getEconomy().currency() + getEconomy().format(getWorth().get(material)));
                } else getMessage().send(player, materialName + "&c is not sellable");
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var material = getMaterialHandler().getMaterial(args[0]);
                var materialName = getMessage().toTitleCase(material.toString());
                if (getWorth().isListed(material)) {
                    consoleCommandSender.sendMessage(materialName + " is worth: " + getEconomy().currency() + getEconomy().format(getWorth().get(material)));
                } else consoleCommandSender.sendMessage(materialName + " is not sellable");
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
                getWorth().getListed().forEach(listed -> {
                    var lowered = listed.toLowerCase();
                    if (lowered.startsWith(args[0])) {
                        commands.add(lowered);
                    }
                });
            }
        }
        return commands;
    }
}