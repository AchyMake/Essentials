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

public class ExpCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public ExpCommand() {
        getInstance().getCommand("exp").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                player.sendMessage(getMessage().addColor(player.getLevel() + ": " + player.getTotalExperience()));
                return true;
            } else if (args.length == 1) {
                var target = getInstance().getPlayer(args[0]);
                if (target != null) {
                    player.sendMessage(getMessage().addColor(target.getLevel() + ": " + target.getTotalExperience()));
                    return true;
                }
            } else if (args.length == 4) {
                var target = getInstance().getPlayer(args[0]);
                if (args[3].equalsIgnoreCase("exp")) {
                    var value = Integer.parseInt(args[2]);
                    if (args[1].equalsIgnoreCase("add")) {
                        target.giveExp(value);
                        player.sendMessage(getMessage().addColor(target.getName() + " exp is now " + target.getTotalExperience()));
                        return true;
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        target.giveExp(-value);
                        player.sendMessage(getMessage().addColor(target.getName() + " exp is now " + target.getTotalExperience()));
                        return true;
                    } else if (args[1].equalsIgnoreCase("set")) {
                        if (value >= 0) {
                            target.setExperienceLevelAndProgress(value);
                            player.sendMessage(getMessage().addColor(target.getName() + " exp is now " + target.getTotalExperience()));
                            return true;
                        }
                    }
                } else if (args[3].equalsIgnoreCase("lvl")) {
                    var value = Integer.parseInt(args[2]);
                    if (args[1].equalsIgnoreCase("add")) {
                        target.giveExpLevels(value);
                        player.sendMessage(getMessage().addColor(target.getName() + " lvl is now " + target.getLevel()));
                        return true;
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        target.giveExpLevels(-value);
                        player.sendMessage(getMessage().addColor(target.getName() + " lvl is now " + target.getLevel()));
                        return true;
                    } else if (args[1].equalsIgnoreCase("set")) {
                        target.setLevel(value);
                        player.sendMessage(getMessage().addColor(target.getName() + " lvl is now " + target.getLevel()));
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
            }
            if (args.length == 2) {
                commands.add("add");
                commands.add("remove");
                commands.add("set");
            }
            if (args.length == 3) {
                commands.add("1");
                commands.add("5");
                commands.add("10");
            }
            if (args.length == 4) {
                commands.add("exp");
                commands.add("lvl");
            }
        }
        return commands;
    }
}