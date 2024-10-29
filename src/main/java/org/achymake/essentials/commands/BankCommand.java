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

public class BankCommand implements CommandExecutor, TabCompleter {
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
    public BankCommand() {
        getInstance().getCommand("bank").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                getMessage().send(player, "&6Bank:&a " + getEconomy().currency() + getEconomy().format(getUserdata(player).getBankAccount()));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission("essentials.command.balance.top")) {
                        getMessage().send(player, "&6Top 10 Bank:");
                        var list = new ArrayList<>(getEconomy().getTopAccounts(getEconomy().getBankAccounts(), 10));
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
            } else if (args.length == 2) {
                var amount = Double.parseDouble(args[1]);
                if (args[0].equalsIgnoreCase("withdraw")) {
                    if (amount >= getEconomy().getMinimumBankWithdraw()) {
                        if (getEconomy().hasBank(player, amount)) {
                            getEconomy().removeBank(player, amount);
                            getEconomy().add(player, amount);
                            getMessage().send(player, "&6You withdrew&a " + getEconomy().currency() + getEconomy().format(amount) + "&6 from bank");
                            getMessage().send(player, "&6You now have&a " + getEconomy().currency() + getEconomy().format(getEconomy().getBank(player)));
                        } else getMessage().send(player, "&cYou don't have&a " + getEconomy().currency() + getEconomy().format(amount) + "&c to withdraw");
                    } else getMessage().send(player, "&cYou have to withdraw at least&a " + getEconomy().currency() + getEconomy().format(getEconomy().getMinimumBankWithdraw()));
                    return true;
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (amount >= getEconomy().getMinimumBankDeposit()) {
                        if (getEconomy().has(player, amount)) {
                            getEconomy().remove(player, amount);
                            getEconomy().addBank(player, amount);
                            getMessage().send(player, "&6You deposit&a " + getEconomy().currency() + getEconomy().format(amount) + "&6 to bank");
                            getMessage().send(player, "&6You now have&a " + getEconomy().currency() + getEconomy().format(getEconomy().getBank(player)));
                        } else getMessage().send(player, "&cYou don't have&a " + getEconomy().currency() + getEconomy().format(amount) + "&c to deposit");
                    } else getMessage().send(player, "&cYou have to deposit at least&a " + getEconomy().currency() + getEconomy().format(getEconomy().getMinimumPayment()));
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
                if (player.hasPermission("essentials.command.bank.top")) {
                    commands.add("top");
                }
                if (player.hasPermission("essentials.command.bank.withdraw")) {
                    commands.add("withdraw");
                }
                if (player.hasPermission("essentials.command.bank.deposit")) {
                    commands.add("deposit");
                }
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.bank.withdraw") || player.hasPermission("essentials.command.bank.deposit")) {
                    commands.add("8");
                    commands.add("16");
                    commands.add("32");
                    commands.add("64");
                }
            }
        }
        return commands;
    }
}