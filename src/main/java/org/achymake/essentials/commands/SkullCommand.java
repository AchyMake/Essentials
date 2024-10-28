package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Skulls;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkullCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private Skulls getSkulls() {
        return getInstance().getSkulls();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SkullCommand() {
        getInstance().getCommand("skull").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (getUserdata(player).isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("player")) {
                    getMaterialHandler().giveItem(player, getMaterialHandler().getPlayerHead(sender.getServer().getOfflinePlayer(args[1]), 1));
                    return true;
                } else if (args[0].equalsIgnoreCase("custom")) {
                    var skullName = args[1];
                    if (getSkulls().isListed(skullName)) {
                        getMaterialHandler().giveItem(player, getSkulls().getCustomHead(skullName, 1));
                        getMessage().send(player, "&6You received&f " + skullName + "&6 skull");
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 3) {
                var target = getInstance().getServer().getPlayerExact(args[2]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("player")) {
                        var offlinePlayer = sender.getServer().getOfflinePlayer(args[1]);
                        getMaterialHandler().giveItem(target, getMaterialHandler().getPlayerHead(offlinePlayer, 1));
                        getMessage().send(target, "&6You received&f " + offlinePlayer.getName() + "&6's skull");
                        return true;
                    } else if (args[0].equalsIgnoreCase("custom")) {
                        var skullName = args[1];
                        if (getSkulls().isListed(skullName)) {
                            getMaterialHandler().giveItem(target, getSkulls().getCustomHead(skullName, 1));
                            getMessage().send(target, "&6You received&f " + skullName + "&6's custom skull");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                commands.add("player");
                if (!getInstance().isSpigot()) {
                    commands.add("custom");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("player")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata(target).isVanished()) {
                            if (target.getName().startsWith(args[1])) {
                                commands.add(target.getName());
                            }
                        }
                    });
                } else if (args[0].equalsIgnoreCase("custom")) {
                    if (!getInstance().isSpigot()) {
                        commands.addAll(getSkulls().getListed());
                    }
                }
            }
        }
        return commands;
    }
}