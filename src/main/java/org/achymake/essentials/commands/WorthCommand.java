package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
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
    private MaterialHandler getMaterials() {
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
            if (args.length == 1) {
                var itemName = getMessage().toTitleCase(args[0]);
                if (getWorth().isListed(getMaterials().get(args[0]))) {
                    player.sendMessage(getMessage().get("commands.worth.listed", itemName, getEconomy().currency() + getEconomy().format(getWorth().get(getMaterials().get(itemName)))));
                } else player.sendMessage(getMessage().get("commands.worth.unlisted", itemName));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var itemName = getMessage().toTitleCase(args[0]);
                if (getWorth().isListed(getMaterials().get(itemName))) {
                    consoleCommandSender.sendMessage(getMessage().get("commands.worth.listed", itemName, getEconomy().currency() + getEconomy().format(getWorth().get(getMaterials().get(itemName)))));
                } else consoleCommandSender.sendMessage(getMessage().get("commands.worth.unlisted", itemName));
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
                var itemName = args[0];
                getWorth().getListed().forEach(listed -> {
                    var lowered = listed.toLowerCase();
                    if (lowered.startsWith(itemName)) {
                        commands.add(lowered);
                    }
                });
            }
        }
        return commands;
    }
}