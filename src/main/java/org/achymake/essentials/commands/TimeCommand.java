package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Worlds;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.World;
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
    private Worlds getWorlds() {
        return getInstance().getWorlds();
    }
    private WorldHandler getWorldHandler(World world) {
        return getInstance().getWorldHandler(world);
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
                        getWorldHandler(player.getWorld()).setMorning();
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Morning"));
                    } else if (value.equalsIgnoreCase("day")) {
                        getWorldHandler(player.getWorld()).setDay();
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Day"));
                    } else if (value.equalsIgnoreCase("noon")) {
                        getWorldHandler(player.getWorld()).setNoon();
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Noon"));
                    } else if (value.equalsIgnoreCase("night")) {
                        getWorldHandler(player.getWorld()).setNight();
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Night"));
                    } else if (value.equalsIgnoreCase("midnight")) {
                        getWorldHandler(player.getWorld()).setMidnight();
                        player.sendMessage(getMessage().get("commands.time.set", player.getWorld().getName(), "Midnight"));
                    } else getWorldHandler(player.getWorld()).setTime(Long.parseLong(value));
                    return true;
                } else if (options.equalsIgnoreCase("add")) {
                    getWorldHandler(player.getWorld()).addTime(Long.parseLong(value));
                    player.sendMessage(getMessage().get("commands.time.add", player.getWorld().getName(), value));
                    return true;
                } else if (options.equalsIgnoreCase("remove")) {
                    getWorldHandler(player.getWorld()).removeTime(Long.parseLong(value));
                    player.sendMessage(getMessage().get("commands.time.remove", player.getWorld().getName(), value));
                    return true;
                }
            } else if (args.length == 3) {
                var world = getWorlds().get(args[2]);
                if (world != null) {
                    var worldHandler = getWorldHandler(world);
                    var options = args[0];
                    var value = args[1];
                    if (options.equalsIgnoreCase("set")) {
                        if (value.equalsIgnoreCase("morning")) {
                            worldHandler.setMorning();
                            player.sendMessage(getMessage().get("commands.time.set", worldHandler.getName(), "Morning"));
                        } else if (value.equalsIgnoreCase("day")) {
                            worldHandler.setDay();
                            player.sendMessage(getMessage().get("commands.time.set", worldHandler.getName(), "Day"));
                        } else if (value.equalsIgnoreCase("noon")) {
                            worldHandler.setNoon();
                            player.sendMessage(getMessage().get("commands.time.set", worldHandler.getName(), "Noon"));
                        } else if (value.equalsIgnoreCase("night")) {
                            worldHandler.setNight();
                            player.sendMessage(getMessage().get("commands.time.set", worldHandler.getName(), "Night"));
                        } else if (value.equalsIgnoreCase("midnight")) {
                            worldHandler.setMidnight();
                            player.sendMessage(getMessage().get("commands.time.set", worldHandler.getName(), "Midnight"));
                        } else worldHandler.setTime(Long.parseLong(value));
                        return true;
                    } else if (options.equalsIgnoreCase("add")) {
                        worldHandler.addTime(Long.parseLong(value));
                        player.sendMessage(getMessage().get("commands.time.add", worldHandler.getName(), value));
                        return true;
                    } else if (options.equalsIgnoreCase("remove")) {
                        worldHandler.removeTime(Long.parseLong(value));
                        player.sendMessage(getMessage().get("commands.time.remove", worldHandler.getName(), value));
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
                    commands.add("morning");
                    commands.add("day");
                    commands.add("noon");
                    commands.add("night");
                    commands.add("midnight");
                } else if (args[0].equalsIgnoreCase("add") | args[0].equalsIgnoreCase("remove")) {
                    commands.add("10");
                    commands.add("100");
                    commands.add("1000");
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set") | args[0].equalsIgnoreCase("add")) {
                    commands.addAll(getWorlds().getListed());
                }
            }
        }
        return commands;
    }
}