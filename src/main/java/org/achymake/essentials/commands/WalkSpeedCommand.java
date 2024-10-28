package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WalkSpeedCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public WalkSpeedCommand() {
        getInstance().getCommand("walkspeed").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 1) {
                var value = Double.parseDouble(args[0]);
                userdata.setMovementSpeed(value);
                getMessage().send(player, "&6You changed walk speed to&f " + value);
                return true;
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.walkspeed.other")) {
                    var target = sender.getServer().getPlayerExact(args[1]);
                    var userdataTarget = getUserdata(target);
                    if (target != null) {
                        var value = Double.parseDouble(args[0]);
                        if (target == player) {
                            userdataTarget.setMovementSpeed(value);
                            getMessage().send(player, "&6You changed&f " + target.getName() + " &6walk speed to&f " + value);
                        } else if (!target.hasPermission("essentials.command.walkspeed.exempt")) {
                            userdataTarget.setMovementSpeed(value);
                            getMessage().send(player, "&6You changed&f " + target.getName() + " &6walk speed to&f " + value);
                        } else getMessage().send(player, command.getPermissionMessage());
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 2) {
                var target = sender.getServer().getPlayerExact(args[1]);
                var userdataTarget = getUserdata(target);
                if (target != null) {
                    var value = Double.parseDouble(args[0]);
                    userdataTarget.setMovementSpeed(value);
                    consoleCommandSender.sendMessage("You changed " + target.getName() + " walk speed to " + value);
                } else consoleCommandSender.sendMessage(args[1] + " is currently offline");
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
                commands.add("0.2");
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.walkspeed.other")) {
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