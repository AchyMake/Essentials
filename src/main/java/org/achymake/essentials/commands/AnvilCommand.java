package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class AnvilCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public AnvilCommand() {
        getInstance().getCommand("anvil").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                openAnvil(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.anvil.other")) {
                    var target = player.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            openAnvil(target);
                            target.sendMessage(getMessage().get("commands.anvil.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.anvil.sender", target.getName()));
                            return true;
                        } else if (!target.hasPermission("essentials.command.anvil.exempt")) {
                            openAnvil(target);
                            target.sendMessage(getMessage().get("commands.anvil.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.anvil.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.anvil.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    openAnvil(target);
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
                if (player.hasPermission("essentials.command.anvil.other")) {
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
    private void openAnvil(Player player) {
        var inventory = getInstance().getInventoryHandler().openAnvil(player);
        if (inventory == null) {
            player.sendMessage(getMessage().get("error.not-provided"));
        }
    }
}