package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Skulls;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.Bukkit;
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
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("player")) {
                    var offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    getMaterialHandler().giveItemStack(player, getMaterialHandler().getPlayerHead(offlinePlayer, 1));
                    player.sendMessage(getMessage().get("commands.skull.received", offlinePlayer.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("custom")) {
                    var skullName = args[1];
                    if (getSkulls().isListed(skullName)) {
                        getMaterialHandler().giveItemStack(player, getSkulls().getCustomHead(skullName, 1));
                        player.sendMessage(getMessage().get("commands.skull.received", skullName));
                    } else player.sendMessage(getMessage().get("commands.skull.invalid", skullName));
                    return true;
                }
            } else if (args.length == 3) {
                var target = getInstance().getServer().getPlayerExact(args[2]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("player")) {
                        var offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        getMaterialHandler().giveItemStack(target, getMaterialHandler().getPlayerHead(offlinePlayer, 1));
                        target.sendMessage(getMessage().get("commands.skull.received", offlinePlayer.getName()));
                        player.sendMessage(getMessage().get("commands.skull.sender", offlinePlayer.getName()));
                        return true;
                    } else if (args[0].equalsIgnoreCase("custom")) {
                        var skullName = args[1];
                        if (getSkulls().isListed(skullName)) {
                            getMaterialHandler().giveItemStack(target, getSkulls().getCustomHead(skullName, 1));
                            target.sendMessage(getMessage().get("commands.skull.received", skullName));
                            player.sendMessage(getMessage().get("commands.skull.sender", skullName));
                        } else player.sendMessage(getMessage().get("commands.skull.invalid", skullName));
                    }
                } else player.sendMessage(getMessage().get("error.target.invalid", args[2]));
                return true;
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 3) {
                var target = getInstance().getServer().getPlayerExact(args[2]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("player")) {
                        var offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        getMaterialHandler().giveItemStack(target, getMaterialHandler().getPlayerHead(offlinePlayer, 1));
                        target.sendMessage(getMessage().get("commands.skull.received", offlinePlayer.getName()));
                        consoleCommandSender.sendMessage(getMessage().get("commands.skull.sender", offlinePlayer.getName()));
                        return true;
                    } else if (args[0].equalsIgnoreCase("custom")) {
                        var skullName = args[1];
                        if (getSkulls().isListed(skullName)) {
                            getMaterialHandler().giveItemStack(target, getSkulls().getCustomHead(skullName, 1));
                            target.sendMessage(getMessage().get("commands.skull.received", skullName));
                            consoleCommandSender.sendMessage(getMessage().get("commands.skull.sender", skullName));
                        } else consoleCommandSender.sendMessage(getMessage().get("commands.skull.invalid", skullName));
                    }
                } else consoleCommandSender.sendMessage(getMessage().get("error.target.invalid", args[2]));
                return true;
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
                if (!getInstance().isBukkit()) {
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
                    if (!getInstance().isBukkit()) {
                        commands.addAll(getSkulls().getListed());
                    }
                }
            }
        }
        return commands;
    }
}