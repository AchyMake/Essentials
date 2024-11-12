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
    public WorldCommand() {
        getInstance().getCommand("world").setExecutor(this);
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                var worldHandler = getWorldHandler(player.getWorld());
                if (args[0].equalsIgnoreCase("setspawn")) {
                    worldHandler.setSpawn(player.getLocation());
                    player.sendMessage(getMessage().get("commands.world.setspawn", worldHandler.getName()));
                    return true;
                } else if (args[0].equalsIgnoreCase("pvp")) {
                    var world = player.getWorld();
                    world.setPVP(!world.getPVP());
                    if (world.getPVP()) {
                        player.sendMessage(getMessage().get("commands.world.pvp.enable", worldHandler.getName()));
                    } else player.sendMessage(getMessage().get("commands.world.pvp.disable", worldHandler.getName()));
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("teleport")) {
                    var world = getWorlds().get(args[1]);
                    if (world != null) {
                        getMessage().sendActionBar(player, getMessage().get("events.teleport.success", args[1]));
                        player.teleport(getWorldHandler(world).getSpawn());
                    } else player.sendMessage(getMessage().get("error.world.invalid", args[1]));
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    var world = getWorlds().get(args[1]);
                    if (world != null) {
                        getWorldHandler(world).remove();
                        player.sendMessage(getMessage().get("commands.world.remove", args[1]));
                    } else player.sendMessage(getMessage().get("error.world.invalid", args[1]));
                    return true;
                } else if (args[0].equalsIgnoreCase("pvp")) {
                    var world = getWorlds().get(args[1]);
                    if (world != null) {
                        world.setPVP(!world.getPVP());
                        if (world.getPVP()) {
                            player.sendMessage(getMessage().get("commands.world.pvp.enable", args[1]));
                        } else player.sendMessage(getMessage().get("commands.world.pvp.disable", args[1]));
                    } else player.sendMessage(getMessage().get("error.world.invalid", args[1]));
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("pvp")) {
                    var world = getWorlds().get(args[1]);
                    if (world != null) {
                        world.setPVP(Boolean.parseBoolean(args[2]));
                        if (world.getPVP()) {
                            player.sendMessage(getMessage().get("commands.world.pvp.enable", world.getName()));
                        } else player.sendMessage(getMessage().get("commands.world.pvp.disable", world.getName()));
                    } else player.sendMessage(getMessage().get("error.world.invalid", args[1]));
                    return true;
                } else if (args[0].equalsIgnoreCase("create")) {
                    if (!getWorlds().getFolder(args[1]).exists()) {
                        player.sendMessage(getMessage().get("creator.post", args[1]));
                        var info = getWorlds().create(args[1], World.Environment.valueOf(args[2].toUpperCase()));
                        player.sendMessage(getMessage().get("creator.title", info.getName()));
                        player.sendMessage(getMessage().get("creator.environment", info.getEnvironment().name()));
                        player.sendMessage(getMessage().get("creator.seed", String.valueOf(info.getSeed())));
                    } else player.sendMessage(getMessage().get("error.world.folder-exists", args[1]));
                    return true;
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (getWorlds().get(args[1]) == null) {
                        if (getWorlds().getFolder(args[1]).exists()) {
                            player.sendMessage(getMessage().get("creator.post", args[1]));
                            var info = getWorlds().create(args[1], World.Environment.valueOf(args[2].toUpperCase()));
                            player.sendMessage(getMessage().get("creator.title", info.getName()));
                            player.sendMessage(getMessage().get("creator.environment", info.getEnvironment().name()));
                            player.sendMessage(getMessage().get("creator.seed", String.valueOf(info.getSeed())));
                        } else player.sendMessage(getMessage().get("error.world.folder-invalid", args[1]));
                    } else player.sendMessage(getMessage().get("error.world.exists", args[1]));
                    return true;
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("gamerule")) {
                    var world = getWorlds().get(args[1]);
                    if (world != null) {
                        world.setGameRuleValue(args[2], args[3]);
                        player.sendMessage(getMessage().get("commands.world.gamerule.changed", args[1], args[2], args[3]));
                    } else player.sendMessage(getMessage().get("error.world.invalid", args[1]));
                    return true;
                } else if (args[0].equalsIgnoreCase("create")) {
                    if (args[3].equalsIgnoreCase("random")) {
                        if (!getWorlds().getFolder(args[1]).exists()) {
                            player.sendMessage(getMessage().get("creator.post", args[1]));
                            var info = getWorlds().createRandom(args[1], World.Environment.valueOf(args[2].toUpperCase()));
                            player.sendMessage(getMessage().get("creator.title", info.getName()));
                            player.sendMessage(getMessage().get("creator.environment", info.getEnvironment().name()));
                            player.sendMessage(getMessage().get("creator.seed", String.valueOf(info.getSeed())));
                        } else player.sendMessage(getMessage().get("error.world.folder-exists", args[1]));
                    } else if (!getWorlds().getFolder(args[1]).exists()) {
                        player.sendMessage(getMessage().get("creator.post", args[1]));
                        var info = getWorlds().create(args[1], World.Environment.valueOf(args[2].toUpperCase()), Long.parseLong(args[3]));
                        player.sendMessage(getMessage().get("creator.title", info.getName()));
                        player.sendMessage(getMessage().get("creator.environment", info.getEnvironment().name()));
                        player.sendMessage(getMessage().get("creator.seed", String.valueOf(info.getSeed())));
                    } else player.sendMessage(getMessage().get("error.world.folder-exists", args[1]));
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