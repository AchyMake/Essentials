package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SmithingCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SmithingCommand() {
        getInstance().getCommand("smithing").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                openSmithingTable(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.smithing.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            openSmithingTable(player);
                            player.sendMessage(getMessage().get("commands.smithing.sender", target.getName()));
                            return true;
                        } else if (!target.hasPermission("essentials.command.smithing.exempt")) {
                            openSmithingTable(target);
                            player.sendMessage(getMessage().get("commands.smithing.sender", target.getName()));
                        } else getMessage().send(player, command.getPermissionMessage());
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    openSmithingTable(target);
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
                if (player.hasPermission("essentials.command.smithing.other")) {
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
    private void openSmithingTable(Player player) {
        var inventory = getInstance().getInventoryHandler().openSmithingTable(player);
        if (inventory == null) {
            player.sendMessage(getMessage().get("error.not-provided"));
        }
    }
}