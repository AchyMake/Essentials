package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.InventoryHandler;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchCommand implements CommandExecutor, TabCompleter {
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
    public WorkbenchCommand() {
        getInstance().getCommand("workbench").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getInventoryHandler().openWorkbench(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.workbench.other")) {
                    var username = args[0];
                    var target = getInstance().getPlayer(username);
                    if (target != null) {
                        if (target == player) {
                            getInventoryHandler().openWorkbench(target);
                            player.sendMessage(getMessage().get("commands.workbench.sender", target.getName()));
                        } else if (!target.hasPermission("essentials.command.workbench.exempt")) {
                            getInventoryHandler().openWorkbench(target);
                            player.sendMessage(getMessage().get("commands.workbench.sender", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.workbench.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", username));
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var username = args[0];
                var target = getInstance().getPlayer(username);
                if (target != null) {
                    getInventoryHandler().openWorkbench(target);
                    consoleCommandSender.sendMessage(getMessage().get("commands.workbench.sender", target.getName()));
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.offline", username));
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
                if (player.hasPermission("essentials.command.workbench.other")) {
                    var username = args[0];
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata().isVanished(target)) {
                            if (target.getName().startsWith(username)) {
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