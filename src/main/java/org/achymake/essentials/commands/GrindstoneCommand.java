package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
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
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public GrindstoneCommand() {
        getInstance().getCommand("grindstone").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                openGrindstone(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.grindstone.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            openGrindstone(target);
                            getMessage().send(target, player.getName() + "&6 opened grindstone for you");
                            getMessage().send(player, "&6You opened grindstone for&f " + target.getName());
                        } else if (target.hasPermission("essentials.command.grindstone.exempt")) {
                            getMessage().send(player, command.getPermissionMessage());
                        } else {
                            openGrindstone(target);
                            getMessage().send(target, player.getName() + "&6 opened grindstone for you");
                            getMessage().send(player, "&6You opened grindstone for&f " + target.getName());
                        }
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    openGrindstone(target);
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
    private void openGrindstone(Player player) {
        var inventory = getInstance().getInventoryHandler().openGrindstone(player);
        if (inventory == null) {
            getMessage().send(player, "&cServer does not provide this function");
        }
    }
}