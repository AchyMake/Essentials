package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EnchantingCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public EnchantingCommand() {
        getInstance().getCommand("enchanting").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                openEnchantingTable(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.enchanting.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            openEnchantingTable(player);
                            getMessage().send(target, player.getName() + "&6 opened enchanting table for you");
                            getMessage().send(player, "&6You opened enchanting table for&f " + target.getName());
                            return true;
                        } else if (target.hasPermission("essentials.command.enchanting.exempt")) {
                            getMessage().send(player, command.getPermissionMessage());
                        } else {
                            openEnchantingTable(target);
                            getMessage().send(target, player.getName() + "&6 opened enchanting table for you");
                            getMessage().send(player, "&6You opened enchanting table for&f " + target.getName());
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    openEnchantingTable(target);
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
                if (player.hasPermission("essentials.command.enchanting.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata(target).isVanished()) {
                            if (target.getName().startsWith(args[0])) {
                                commands.add(target.getName());
                            }
                        }
                    });
                }
            }
        }
        return commands;
    }
    private void openEnchantingTable(Player player) {
        getInstance().getInventoryHandler().openEnchanting(player);
    }
}