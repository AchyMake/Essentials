package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CartographyCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public CartographyCommand() {
        getInstance().getCommand("cartography").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                openCartographyTable(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.cartography.other")) {
                    var target = player.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            openCartographyTable(player);
                            target.sendMessage(getMessage().get("commands.cartography.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.cartography.sender", target.getName()));
                            return true;
                        } else if (!target.hasPermission("essentials.command.cartography.exempt")) {
                            openCartographyTable(target);
                            target.sendMessage(getMessage().get("commands.cartography.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.cartography.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.cartography.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = consoleCommandSender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    openCartographyTable(target);
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", args[0]));
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
                if (player.hasPermission("essentials.command.cartography.other")) {
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
    private void openCartographyTable(Player player) {
        var inventory = getInstance().getInventoryHandler().openCartographyTable(player);
        if (inventory == null) {
            player.sendMessage(getMessage().get("error.not-provided"));
        }
    }
}