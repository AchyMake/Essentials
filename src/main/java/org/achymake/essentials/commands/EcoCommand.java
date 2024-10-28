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

public class EcoCommand implements CommandExecutor, TabCompleter {
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
    public EcoCommand() {
        getInstance().getCommand("eco").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 2) {
                var offlinePlayer = player.getServer().getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    Userdata userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setDouble("account", getInstance().getConfig().getDouble("economy.starting-balance"));
                        getMessage().send(player, "&6You reset&f " + offlinePlayer.getName() + "&6 account to&a " + getEconomy().currency() + getEconomy().format(getInstance().getConfig().getDouble("economy.starting-balance")));
                    } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    return true;
                }
            } else if (args.length == 3) {
                var offlinePlayer = player.getServer().getOfflinePlayer(args[1]);
                var value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        getEconomy().add(offlinePlayer, value);
                        getMessage().send(player, "&6You added&a " + getEconomy().currency() + getEconomy().format(value) + "&6 to&f " + offlinePlayer.getName());
                    } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        if (getEconomy().has(offlinePlayer, value)) {
                            getEconomy().remove(offlinePlayer, value);
                            getMessage().send(player, "&6You removed&a " + getEconomy().currency() + getEconomy().format(value) + "&6 from&f " + offlinePlayer.getName());
                        } else getMessage().send(player, offlinePlayer.getName() + "&c does not have&a " + getEconomy().currency() + getEconomy().format(value));
                    } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    return true;
                } else if (args[0].equalsIgnoreCase("set")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setDouble("account", value);
                        getMessage().send(player, "&6You set&a " + getEconomy().currency() + getEconomy().format(value) + "&6 to&f " + offlinePlayer.getName());
                    } else getMessage().send(player, offlinePlayer.getName() + "&c has never joined");
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var offlinePlayer = sender.getServer().getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setDouble("account", getInstance().getConfig().getDouble("economy.starting-balance"));
                        consoleCommandSender.sendMessage("You reset " + offlinePlayer.getName() + " account to " + getEconomy().currency() + getEconomy().format(getInstance().getConfig().getDouble("economy.starting-balance")));
                    } else consoleCommandSender.sendMessage(offlinePlayer.getName() + " has never joined");
                    return true;
                }
            } else if (args.length == 3) {
                var offlinePlayer = sender.getServer().getOfflinePlayer(args[1]);
                var value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        getEconomy().add(offlinePlayer, value);
                        consoleCommandSender.sendMessage("You added " + getEconomy().currency() + getEconomy().format(value) + " to " + offlinePlayer.getName());
                    } else consoleCommandSender.sendMessage(offlinePlayer.getName() + " has never joined");
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        if (getEconomy().has(offlinePlayer, value)) {
                            getEconomy().remove(offlinePlayer, value);
                            consoleCommandSender.sendMessage("You removed " + getEconomy().currency() + getEconomy().format(value) + " from " + offlinePlayer.getName());
                        } else consoleCommandSender.sendMessage(offlinePlayer.getName() + " does not have " + getEconomy().currency() + getEconomy().format(value));
                    } else consoleCommandSender.sendMessage(offlinePlayer.getName() + " has never joined");
                    return true;
                } else if (args[0].equalsIgnoreCase("set")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        getEconomy().set(offlinePlayer, value);
                        consoleCommandSender.sendMessage("You set " + getEconomy().currency() + getEconomy().format(value) + " to " + offlinePlayer.getName());
                    } else consoleCommandSender.sendMessage(offlinePlayer.getName() + " has never joined");
                    return true;
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
                commands.add("add");
                commands.add("remove");
                commands.add("reset");
                commands.add("set");
            } else if (args.length == 2) {
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata(target).isVanished()) {
                        if (target.getName().startsWith(args[1])) {
                            commands.add(target.getName());
                        }
                    }
                });
            } else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("set")) {
                    commands.add("100");
                    commands.add("500");
                    commands.add("1000");
                }
            }
        }
        return commands;
    }
}