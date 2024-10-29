package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Worlds;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldCommand implements CommandExecutor, TabCompleter {
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
    private Server getServer() {
        return getInstance().getServer();
    }
    public WorldCommand() {
        getInstance().getCommand("world").setExecutor(this);
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("setspawn")) {
                    getWorldHandler(player.getWorld()).setSpawn(player.getLocation());
                    getMessage().send(player, player.getWorld().getName() + "&6 changed spawn point");
                    return true;
                }
                if (args[0].equalsIgnoreCase("pvp")) {
                    player.getWorld().setPVP(!player.getWorld().getPVP());
                    if (player.getWorld().getPVP()) {
                        getMessage().send(player, player.getWorld().getName() + "&6 is now pvp mode");
                    } else getMessage().send(player, player.getWorld().getName() + "&6 is no longer pvp mode");
                    return true;
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        getMessage().sendActionBar(player, "&6Teleporting to&f " + worldName);
                        player.teleport(getWorldHandler(world).getSpawn());
                    } else getMessage().send(player, worldName + "&c does not exist");
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        getWorldHandler(world).remove();
                        getMessage().send(player, worldName + "&6 is saved and removed");
                    } else getMessage().send(player, worldName + "&c does not exist");
                    return true;
                }
                if (args[0].equalsIgnoreCase("pvp")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        world.setPVP(!world.getPVP());
                        if (world.getPVP()) {
                            getMessage().send(player, worldName + "&6 is now pvp mode");
                        } else getMessage().send(player, worldName  + "&6 is no longer pvp mode");
                    } else getMessage().send(player, worldName + "&c does not exist");
                    return true;
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("pvp")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    var value = Boolean.parseBoolean(args[2]);
                    if (world != null) {
                        world.setPVP(value);
                        if (world.getPVP()) {
                            getMessage().send(player, worldName + "&6 is now pvp mode");
                        } else getMessage().send(player, worldName + "&6 is no longer pvp mode");
                    } else getMessage().send(player, worldName + "&c does not exist");
                    return true;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    var worldName = args[1];
                    var environment = World.Environment.valueOf(args[2].toUpperCase());
                    if (getWorlds().getFolder(worldName).exists()) {
                        getMessage().send(player, worldName + "&c already exist, try use add instead of create");
                    } else {
                        getMessage().send(player, worldName + "&6 is about to be created");
                        var info = getWorlds().create(worldName, environment);
                        getMessage().send(player, info.getName() + "&6 has been created with the following:");
                        getMessage().send(player, "&6environment:&f " + info.getEnvironment().name());
                        getMessage().send(player, "&6seed:&f " + info.getSeed());
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("add")) {
                    var worldName = args[1];
                    var environment = World.Environment.valueOf(args[2].toUpperCase());
                    if (getWorlds().getFolder(worldName).exists()) {
                        if (getWorlds().get(worldName) == null) {
                            getMessage().send(player, worldName + "&6 is about to be added");
                            var info = getWorlds().create(worldName, environment);
                            getMessage().send(player, info.getName() + "&6 has been added with the following:");
                            getMessage().send(player, "&6environment:&f " + info.getEnvironment().name());
                            getMessage().send(player, "&6seed:&f " + info.getSeed());
                        } else getMessage().send(player, worldName + "&c already exist");
                    } else getMessage().send(player, worldName + "&c folder does not exist");
                    return true;
                }
            }
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("gamerule")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        var gamerule = args[2];
                        var value = args[3];
                        getServer().getWorld(worldName).setGameRuleValue(gamerule, value);
                        getMessage().send(player, worldName + "&6 changed&f " + gamerule + "&6 to&f " + value);
                    } else getMessage().send(player, worldName + "&c does not exist");
                    return true;
                }
                if (args[0].equalsIgnoreCase("create")) {
                    var worldName = args[1];
                    var environment = World.Environment.valueOf(args[2].toUpperCase());
                    if (args[3].equalsIgnoreCase("random")) {
                        if (getWorlds().getFolder(worldName).exists()) {
                            getMessage().send(player, worldName + "&c already exist");
                        } else {
                            getMessage().send(player, worldName + "&6 is about to be created");
                            var info = getWorlds().createRandom(worldName, environment);
                            getMessage().send(player, info.getName() + "&6 has been created with the following:");
                            getMessage().send(player, "&6environment:&f " + info.getEnvironment().name());
                            getMessage().send(player, "&6seed:&f " + info.getSeed());
                        }
                    } else {
                        var seed = Long.parseLong(args[3]);
                        if (getWorlds().getFolder(worldName).exists()) {
                            getMessage().send(player, worldName + "&c already exist");
                        } else {
                            getMessage().send(player, worldName + "&6 is about to be created");
                            var info = getWorlds().create(worldName, environment, seed);
                            getMessage().send(player, info.getName() + "&6 has been created with the following:");
                            getMessage().send(player, "&6environment:&f " + info.getEnvironment().name());
                            getMessage().send(player, "&6seed:&f " + info.getSeed());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                commands.add("add");
                commands.add("create");
                commands.add("gamerule");
                commands.add("pvp");
                commands.add("remove");
                commands.add("setspawn");
                commands.add("teleport");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("gamerule") | args[0].equalsIgnoreCase("pvp") | args[0].equalsIgnoreCase("remove") | args[0].equalsIgnoreCase("teleport")) {
                    for (var worlds : player.getServer().getWorlds()) {
                        commands.add(worlds.getName());
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    commands.add("normal");
                    commands.add("nether");
                    commands.add("the_end");
                }
                if (args[0].equalsIgnoreCase("create")) {
                    commands.add("normal");
                    commands.add("nether");
                    commands.add("the_end");
                }
                if (args[0].equalsIgnoreCase("pvp")) {
                    commands.add(String.valueOf(getWorlds().get(args[1]).getPVP()));
                }
                if (args[0].equalsIgnoreCase("gamerule")) {
                    Collections.addAll(commands, player.getServer().getWorld(args[1]).getGameRules());
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("gamerule")) {
                    commands.add(player.getServer().getWorld(args[1]).getGameRuleValue(args[2]));
                }
                if (args[0].equalsIgnoreCase("create")) {
                    commands.add("random");
                }
            }
        }
        return commands;
    }
}