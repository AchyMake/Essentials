package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Entities;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Entities getEntityHandler() {
        return getInstance().getEntities();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public EntityCommand() {
        getInstance().getCommand("entity").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("hostile")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntityHandler().setBoolean(entityType, "settings.hostile", value);
                    player.sendMessage(getMessage().get("commands.entity.hostile", entityName, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("chunk-limit")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var limit = Integer.parseInt(args[2]);
                    getEntityHandler().setInt(entityType, "settings.chunk-limit", limit);
                    player.sendMessage(getMessage().get("commands.entity.chunk-limit", entityName, String.valueOf(limit)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-spawn")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntityHandler().setBoolean(entityType, "settings.disable-spawn", value);
                    player.sendMessage(getMessage().get("commands.entity.disable-spawn", entityName, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-block-form")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntityHandler().setBoolean(entityType, "settings.disable-block-form", value);
                    player.sendMessage(getMessage().get("commands.entity.disable-block-form", entityName, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-explode")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntityHandler().setBoolean(entityType, "settings.disable-explode", value);
                    player.sendMessage(getMessage().get("commands.entity.disable-explode", entityName, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-change-block")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntityHandler().setBoolean(entityType, "settings.disable-change-block", value);
                    player.sendMessage(getMessage().get("commands.entity.disable-change-block", entityName, String.valueOf(value)));
                    return true;
                }
            } else if (args.length == 4) {
                if (args[1].equalsIgnoreCase("disable-interact")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var blockType = args[2].toUpperCase();
                    var value = Boolean.parseBoolean(args[3]);
                    getEntityHandler().setBoolean(entityType, "settings.disable-interact." + blockType, value);
                    player.sendMessage(getMessage().get("commands.entity.disable-interact", entityName, blockType, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-target")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var targetType = args[2].toUpperCase();
                    var value = Boolean.parseBoolean(args[3]);
                    getEntityHandler().setBoolean(entityType, "settings.disable-target." + targetType, value);
                    player.sendMessage(getMessage().get("commands.entity.disable-target", entityName, targetType, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-entity-damage")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var targetType = args[2].toUpperCase();
                    var value = Boolean.parseBoolean(args[3]);
                    getEntityHandler().setBoolean(entityType, "settings.disable-entity-damage." + targetType, value);
                    player.sendMessage(getMessage().get("commands.entity.disable-entity-damage", entityName, targetType, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-hanging-break")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var targetType = args[2].toUpperCase();
                    var value = Boolean.parseBoolean(args[3]);
                    getEntityHandler().setBoolean(entityType, "settings.disable-hanging-break." + targetType, value);
                    player.sendMessage(getMessage().get("commands.entity.disable-hanging-break", entityName, targetType, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-spawn-reason")) {
                    var entityType = getEntityHandler().getType(args[0]);
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var spawnReason = args[2].toUpperCase();
                    var value = Boolean.parseBoolean(args[3]);
                    getEntityHandler().setBoolean(entityType, "settings.disable-spawn-reason." + spawnReason, value);
                    player.sendMessage(getMessage().get("commands.entity.disable-spawn-reason", entityName, spawnReason, String.valueOf(value)));
                    return true;
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
                for (var entityType : EntityType.values()) {
                    if (entityType != player.getType()) {
                        var entityName = entityType.name().toLowerCase();
                        if (entityName.startsWith(args[0])) {
                            commands.add(entityName);
                        }
                    }
                }
            } else if (args.length == 2) {
                var entityType = getEntityHandler().getType(args[0]);
                if (getEntityHandler().exists(entityType)) {
                    commands.addAll(getEntityHandler().getConfig(entityType).getConfigurationSection("settings").getKeys(false));
                }
            } else if (args.length == 3) {
                var entityType = getEntityHandler().getType(args[0]);
                if (getEntityHandler().exists(entityType)) {
                    if (args[1].equalsIgnoreCase("hostile")) {
                        commands.add(String.valueOf(getEntityHandler().isHostile(entityType)));
                    } else if (args[1].equalsIgnoreCase("chunk-limit")) {
                        commands.add(String.valueOf(getEntityHandler().chunkLimit(entityType)));
                    } else if (args[1].equalsIgnoreCase("disable-spawn")) {
                        commands.add(String.valueOf(getEntityHandler().disableCreatureSpawn(entityType)));
                    } else if (args[1].equalsIgnoreCase("disable-block-form")) {
                        commands.add(String.valueOf(getEntityHandler().disableEntityBlockForm(entityType)));
                    } else if (args[1].equalsIgnoreCase("disable-change-block")) {
                        commands.add(String.valueOf(getEntityHandler().disableEntityChangeBlock(entityType)));
                    } else if (args[1].equalsIgnoreCase("disable-explode")) {
                        commands.add(String.valueOf(getEntityHandler().disableEntityExplode(entityType)));
                    } else if (args[1].equalsIgnoreCase("disable-interact")) {
                        for (var material : Material.values()) {
                            var materialName = material.name().toLowerCase();
                            if (materialName.startsWith(args[2])) {
                                commands.add(materialName);
                            }
                        }
                    } else if (args[1].equalsIgnoreCase("disable-target") || args[1].equalsIgnoreCase("disable-hanging-break") || args[1].equalsIgnoreCase("disable-entity-damage")) {
                        for (var entityTypes : EntityType.values()) {
                            var entityName = entityTypes.name().toLowerCase();
                            if (entityName.startsWith(args[2])) {
                                commands.add(entityName);
                            }
                        }
                    } else if (args[1].equalsIgnoreCase("disable-spawn-reason")) {
                        for (var spawnReasons : CreatureSpawnEvent.SpawnReason.values()) {
                            var reasons = spawnReasons.name().toLowerCase();
                            if (reasons.startsWith(args[2])) {
                                commands.add(reasons);
                            }
                        }
                    }
                }
            } else if (args.length == 4) {
                var entityType = getEntityHandler().getType(args[0]);
                if (getEntityHandler().exists(entityType)) {
                    if (args[1].equalsIgnoreCase("disable-interact")) {
                        var blockType = getInstance().getMaterialHandler().get(args[2]);
                        commands.add(String.valueOf(getEntityHandler().disableEntityInteract(entityType, blockType)));
                    } else if (args[1].equalsIgnoreCase("disable-target")) {
                        var targetType = getEntityHandler().getType(args[2]);
                        commands.add(String.valueOf(getEntityHandler().disableEntityTarget(entityType, targetType)));
                    } else if (args[1].equalsIgnoreCase("disable-entity-damage")) {
                        var targetType = getEntityHandler().getType(args[2]);
                        commands.add(String.valueOf(getEntityHandler().disableEntityDamageByEntity(entityType, targetType)));
                    } else if (args[1].equalsIgnoreCase("disable-hanging-break")) {
                        var targetType = getEntityHandler().getType(args[2]);
                        commands.add(String.valueOf(getEntityHandler().disableHangingBreakByEntity(entityType, targetType)));
                    } else if (args[1].equalsIgnoreCase("disable-spawn-reason")) {
                        var spawnReason = CreatureSpawnEvent.SpawnReason.valueOf(args[2].toUpperCase());
                        commands.add(String.valueOf(getEntityHandler().disableSpawnReason(entityType, spawnReason)));
                    }
                }
            }
        }
        return commands;
    }
}