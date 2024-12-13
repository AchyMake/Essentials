package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
import org.achymake.essentials.handlers.MaterialHandler;
import org.achymake.essentials.handlers.RandomHandler;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class CreatureSpawn implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Entities getEntities() {
        return getInstance().getEntities();
    }
    private RandomHandler getRandomHandler() {
        return getInstance().getRandomHandler();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public CreatureSpawn() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        var entity = event.getEntity();
        if (entity instanceof Player)return;
        if (!getEntities().disableSpawnReason(event.getEntityType(), event.getSpawnReason())) {
            if (!getEntities().disableCreatureSpawn(event.getEntityType())) {
                var chunkLimit = getEntities().chunkLimit(event.getEntityType());
                if (chunkLimit > 0) {
                    var chunk = event.getLocation().getChunk();
                    var listed = new ArrayList<Entity>();
                    for (var entities : chunk.getEntities()) {
                        if (entities.getType().equals(event.getEntityType())) {
                            listed.add(entity);
                        }
                    }
                    if (listed.size() >= chunkLimit) {
                        event.setCancelled(true);
                    }
                    listed.clear();
                }
                var equipment = getEntities().getEquipment(entity);
                var config = getEntities().getConfig(event.getEntityType());
                config.getConfigurationSection("levels").getKeys(false).forEach(level -> {
                    var section = "levels." + level;
                    if (getRandomHandler().isTrue(config.getDouble(section + ".chance"))) {
                        if (config.isDouble(section + ".health")) {
                            entity.setMaxHealth(config.getDouble(section + ".health"));
                            entity.setHealth(config.getDouble(section + ".health"));
                        }
                        if (config.isDouble(section + ".scale")) {
                            entity.getAttribute(Attribute.SCALE).setBaseValue(config.getDouble(section + ".scale"));
                        }
                        if (config.isString(section + ".main-hand.item") && config.isInt(section + ".main-hand.amount")) {
                            equipment.setItemInMainHand(getMaterials().getItemStack(config.getString(section + ".main-hand.item"), config.getInt(section + ".main-hand.amount")));
                        }
                        if (config.isDouble(section + ".main-hand.drop-chance")) {
                            equipment.setItemInMainHandDropChance((float) config.getDouble(section + ".main-hand.drop-chance"));
                        }
                        if (config.isString(section + ".off-hand.item") && config.isInt(section + ".off-hand.amount")) {
                            equipment.setItemInOffHand(getMaterials().getItemStack(config.getString(section + ".off-hand.item"), config.getInt(section + ".off-hand.amount")));
                        }
                        if (config.isDouble(section + ".off-hand.drop-chance")) {
                            equipment.setItemInOffHandDropChance((float) config.getDouble(section + ".off-hand.drop-chance"));
                        }
                        if (config.isString(section + ".helmet.item") && config.isInt(section + ".helmet.amount")) {
                            equipment.setHelmet(getMaterials().getItemStack(config.getString(section + ".helmet.item"), config.getInt(section + ".helmet.amount")));
                        }
                        if (config.isDouble(section + ".helmet.drop-chance")) {
                            equipment.setHelmetDropChance((float) config.getDouble(section + ".helmet.drop-chance"));
                        }
                        if (config.isString(section + ".chestplate.item") && config.isInt(section + ".chestplate.amount")) {
                            equipment.setChestplate(getMaterials().getItemStack(config.getString(section + ".chestplate.item"), config.getInt(section + ".chestplate.amount")));
                        }
                        if (config.isDouble(section + ".chestplate.drop-chance")) {
                            equipment.setChestplateDropChance((float) config.getDouble(section + ".chestplate.drop-chance"));
                        }
                        if (config.isString(section + ".leggings.item") && config.isInt(section + ".leggings.amount")) {
                            equipment.setLeggings(getMaterials().getItemStack(config.getString(section + ".leggings.item"), config.getInt(section + ".leggings.amount")));
                        }
                        if (config.isDouble(section + ".leggings.drop-chance")) {
                            equipment.setLeggingsDropChance((float) config.getDouble(section + ".leggings.drop-chance"));
                        }
                        if (config.isString(section + ".boots.item") && config.isInt(section + ".boots.amount")) {
                            equipment.setBoots(getMaterials().getItemStack(config.getString(section + ".boots.item"), config.getInt(section + ".boots.amount")));
                        }
                        if (config.isDouble(section + ".boots.drop-chance")) {
                            equipment.setBootsDropChance((float) config.getDouble(section + ".boots.drop-chance"));
                        }
                    }
                });
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }
}