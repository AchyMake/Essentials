package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LvlCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public LvlCommand() {
        getInstance().getCommand("lvl").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.sendMessage(getMessage().get("commands.lvl.self", String.valueOf(player.getLevel())));
                return true;
            } else if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    player.sendMessage(getMessage().get("commands.lvl.other", target.getName(), String.valueOf(target.getLevel())));
                    return true;
                }
            } else if (args.length == 3) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var value = Integer.parseInt(args[2]);
                    if (args[1].equalsIgnoreCase("add")) {
                        target.giveExpLevels(value);
                        player.sendMessage(getMessage().get("commands.lvl.add", String.valueOf(value), target.getName()));
                        return true;
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        target.giveExpLevels(-value);
                        player.sendMessage(getMessage().get("commands.lvl.remove", String.valueOf(value), target.getName()));
                        return true;
                    } else if (args[1].equalsIgnoreCase("set")) {
                        target.setLevel(value);
                        player.sendMessage(getMessage().get("commands.lvl.set", String.valueOf(value), target.getName()));
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    consoleCommandSender.sendMessage(getMessage().get("commands.lvl.other", target.getName(), String.valueOf(target.getLevel())));
                    return true;
                }
            } else if (args.length == 3) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    var value = Integer.parseInt(args[2]);
                    if (args[1].equalsIgnoreCase("add")) {
                        target.giveExpLevels(value);
                        consoleCommandSender.sendMessage(getMessage().get("commands.lvl.add", String.valueOf(value), target.getName()));
                        return true;
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        target.giveExpLevels(-value);
                        consoleCommandSender.sendMessage(getMessage().get("commands.lvl.remove", String.valueOf(value), target.getName()));
                        return true;
                    } else if (args[1].equalsIgnoreCase("set")) {
                        target.setLevel(value);
                        consoleCommandSender.sendMessage(getMessage().get("commands.lvl.set", String.valueOf(value), target.getName()));
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
        if (sender instanceof Player) {
            if (args.length == 1) {
                getInstance().getOnlinePlayers().forEach(target -> {
                    if (!getUserdata(target).isVanished()) {
                        if (target.getName().startsWith(args[0])) {
                            commands.add(target.getName());
                        }
                    }
                });
            } else if (args.length == 2) {
                commands.add("add");
                commands.add("remove");
                commands.add("set");
            } else if (args.length == 3) {
                commands.add("1");
                commands.add("2");
                commands.add("3");
            }
        }
        return commands;
    }
}