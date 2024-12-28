package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BankCommand implements CommandExecutor, TabCompleter {
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
    public BankCommand() {
        getInstance().getCommand("bank").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.sendMessage(getMessage().get("commands.bank.self", getEconomy().currency() + getEconomy().format(getUserdata().getBankAccount(player))));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission("essentials.command.balance.top")) {
                        player.sendMessage(getMessage().get("commands.bank.top.title"));
                        var list = new ArrayList<>(getEconomy().getTopAccounts(getEconomy().getBankAccounts(), 10));
                        for (int i = 0; i < list.size(); i++) {
                            player.sendMessage(getMessage().get("commands.bank.top.listed", String.valueOf(i + 1), list.get(i).getKey().getName(), getEconomy().currency() + getEconomy().format(list.get(i).getValue())));
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    if (player.hasPermission("essentials.command.bank.withdraw")) {
                        getEconomy().openBankWithdraw(player);
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (player.hasPermission("essentials.command.bank.deposit")) {
                        getEconomy().openBankDeposit(player);
                        return true;
                    }
                }
            } else if (args.length == 2) {
                var amount = Double.parseDouble(args[1]);
                if (args[0].equalsIgnoreCase("withdraw")) {
                    if (player.hasPermission("essentials.command.bank.withdraw")) {
                        if (amount >= getEconomy().getMinimumBankWithdraw()) {
                            if (getEconomy().hasBank(player, amount)) {
                                getEconomy().removeBank(player, amount);
                                getEconomy().add(player, amount);
                                player.sendMessage(getMessage().get("commands.bank.withdraw.success", getEconomy().currency() + getEconomy().format(amount)));
                                player.sendMessage(getMessage().get("commands.bank.withdraw.left", getEconomy().currency() + getEconomy().format(getUserdata().getBankAccount(player))));
                            } else player.sendMessage(getMessage().get("commands.bank.withdraw.insufficient-funds", getEconomy().currency() + getEconomy().format(amount)));
                        } else player.sendMessage(getMessage().get("commands.bank.withdraw.minimum", getEconomy().currency() + getEconomy().format(getEconomy().getMinimumBankWithdraw())));
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    if (player.hasPermission("essentials.command.bank.deposit")) {
                        if (amount >= getEconomy().getMinimumBankDeposit()) {
                            if (getEconomy().has(player, amount)) {
                                getEconomy().remove(player, amount);
                                getEconomy().addBank(player, amount);
                                player.sendMessage(getMessage().get("commands.bank.deposit.success", getEconomy().currency() + getEconomy().format(amount)));
                                player.sendMessage(getMessage().get("commands.bank.deposit.left", getEconomy().currency() + getEconomy().format(getUserdata().getBankAccount(player))));
                            } else player.sendMessage(getMessage().get("commands.bank.deposit.insufficient-funds", getEconomy().currency() + getEconomy().format(amount)));
                        } else player.sendMessage(getMessage().get("commands.bank.deposit.minimum", getEconomy().currency() + getEconomy().format(getEconomy().getMinimumBankDeposit())));
                    }
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("top")) {
                    consoleCommandSender.sendMessage(getMessage().get("commands.bank.top.title"));
                    var list = new ArrayList<>(getEconomy().getTopAccounts(getEconomy().getBankAccounts(), 10));
                    for (int i = 0; i < list.size(); i++) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.bank.top.listed", String.valueOf(i + 1), list.get(i).getKey().getName(), getEconomy().currency() + getEconomy().format(list.get(i).getValue())));
                    }
                    return true;
                }
            } else if (args.length == 2) {
                var target = getInstance().getPlayer(args[1]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("withdraw")) {
                        getEconomy().openBankWithdraw(target);
                    } else if (args[0].equalsIgnoreCase("deposit")) {
                        getEconomy().openBankDeposit(target);
                    }
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[1]));
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
                if (args[0].equalsIgnoreCase("withdraw") || args[0].equalsIgnoreCase("deposit")) {
                    if (player.hasPermission("essentials.command.bank.withdraw") || player.hasPermission("essentials.command.bank.deposit")) {
                        commands.add("8");
                        commands.add("16");
                        commands.add("32");
                        commands.add("64");
                    }
                }
            }
        }
        return commands;
    }
}