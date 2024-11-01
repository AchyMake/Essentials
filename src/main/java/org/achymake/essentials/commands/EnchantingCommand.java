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
            if (args.length == 0) {
                openEnchantingTable(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.enchanting.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            openEnchantingTable(player);
                            target.sendMessage(getMessage().get("commands.enchanting.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.enchanting.sender", target.getName()));
                            return true;
                        } else if (!target.hasPermission("essentials.command.enchanting.exempt")) {
                            openEnchantingTable(target);
                            target.sendMessage(getMessage().get("commands.enchanting.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.enchanting.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.enchanting.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    openEnchantingTable(target);
                    return true;
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
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