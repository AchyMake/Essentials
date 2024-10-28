package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
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
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                getMessage().send(player, "&6Balance:&a " + getEconomy().currency() + getEconomy().format(userdata.getAccount()));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission("essentials.command.balance.top")) {
                        getMessage().send(player, "&6Top 10 Balance:");
                        var list = new ArrayList<>(getEconomy().getTopAccounts(getEconomy().getAccounts(), 10));
                        for (int i = 0; i < list.size(); i++) {
                            var test = list.get(i);
                            var offlinePlayer = test.getKey();
                            var economy = test.getValue();
                            var placed = i + 1;
                            getMessage().send(player, "&6" + placed + "&f " + offlinePlayer.getName() + "&a " + getEconomy().currency() + getEconomy().format(economy));
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender commandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    commandSender.sendMessage("&6Top 10 Balance:");
                    var list = new ArrayList<>(getEconomy().getTopAccounts(getEconomy().getAccounts(), 10));
                    for (int i = 0; i < list.size(); i++) {
                        var test = list.get(i);
                        var offlinePlayer = test.getKey();
                        var economy = test.getValue();
                        var placed = i + 1;
                        commandSender.sendMessage("&6" + placed + "&f " + offlinePlayer.getName() + "&a " + getEconomy().currency() + getEconomy().format(economy));
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