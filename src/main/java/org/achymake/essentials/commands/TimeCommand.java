package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TimeCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public TimeCommand() {
        getInstance().getCommand("time").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 2) {
                var options = args[0];
                var value = args[1];
                if (options.equalsIgnoreCase("set")) {
                    if (value.equalsIgnoreCase("morning")) {
                        getWorldHandler().setMorning(player.getWorld());
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Morning"));
                    } else if (value.equalsIgnoreCase("day")) {
                        getWorldHandler().setDay(player.getWorld());
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Day"));
                    } else if (value.equalsIgnoreCase("noon")) {
                        getWorldHandler().setNoon(player.getWorld());
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Noon"));
                    } else if (value.equalsIgnoreCase("night")) {
                        getWorldHandler().setNight(player.getWorld());
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Night"));
                    } else if (value.equalsIgnoreCase("midnight")) {
                        getWorldHandler().setMidnight(player.getWorld());
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Midnight"));
                    } else getWorldHandler().setTime(player.getWorld(), Long.parseLong(value));
                    return true;
                } else if (options.equalsIgnoreCase("add")) {
                    getWorldHandler().addTime(player.getWorld(), Long.parseLong(value));
                    player.sendMessage(getMessage().get("commands.time.add", player.getWorld().getName(), value));
                    return true;
                } else if (options.equalsIgnoreCase("remove")) {
                    getWorldHandler().removeTime(player.getWorld(), Long.parseLong(value));
                    player.sendMessage(getMessage().get("commands.time.remove", player.getWorld().getName(), value));
                    return true;
                }
            } else if (args.length == 3) {
                var world = getWorldHandler().get(args[2]);
                if (world != null) {
                    if (args[0].equalsIgnoreCase("set")) {
                        if (args[1].equalsIgnoreCase("morning")) {
                            getWorldHandler().setMorning(world);
                            player.sendMessage(getMessage().get("commands.time.set", world.getName(), getMessage().toTitleCase(args[1])));
                        } else if (args[1].equalsIgnoreCase("day")) {
                            getWorldHandler().setDay(world);
                            player.sendMessage(getMessage().get("commands.time.set", world.getName(), getMessage().toTitleCase(args[1])));
                        } else if (args[1].equalsIgnoreCase("noon")) {
                            getWorldHandler().setNoon(world);
                            player.sendMessage(getMessage().get("commands.time.set", world.getName(), getMessage().toTitleCase(args[1])));
                        } else if (args[1].equalsIgnoreCase("night")) {
                            getWorldHandler().setNight(world);
                            player.sendMessage(getMessage().get("commands.time.set", world.getName(), getMessage().toTitleCase(args[1])));
                        } else if (args[1].equalsIgnoreCase("midnight")) {
                            getWorldHandler().setMidnight(world);
                            player.sendMessage(getMessage().get("commands.time.set", world.getName(), getMessage().toTitleCase(args[1])));
                        } else getWorldHandler().setTime(world, Long.parseLong(args[1]));
                        return true;
                    } else if (args[0].equalsIgnoreCase("add")) {
                        getWorldHandler().addTime(world, Long.parseLong(args[1]));
                        player.sendMessage(getMessage().get("commands.time.add", world.getName(), args[1]));
                        return true;
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        getWorldHandler().removeTime(world, Long.parseLong(args[1]));
                        player.sendMessage(getMessage().get("commands.time.remove", world.getName(), args[1]));
                        return true;
                    }
                } else player.sendMessage(getMessage().get("error.world.invalid", args[2]));
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                commands.add("add");
                commands.add("set");
                commands.add("remove");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    commands.add("day");
                    commands.add("night");
                    commands.add("noon");
                    commands.add("midnight");
                    commands.add("morning");
                } else if (args[0].equalsIgnoreCase("add") | args[0].equalsIgnoreCase("remove")) {
                    commands.add("10");
                    commands.add("100");
                    commands.add("1000");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set") | args[0].equalsIgnoreCase("add")) {
                    commands.addAll(getWorldHandler().getListed());
                }
            }
        }
        return commands;
    }
}