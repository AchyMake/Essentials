package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlySpeedCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public FlySpeedCommand() {
        getInstance().getCommand("flyspeed").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 1) {
                var value = Float.parseFloat(args[0]);
                userdata.setFlySpeed(value);
                getMessage().send(player, "&6You're fly speed has changed to&f " + value);
                return true;
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.flyspeed.other")) {
                    var value = Float.parseFloat(args[0]);
                    var target = sender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        if (target == player) {
                            userdataTarget.setFlySpeed(value);
                            getMessage().send(player, "&6You changed&f " + target.getName() + " &6fly speed to&f " + value);
                        } else if (!target.hasPermission("essentials.command.flyspeed.exempt")) {
                            userdataTarget.setFlySpeed(value);
                            getMessage().send(player, "&6You changed&f " + target.getName() + " &6fly speed to&f " + value);
                        } else getMessage().send(player, command.getPermissionMessage());
                        return true;
                    }
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
                commands.add("1");
                commands.add("2");
                commands.add("4");
            } else if (args.length == 2) {
                if (player.hasPermission("essentials.command.flyspeed.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata(target).isVanished()) {
                            if (target.getName().startsWith(args[1])) {
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