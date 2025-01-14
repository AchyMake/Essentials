package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.InventoryHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AnvilCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private InventoryHandler getInventoryHandler() {
        return getInstance().getInventoryHandler();
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
                if (getInventoryHandler().openAnvil(player) == null) {
                    player.sendMessage(getMessage().get("error.invalid"));
                }
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.anvil.other")) {
                    var target = getInstance().getPlayer(args[0]);
                    if (target != null) {
                        if (target == player) {
                            if (getInventoryHandler().openAnvil(target) == null) {
                                player.sendMessage(getMessage().get("error.invalid"));
                            } else player.sendMessage(getMessage().get("commands.anvil.sender", target.getName()));
                            return true;
                        } else if (!target.hasPermission("essentials.command.anvil.exempt")) {
                            if (getInventoryHandler().openAnvil(target) == null) {
                                player.sendMessage(getMessage().get("error.invalid"));
                            } else player.sendMessage(getMessage().get("commands.anvil.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.anvil.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    if (getInventoryHandler().openAnvil(target) == null) {
                        consoleCommandSender.sendMessage(getMessage().get("error.invalid"));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.anvil.sender", target.getName()));
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
                        if (!getUserdata().isVanished(target)) {
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
}