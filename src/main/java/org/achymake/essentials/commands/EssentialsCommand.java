package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EssentialsCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public EssentialsCommand() {
        getInstance().getCommand("essentials").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                getMessage().send(player, "&6" + getInstance().name() + ":&f " + getInstance().version());
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    getInstance().reload();
                    getMessage().send(player, "&6Essentials:&f reloaded");
                    return true;
                } else if (args[0].equalsIgnoreCase("discord")) {
                    getMessage().send(player, "&6Developers Discord:");
                    getMessage().send(player, "&f-&a https://discord.gg/BMKaW4yTvy");
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (args[1].equalsIgnoreCase("userdata")) {
                        getInstance().reloadUserdata();
                        getMessage().send(player, "&6Essentials:&f reloaded > userdata");
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 0) {
                consoleCommandSender.sendMessage(getInstance().name() + " " + getInstance().version());
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    getInstance().reload();
                    consoleCommandSender.sendMessage("Essentials: reloaded");
                    return true;
                } else if (args[0].equalsIgnoreCase("discord")) {
                    consoleCommandSender.sendMessage("Developers Discord:");
                    consoleCommandSender.sendMessage("- https://discord.gg/BMKaW4yTvy");
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (args[1].equalsIgnoreCase("userdata")) {
                        getInstance().reloadUserdata();
                        consoleCommandSender.sendMessage("Essentials: reloaded > userdata");
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
                commands.add("reload");
                commands.add("discord");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    commands.add("userdata");
                }
            }
        }
        return commands;
    }
}