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
            if (args.length == 0) {
                player.setAllowFlight(!player.getAllowFlight());
                if (player.getAllowFlight()) {
                    getMessage().sendActionBar(player, getMessage().get("commands.fly.enable"));
                } else getMessage().sendActionBar(player, getMessage().get("commands.fly.disable"));
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("players.command.fly.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            target.setAllowFlight(!target.getAllowFlight());
                            if (target.getAllowFlight()) {
                                getMessage().sendActionBar(target, getMessage().get("commands.fly.enable"));
                            } else getMessage().sendActionBar(target, getMessage().get("commands.fly.disable"));
                        } else if (!target.hasPermission("players.command.fly.exempt")) {
                            target.setAllowFlight(!target.getAllowFlight());
                            if (target.getAllowFlight()) {
                                getMessage().sendActionBar(target, getMessage().get("commands.fly.enable"));
                                target.sendMessage(getMessage().get("commands.fly.target", player.getName(), "enabled"));
                                player.sendMessage(getMessage().get("commands.fly.sender", "enabled", target.getName()));
                            } else {
                                getMessage().sendActionBar(target, getMessage().get("commands.fly.disable"));
                                target.sendMessage(getMessage().get("commands.fly.target", player.getName(), "disabled"));
                                player.sendMessage(getMessage().get("commands.fly.sender", "disabled", target.getName()));
                            }
                        } else player.sendMessage(getMessage().get("commands.fly.exempt", target.getName()));
                    } else player.sendMessage(getMessage().get("error.target.offline", args[0]));
                    return true;
                }
            }
        }
        if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    target.setAllowFlight(!target.getAllowFlight());
                    if (target.getAllowFlight()) {
                        getMessage().sendActionBar(target, getMessage().get("commands.fly.enable"));
                    } else getMessage().sendActionBar(target, getMessage().get("commands.fly.disable"));
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