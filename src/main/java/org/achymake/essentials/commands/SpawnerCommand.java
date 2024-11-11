package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnerCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SpawnerCommand() {
        getInstance().getCommand("spawners").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("essentials.command.spawner.info")) {
                        var block = player.getTargetBlockExact(1);
                        if (block != null) {
                            if (block.getState() instanceof CreatureSpawner spawner) {
                                player.sendMessage(getMessage().get("commands.spawner.info.title"));
                                player.sendMessage(getMessage().get("commands.spawner.info.entity-type", getMessage().toTitleCase(spawner.getSpawnedType().toString())));
                                player.sendMessage(getMessage().get("commands.spawner.info.spawn-count", String.valueOf(spawner.getSpawnCount())));
                                player.sendMessage(getMessage().get("commands.spawner.info.delay", String.valueOf(spawner.getDelay())));
                                player.sendMessage(getMessage().get("commands.spawner.info.max-spawn-delay", String.valueOf(spawner.getMaxSpawnDelay())));
                                player.sendMessage(getMessage().get("commands.spawner.info.min-spawn-delay", String.valueOf(spawner.getMinSpawnDelay())));
                                player.sendMessage(getMessage().get("commands.spawner.info.spawn-range", String.valueOf(spawner.getSpawnRange())));
                            } else player.sendMessage(getMessage().get("commands.spawner.info.non-spawner"));
                        } else player.sendMessage(getMessage().get("commands.spawner.info.invalid"));
                        return true;
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (player.hasPermission("essentials.command.spawner.set")) {
                        if (args[1].equalsIgnoreCase("entity-type")) {
                            var block = player.getTargetBlockExact(1);
                            if (block != null) {
                                if (block.getState() instanceof CreatureSpawner spawner) {
                                    var entityTypeString = args[2].toUpperCase();
                                    spawner.setSpawnedType(EntityType.valueOf(entityTypeString));
                                    spawner.update();
                                    player.sendMessage(getMessage().get("commands.spawner.set.entity-type", getMessage().toTitleCase(entityTypeString)));
                                } else player.sendMessage(getMessage().get("commands.spawner.info.non-spawner"));
                            } else player.sendMessage(getMessage().get("commands.spawner.info.invalid"));
                            return true;
                        } else if (args[1].equalsIgnoreCase("spawn-count")) {
                            if (player.hasPermission("essentials.command.spawner.set")) {
                                var block = player.getTargetBlockExact(1);
                                if (block != null) {
                                    if (block.getState() instanceof CreatureSpawner spawner) {
                                        var value = Integer.parseInt(args[2]);
                                        if (value > 0) {
                                            spawner.setSpawnCount(value);
                                            spawner.update();
                                            player.sendMessage(getMessage().get("commands.spawner.set.spawn-count", String.valueOf(value)));
                                        }
                                    } else player.sendMessage(getMessage().get("commands.spawner.info.non-spawner"));
                                } else player.sendMessage(getMessage().get("commands.spawner.info.invalid"));
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("max-spawn-delay")) {
                            if (player.hasPermission("essentials.command.spawner.set")) {
                                var block = player.getTargetBlockExact(1);
                                if (block != null) {
                                    if (block.getState() instanceof CreatureSpawner spawner) {
                                        var value = Integer.parseInt(args[2]);
                                        if (value > 0) {
                                            spawner.setMaxSpawnDelay(value);
                                            spawner.update();
                                            player.sendMessage(getMessage().get("commands.spawner.set.max-spawn-delay", String.valueOf(value)));
                                        }
                                    } else player.sendMessage(getMessage().get("commands.spawner.info.non-spawner"));
                                } else player.sendMessage(getMessage().get("commands.spawner.info.invalid"));
                                return true;
                            }
                        } else if (args[1].equalsIgnoreCase("min-spawn-delay")) {
                            if (player.hasPermission("essentials.command.spawner.set")) {
                                var block = player.getTargetBlockExact(1);
                                if (block != null) {
                                    if (block.getState() instanceof CreatureSpawner spawner) {
                                        var value = Integer.parseInt(args[2]);
                                        if (value > 0) {
                                            spawner.setMinSpawnDelay(value);
                                            spawner.update();
                                            player.sendMessage(getMessage().get("commands.spawner.set.min-spawn-delay", String.valueOf(value)));
                                        }
                                    } else player.sendMessage(getMessage().get("commands.spawner.info.non-spawner"));
                                } else player.sendMessage(getMessage().get("commands.spawner.info.invalid"));
                                return true;
                            }
                        }
                    }
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("essentials.command.spawner.give")) {
                        var target = player.getServer().getPlayerExact(args[1]);
                        if (target != null) {
                            var entityType = args[2].toUpperCase();
                            int amount = Integer.parseInt(args[3]);
                            if (amount > 0) {
                                getMaterials().giveItemStack(target, getMaterials().getSpawner(entityType, amount));
                                player.sendMessage(getMessage().get("commands.spawner.give", target.getName(), String.valueOf(amount), getMessage().toTitleCase(entityType)));
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender consoleCommandSender) {
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    var target = consoleCommandSender.getServer().getPlayerExact(args[1]);
                    if (target != null) {
                        var entityType = args[2].toUpperCase();
                        int amount = Integer.parseInt(args[3]);
                        if (amount > 0) {
                            getMaterials().giveItemStack(target, getMaterials().getSpawner(entityType, amount));
                            consoleCommandSender.sendMessage(getMessage().get("commands.spawner.give", target.getName(), String.valueOf(amount), getMessage().toTitleCase(entityType)));
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
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.spawner.give")) {
                    commands.add("give");
                }
                if (player.hasPermission("essentials.command.spawner.info")) {
                    commands.add("info");
                }
                if (player.hasPermission("essentials.command.spawner.set")) {
                    commands.add("set");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (player.hasPermission("essentials.command.spawner.set")) {
                        commands.add("entity-type");
                        commands.add("spawn-count");
                        commands.add("max-spawn-delay");
                        commands.add("min-spawn-delay");
                        commands.add("spawn-range");
                    }
                } else if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("essentials.command.spawner.give")) {
                        getInstance().getOnlinePlayers().forEach(players -> {
                            if (players.getName().startsWith(args[1])) {
                                commands.add(players.getName());
                            }
                        });
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (player.hasPermission("essentials.command.spawner.set")) {
                        if (args[1].equalsIgnoreCase("entity-type")) {
                            for (var types : EntityType.values()) {
                                var type = types.toString().toLowerCase();
                                if (type.startsWith(args[2])) {
                                    commands.add(type);
                                }
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("essentials.command.spawner.give")) {
                        for(var types : EntityType.values()) {
                            var type = types.toString().toLowerCase();
                            if (type.startsWith(args[2])) {
                                commands.add(type);
                            }
                        }
                    }
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("essentials.command.spawner.give")) {
                        commands.add("1");
                        commands.add("5");
                        commands.add("10");
                    }
                }
            } else if (args.length == 5) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (player.hasPermission("essentials.command.spawner.give")) {
                        commands.add("1");
                        commands.add("2");
                        commands.add("3");
                        commands.add("4");
                    }
                }
            }
        }
        return commands;
    }
}