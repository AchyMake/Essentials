package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public FlyCommand() {
        getInstance().getCommand("fly").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else {
                if (args.length == 0) {
                    player.setAllowFlight(!player.getAllowFlight());
                    if (player.getAllowFlight()) {
                        getMessage().sendActionBar(player, "&6&lFly:&a Enabled");
                    } else getMessage().sendActionBar(player, "&6&lFly:&c Disabled");
                    return true;
                } else if (args.length == 1) {
                    if (player.hasPermission("players.command.fly.other")) {
                        var target = sender.getServer().getPlayerExact(args[0]);
                        if (target != null) {
                            if (target == player) {
                                target.setAllowFlight(!target.getAllowFlight());
                                if (target.getAllowFlight()) {
                                    getMessage().sendActionBar(target, "&6&lFly:&a Enabled");
                                } else getMessage().sendActionBar(target, "&6&lFly:&c Disabled");
                            } else if (!target.hasPermission("players.command.fly.exempt")) {
                                target.setAllowFlight(!target.getAllowFlight());
                                if (target.getAllowFlight()) {
                                    getMessage().sendActionBar(target, "&6&lFly:&a Enabled");
                                    getMessage().send(player, "&6You enabled fly for&f " + target.getName());
                                } else {
                                    getMessage().sendActionBar(target, "&6&lFly:&c Disabled");
                                    getMessage().send(player, "&6You disabled fly for&f " + target.getName());
                                }
                            } else getMessage().send(player, command.getPermissionMessage());
                            return true;
                        }
                    }
                }
            }
        }
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setAllowFlight(!target.getAllowFlight());
                    if (target.getAllowFlight()) {
                        getMessage().sendActionBar(target, "&6&lFly:&a Enabled");
                    } else getMessage().sendActionBar(target, "&6&lFly:&c Disabled");
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
                if (player.hasPermission("players.command.fly.other")) {
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