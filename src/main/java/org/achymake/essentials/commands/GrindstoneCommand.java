package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.InventoryHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GrindstoneCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private InventoryHandler getInventoryHandler() {
        return getInstance().getInventoryHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public GrindstoneCommand() {
        getInstance().getCommand("grindstone").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getInventoryHandler().openGrindstone(player) == null) {
                    player.sendMessage(getMessage().get("error.not-provided"));
                }
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.grindstone.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            if (getInventoryHandler().openGrindstone(target) == null) {
                                player.sendMessage(getMessage().get("error.not-provided"));
                            } else player.sendMessage(getMessage().get("commands.grindstone.sender", target.getName()));
                        } else if (!target.hasPermission("essentials.command.grindstone.exempt")) {
                            if (getInventoryHandler().openGrindstone(target) == null) {
                                player.sendMessage(getMessage().get("error.not-provided"));
                            } else player.sendMessage(getMessage().get("commands.grindstone.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.grindstone.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (getInventoryHandler().openGrindstone(target) == null) {
                        consoleCommandSender.sendMessage(getMessage().get("error.not-provided"));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.grindstone.sender", target.getName()));
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
                if (player.hasPermission("essentials.command.grindstone.other")) {
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
}