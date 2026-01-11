package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
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

public class SellCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private Worth getWorth() {
        return getInstance().getWorth();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SellCommand() {
        getInstance().getCommand("sell").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                var heldItem = player.getInventory().getItemInMainHand();
                if (!getMaterialHandler().isAir(heldItem)  && !heldItem.getItemMeta().hasEnchants()) {
                    var itemName = getMessage().toTitleCase(heldItem.getType().toString());
                    if (getWorth().isListed(heldItem.getType())) {
                        var amount = heldItem.getAmount();
                        var result = getWorth().get(heldItem.getType()) * amount;
                        if (getEconomy().add(player, result)) {
                            heldItem.setAmount(0);
                            player.sendMessage(getMessage().get("commands.sell.sellable", String.valueOf(amount), itemName, getEconomy().currency() + getEconomy().format(result)));
                        } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                    } else player.sendMessage(getMessage().get("commands.sell.unsellable", itemName));
                } else player.sendMessage(getMessage().get("error.item.invalid"));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("all")) {
                    var inventory = player.getInventory();
                    for (var itemStack : inventory.getStorageContents()) {
                        if (itemStack != null && !itemStack.getItemMeta().hasEnchants()) {
                            var material = itemStack.getType();
                            var itemName = getMessage().toTitleCase(material.toString());
                            if (getWorth().isListed(material)) {
                                var amount = itemStack.getAmount();
                                var result = getWorth().get(material) * amount;
                                if (getEconomy().add(player, result)) {
                                    itemStack.setAmount(0);
                                    player.sendMessage(getMessage().get("commands.sell.sellable", String.valueOf(amount), itemName, getEconomy().currency() + getEconomy().format(result)));
                                } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                            }
                        }
                    }
                    return true;
                } else {
                    var amount = Integer.parseInt(args[0]);
                    if (amount > 0) {
                        var heldItem = player.getInventory().getItemInMainHand();
                        if (!getMaterialHandler().isAir(heldItem) && !heldItem.getItemMeta().hasEnchants()) {
                            var itemName = getMessage().toTitleCase(heldItem.getType().toString());
                            var itemAmount = heldItem.getAmount();
                            if (getWorth().isListed(heldItem.getType())) {
                                if (itemAmount >= amount) {
                                    var result = getWorth().get(heldItem.getType()) * amount;
                                    var newAmount = itemAmount - amount;
                                    if (getEconomy().add(player, result)) {
                                        heldItem.setAmount(newAmount);
                                        player.sendMessage(getMessage().get("commands.sell.sellable", String.valueOf(amount), itemName, getEconomy().currency() + getEconomy().format(result)));
                                    } else player.sendMessage(getMessage().get("error.file.exception", getUserdata().getFile(player).getName()));
                                } else player.sendMessage(getMessage().get("commands.sell.insufficient", itemName));
                            } else player.sendMessage(getMessage().get("commands.sell.unsellable", itemName));
                        } else player.sendMessage(getMessage().get("error.item.invalid"));
                        return true;
                    }
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
                commands.add("8");
                commands.add("16");
                commands.add("32");
                commands.add("64");
                commands.add("all");
            }
        }
        return commands;
    }
}