package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Worth;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetWorthCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Worth getWorth() {
        return getInstance().getWorth();
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SetWorthCommand() {
        getInstance().getCommand("setworth").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var heldItem = player.getInventory().getItemInMainHand();
                if (!getMaterials().isAir(heldItem)) {
                    var itemName = getMessage().toTitleCase(heldItem.toString());
                    var value = Double.parseDouble(args[0]);
                    getWorth().setWorth(heldItem, value);
                    if (getWorth().isListed(heldItem.getType())) {
                        player.sendMessage(getMessage().get("commands.setworth.enable", itemName, getEconomy().currency() + getEconomy().format(getWorth().get(heldItem.getType()))));
                    } else player.sendMessage(getMessage().get("commands.setworth.disable", itemName));
                } else player.sendMessage(getMessage().get("error.item.invalid"));
                return true;
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (!player.getInventory().getItemInMainHand().isEmpty()) {
                if (args.length == 1) {
                    commands.add("0.25");
                    commands.add("0.50");
                    commands.add("0.75");
                    commands.add("1.00");
                }
            }
        }
        return commands;
    }
}