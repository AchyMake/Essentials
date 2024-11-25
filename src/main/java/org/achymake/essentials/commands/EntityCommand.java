package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
import org.achymake.essentials.data.Message;
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
    private Entities getEntities() {
        return getInstance().getEntityHandler();
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
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntities().setBoolean(entityType, "hostile", value);
                    player.sendMessage(getMessage().get("commands.entity.hostile", entityName, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("chunk-limit")) {
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var limit = Integer.parseInt(args[2]);
                    getEntities().setInt(entityType, "chunk-limit", limit);
                    player.sendMessage(getMessage().get("commands.entity.chunk-limit", entityName, String.valueOf(limit)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-spawn")) {
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntities().setBoolean(entityType, "disable-spawn", value);
                    player.sendMessage(getMessage().get("commands.entity.disable-spawn", entityName, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-block-form")) {
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntities().setBoolean(entityType, "disable-block-form", value);
                    player.sendMessage(getMessage().get("commands.entity.disable-block-form", entityName, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-block-damage")) {
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntities().setBoolean(entityType, "disable-block-damage", value);
                    player.sendMessage(getMessage().get("commands.entity.disable-block-damage", entityName, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-block-change")) {
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var value = Boolean.parseBoolean(args[2]);
                    getEntities().setBoolean(entityType, "disable-block-change", value);
                    player.sendMessage(getMessage().get("commands.entity.disable-block-change", entityName, String.valueOf(value)));
                    return true;
                }
            } else if (args.length == 4) {
                if (args[1].equalsIgnoreCase("disable-block-interact")) {
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var blockType = args[2].toUpperCase();
                    var value = Boolean.parseBoolean(args[3]);
                    getEntities().setBoolean(entityType, "disable-block-interact." + blockType, value);
                    player.sendMessage(getMessage().get("commands.entity.disable-block-interact", entityName, blockType, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-target")) {
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var targetType = args[2].toUpperCase();
                    var value = Boolean.parseBoolean(args[3]);
                    getEntities().setBoolean(entityType, "disable-target." + targetType, value);
                    player.sendMessage(getMessage().get("commands.entity.disable-target", entityName, targetType, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disable-damage")) {
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var targetType = args[2].toUpperCase();
                    var value = Boolean.parseBoolean(args[3]);
                    getEntities().setBoolean(entityType, "disable-damage." + targetType, value);
                    player.sendMessage(getMessage().get("commands.entity.disable-damage", entityName, targetType, String.valueOf(value)));
                    return true;
                } else if (args[1].equalsIgnoreCase("disabled-spawn-reason")) {
                    var entityType = EntityType.valueOf(args[0].toUpperCase());
                    var entityName = getMessage().toTitleCase(entityType.toString());
                    var spawnReason = args[2].toUpperCase();
                    var value = Boolean.parseBoolean(args[3]);
                    getEntities().setBoolean(entityType, "disabled-spawn-reason." + spawnReason, value);
                    player.sendMessage(getMessage().get("commands.entity.disabled-spawn-reason", entityName, spawnReason, String.valueOf(value)));
                    return true;
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
                for (var entityType : EntityType.values()) {
                    var entityName = entityType.name().toLowerCase();
                    if (!entityName.equalsIgnoreCase("player")) {
                        if (entityName.startsWith(args[0])) {
                            commands.add(entityName);
                        }
                    }
                }
            }
            if (args.length == 2) {
                var entityType = EntityType.valueOf(args[0].toUpperCase());
                if (getEntities().exists(entityType)) {
                    commands.add("hostile");
                    commands.add("chunk-limit");
                    commands.add("disable-spawn");
                    commands.add("disable-block-form");
                    commands.add("disable-block-damage");
                    commands.add("disable-block-change");
                    commands.add("disable-block-interact");
                    commands.add("disable-target");
                    commands.add("disable-damage");
                    commands.add("disabled-spawn-reason");
                }
            }
            if (args.length == 3) {
                var entityType = EntityType.valueOf(args[0].toUpperCase());
                if (getEntities().exists(entityType)) {
                    if (args[1].equalsIgnoreCase("hostile")) {
                        commands.add(String.valueOf(getEntities().isHostile(entityType)));
                    }
                    if (args[1].equalsIgnoreCase("chunk-limit")) {
                        commands.add(String.valueOf(getEntities().chunkLimit(entityType)));
                    }
                    if (args[1].equalsIgnoreCase("disable-spawn")) {
                        commands.add(String.valueOf(getEntities().disableSpawn(entityType)));
                    }
                    if (args[1].equalsIgnoreCase("disable-block-form")) {
                        commands.add(String.valueOf(getEntities().disableBlockForm(entityType)));
                    }
                    if (args[1].equalsIgnoreCase("disable-block-damage")) {
                        commands.add(String.valueOf(getEntities().disableBlockDamage(entityType)));
                    }
                    if (args[1].equalsIgnoreCase("disable-block-change")) {
                        commands.add(String.valueOf(getEntities().disableBlockChange(entityType)));
                    }
                    if (args[1].equalsIgnoreCase("disable-block-interact")) {
                        for (var material : Material.values()) {
                            var materialName = material.name().toLowerCase();
                            if (materialName.startsWith(args[2])) {
                                commands.add(materialName);
                            }
                        }
                    }
                    if (args[1].equalsIgnoreCase("disable-target") || args[1].equalsIgnoreCase("disable-damage")) {
                        for (var entityTypes : EntityType.values()) {
                            var entityName = entityTypes.name().toLowerCase();
                            if (entityName.startsWith(args[2])) {
                                commands.add(entityName);
                            }
                        }
                    }
                    if (args[1].equalsIgnoreCase("disabled-spawn-reason")) {
                        for (var spawnReasons : CreatureSpawnEvent.SpawnReason.values()) {
                            var reasons = spawnReasons.name().toLowerCase();
                            if (reasons.startsWith(args[2])) {
                                commands.add(reasons);
                            }
                        }
                    }
                }
            }
            if (args.length == 4) {
                var entityType = EntityType.valueOf(args[0].toUpperCase());
                if (getEntities().exists(entityType)) {
                    if (args[1].equalsIgnoreCase("disable-block-interact")) {
                        var blockType = Material.valueOf(args[2].toUpperCase());
                        commands.add(String.valueOf(getEntities().disableBlockInteract(entityType, blockType)));
                    }
                    if (args[1].equalsIgnoreCase("disable-target")) {
                        var targetType = EntityType.valueOf(args[2].toUpperCase());
                        commands.add(String.valueOf(getEntities().disableTarget(entityType, targetType)));
                    }
                    if (args[1].equalsIgnoreCase("disable-damage")) {
                        var targetType = EntityType.valueOf(args[2].toUpperCase());
                        commands.add(String.valueOf(getEntities().disableDamage(entityType, targetType)));
                    }
                    if (args[1].equalsIgnoreCase("disabled-spawn-reason")) {
                        var spawnReason = CreatureSpawnEvent.SpawnReason.valueOf(args[2].toUpperCase());
                        commands.add(String.valueOf(getEntities().disabledSpawnReason(entityType, spawnReason)));
                    }
                }
            }
        }
        return commands;
    }
}