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
                if (args[0].equalsIgnoreCase("add")) {
                    var worldName = args[1];
                    if (getWorlds().get(worldName) == null) {
                        if (getWorlds().getFolder(worldName).exists()) {
                            player.sendMessage(getMessage().get("creator.post", worldName));
                            var info = getWorlds().add(worldName);
                            player.sendMessage(getMessage().get("creator.title", info.getName()));
                            player.sendMessage(getMessage().get("creator.environment", info.getEnvironment().name()));
                            player.sendMessage(getMessage().get("creator.seed", String.valueOf(info.getSeed())));
                        } else player.sendMessage(getMessage().get("error.world.folder-invalid", worldName));
                    } else player.sendMessage(getMessage().get("error.world.exists", worldName));
                    return true;
                } else if (args[0].equalsIgnoreCase("info")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        player.sendMessage(getMessage().get("commands.world.info.title", world.getName()));
                        player.sendMessage(getMessage().get("commands.world.info.pvp", String.valueOf(world.getPVP())));
                        player.sendMessage(getMessage().get("commands.world.info.difficulty", getMessage().toTitleCase(world.getDifficulty().name())));
                        player.sendMessage(getMessage().get("commands.world.info.seed", String.valueOf(world.getSeed())));
                        player.sendMessage(getMessage().get("commands.world.info.environment", getMessage().toTitleCase(world.getEnvironment().name())));
                        player.sendMessage(getMessage().get("commands.world.info.gamerule.title"));
                        for (var gameRules : world.getGameRules()) {
                            player.sendMessage(getMessage().get("commands.world.info.gamerule.listed", gameRules, world.getGameRuleValue(gameRules)));
                        }
                    } else player.sendMessage(getMessage().get("error.world.invalid", worldName));
                    return true;
                } else if (args[0].equalsIgnoreCase("pvp")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        world.setPVP(!world.getPVP());
                        if (world.getPVP()) {
                            player.sendMessage(getMessage().get("commands.world.pvp.enable", worldName));
                        } else player.sendMessage(getMessage().get("commands.world.pvp.disable", worldName));
                    } else player.sendMessage(getMessage().get("error.world.invalid", worldName));
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        getWorldHandler(world).remove();
                        player.sendMessage(getMessage().get("commands.world.remove", worldName));
                    } else player.sendMessage(getMessage().get("error.world.invalid", worldName));
                    return true;
                } else if (args[0].equalsIgnoreCase("teleport")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        getMessage().sendActionBar(player, getMessage().get("events.teleport.success", worldName));
                        player.teleport(getWorldHandler(world).getSpawn());
                    } else player.sendMessage(getMessage().get("error.world.invalid", worldName));
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("create")) {
                    var worldName = args[1];
                    if (!getWorlds().getFolder(worldName).exists()) {
                        player.sendMessage(getMessage().get("creator.post", worldName));
                        var info = getWorlds().create(worldName, World.Environment.valueOf(args[2].toUpperCase()));
                        player.sendMessage(getMessage().get("creator.title", info.getName()));
                        player.sendMessage(getMessage().get("creator.environment", info.getEnvironment().name()));
                        player.sendMessage(getMessage().get("creator.seed", String.valueOf(info.getSeed())));
                    } else player.sendMessage(getMessage().get("error.world.folder-exists", worldName));
                    return true;
                } else if (args[0].equalsIgnoreCase("difficulty")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        if (getWorldHandler(world).setDifficulty(args[2])) {
                            player.sendMessage(getMessage().get("commands.world.difficulty.success", world.getName(), getMessage().toTitleCase(args[2])));
                        } else player.sendMessage(getMessage().get("commands.world.difficulty.invalid", args[2]));
                    } else player.sendMessage(getMessage().get("error.world.invalid", worldName));
                    return true;
                } else if (args[0].equalsIgnoreCase("pvp")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        world.setPVP(Boolean.parseBoolean(args[2]));
                        if (world.getPVP()) {
                            player.sendMessage(getMessage().get("commands.world.pvp.enable", world.getName()));
                        } else player.sendMessage(getMessage().get("commands.world.pvp.disable", world.getName()));
                    } else player.sendMessage(getMessage().get("error.world.invalid", worldName));
                    return true;
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("create")) {
                    var worldName = args[1];
                    if (args[3].equalsIgnoreCase("random")) {
                        if (!getWorlds().getFolder(worldName).exists()) {
                            player.sendMessage(getMessage().get("creator.post", worldName));
                            var info = getWorlds().createRandom(worldName, World.Environment.valueOf(args[2].toUpperCase()));
                            player.sendMessage(getMessage().get("creator.title", info.getName()));
                            player.sendMessage(getMessage().get("creator.environment", info.getEnvironment().name()));
                            player.sendMessage(getMessage().get("creator.seed", String.valueOf(info.getSeed())));
                        } else player.sendMessage(getMessage().get("error.world.folder-exists", worldName));
                    } else if (!getWorlds().getFolder(worldName).exists()) {
                        player.sendMessage(getMessage().get("creator.post", worldName));
                        var info = getWorlds().create(worldName, World.Environment.valueOf(args[2].toUpperCase()), Long.parseLong(args[3]));
                        player.sendMessage(getMessage().get("creator.title", info.getName()));
                        player.sendMessage(getMessage().get("creator.environment", info.getEnvironment().name()));
                        player.sendMessage(getMessage().get("creator.seed", String.valueOf(info.getSeed())));
                    } else player.sendMessage(getMessage().get("error.world.folder-exists", worldName));
                    return true;
                } else if (args[0].equalsIgnoreCase("gamerule")) {
                    var worldName = args[1];
                    var world = getWorlds().get(worldName);
                    if (world != null) {
                        world.setGameRuleValue(args[2], args[3]);
                        player.sendMessage(getMessage().get("commands.world.gamerule.changed", args[1], args[2], args[3]));
                    } else player.sendMessage(getMessage().get("error.world.invalid", worldName));
                    return true;
                }
            }
        }
        return false;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                commands.add("add");
                commands.add("create");
                commands.add("difficulty");
                commands.add("info");
                commands.add("gamerule");
                commands.add("pvp");
                commands.add("remove");
                commands.add("setspawn");
                commands.add("teleport");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("gamerule") ||
                        args[0].equalsIgnoreCase("pvp") ||
                        args[0].equalsIgnoreCase("remove") ||
                        args[0].equalsIgnoreCase("teleport") ||
                        args[0].equalsIgnoreCase("difficulty") ||
                        args[0].equalsIgnoreCase("info")) {
                    getWorlds().getListed().forEach(world -> {
                        if (args[1].startsWith(world.getName())) {
                            commands.add(world.getName());
                        }
                    });
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("create")) {
                    commands.add("normal");
                    commands.add("nether");
                    commands.add("the_end");
                } else if (args[0].equalsIgnoreCase("difficulty")) {
                    if (getWorlds().get(args[1]) != null) {
                        commands.add("peaceful");
                        commands.add("easy");
                        commands.add("normal");
                        commands.add("hard");
                    }
                } else if (args[0].equalsIgnoreCase("pvp")) {
                    commands.add(String.valueOf(getWorlds().get(args[1]).getPVP()));
                } else if (args[0].equalsIgnoreCase("gamerule")) {
                    Collections.addAll(commands, getWorlds().get(args[1]).getGameRules());
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("gamerule")) {
                    commands.add(getWorlds().get(args[1]).getGameRuleValue(args[2]));
                } else if (args[0].equalsIgnoreCase("create")) {
                    commands.add("random");
                }
            }
        }
        return commands;
    }
}