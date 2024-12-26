package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public BalanceCommand() {
        getInstance().getCommand("balance").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.sendMessage(getMessage().get("commands.balance.self", getEconomy().currency() + getEconomy().format(getUserdata().getAccount(player))));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission("essentials.command.balance.top")) {
                        player.sendMessage(getMessage().get("commands.balance.top.title"));
                        var list = new ArrayList<>(getEconomy().getTopAccounts(getEconomy().getAccounts(), 10));
                        for (int i = 0; i < list.size(); i++) {
                            player.sendMessage(getMessage().get("commands.balance.top.listed", String.valueOf(i + 1), list.get(i).getKey().getName(), getEconomy().currency() + getEconomy().format(list.get(i).getValue())));
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    consoleCommandSender.sendMessage(getMessage().get("commands.balance.top.title"));
                    var list = new ArrayList<>(getEconomy().getTopAccounts(getEconomy().getAccounts(), 10));
                    for (int i = 0; i < list.size(); i++) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.balance.top.listed", String.valueOf(i + 1), list.get(i).getKey().getName(), getEconomy().currency() + getEconomy().format(list.get(i).getValue())));
                    }
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
                if (player.hasPermission("essentials.command.balance.top")) {
                    commands.add("top");
                }
            }
        }
        return commands;
    }
}