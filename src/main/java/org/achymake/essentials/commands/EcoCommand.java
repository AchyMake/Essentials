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
            if (args.length == 2) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setDouble("account", getInstance().getConfig().getDouble("economy.starting-balance"));
                        player.sendMessage(getMessage().get("commands.eco.reset", offlinePlayer.getName(), getEconomy().currency() + getEconomy().format(getInstance().getConfig().getDouble("economy.starting-balance"))));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                }
            } else if (args.length == 3) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                var value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        getEconomy().add(offlinePlayer, value);
                        player.sendMessage(getMessage().get("commands.eco.add", getEconomy().currency() + getEconomy().format(value), offlinePlayer.getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        if (getEconomy().has(offlinePlayer, value)) {
                            getEconomy().remove(offlinePlayer, value);
                            player.sendMessage(getMessage().get("commands.eco.remove.success", getEconomy().currency() + getEconomy().format(value), offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("commands.eco.remove.insufficient-funds", offlinePlayer.getName(), getEconomy().currency() + getEconomy().format(value)));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("set")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setDouble("account", value);
                        player.sendMessage(getMessage().get("commands.eco.set", getEconomy().currency() + getEconomy().format(value), offlinePlayer.getName()));
                    } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                if (args[0].equalsIgnoreCase("reset")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        userdataOffline.setDouble("account", getInstance().getConfig().getDouble("economy.starting-balance"));
                        consoleCommandSender.sendMessage(getMessage().get("commands.eco.reset", offlinePlayer.getName(), getEconomy().currency() + getEconomy().format(getInstance().getConfig().getDouble("economy.starting-balance"))));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                }
            } else if (args.length == 3) {
                var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                var value = Double.parseDouble(args[2]);
                if (args[0].equalsIgnoreCase("add")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        getEconomy().add(offlinePlayer, value);
                        consoleCommandSender.sendMessage(getMessage().get("commands.eco.add", getEconomy().currency() + getEconomy().format(value), offlinePlayer.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        if (getEconomy().has(offlinePlayer, value)) {
                            getEconomy().remove(offlinePlayer, value);
                            consoleCommandSender.sendMessage(getMessage().get("commands.eco.remove.success", getEconomy().currency() + getEconomy().format(value), offlinePlayer.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.eco.remove.non-sufficient-funds", offlinePlayer.getName(), getEconomy().currency() + getEconomy().format(value)));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("set")) {
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        getEconomy().set(offlinePlayer, value);
                        consoleCommandSender.sendMessage(getMessage().get("commands.eco.set", getEconomy().currency() + getEconomy().format(value), offlinePlayer.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
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