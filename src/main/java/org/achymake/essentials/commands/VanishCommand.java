package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private VanishHandler getVanish() {
        return getInstance().getVanishHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public VanishCommand() {
        getInstance().getCommand("vanish").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getVanish().toggleVanish(player);
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.vanish.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            getVanish().toggleVanish(target);
                            if (getVanish().isVanish(target)) {
                                player.sendMessage(getMessage().get("commands.vanish.enable", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.vanish.disable", target.getName()));
                        } else if (!target.hasPermission("essentials.command.vanish.exempt")) {
                            getVanish().toggleVanish(target);
                            if (getVanish().isVanish(target)) {
                                player.sendMessage(getMessage().get("commands.vanish.enable", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.vanish.disable", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.vanish.exempt", target.getName()));
                    } else {
                        var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                        var userdataOffline = getUserdata(offlinePlayer);
                        if (userdataOffline.exists()) {
                            getVanish().toggleVanish(offlinePlayer);
                            if (getVanish().isVanish(offlinePlayer)) {
                                player.sendMessage(getMessage().get("commands.vanish.enable", offlinePlayer.getName()));
                            } else player.sendMessage(getMessage().get("commands.vanish.disable", offlinePlayer.getName()));
                        } else player.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                    }
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getVanish().toggleVanish(target);
                    if (getVanish().isVanish(target)) {
                        consoleCommandSender.sendMessage(getMessage().get("commands.vanish.enable", target.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("commands.vanish.disable", target.getName()));
                } else {
                    var offlinePlayer = sender.getServer().getOfflinePlayer(args[0]);
                    var userdataOffline = getUserdata(offlinePlayer);
                    if (userdataOffline.exists()) {
                        getVanish().toggleVanish(offlinePlayer);
                        if (getVanish().isVanish(offlinePlayer)) {
                            consoleCommandSender.sendMessage(getMessage().get("commands.vanish.enable", offlinePlayer.getName()));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.vanish.disable", offlinePlayer.getName()));
                    } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", offlinePlayer.getName()));
                }
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
                if (player.hasPermission("essentials.command.vanish.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    });
                }
            }
        }
        return commands;
    }
}