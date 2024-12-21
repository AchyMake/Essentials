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
                var equipment = getEntities().getEquipment(entity);
                var config = getEntities().getConfig(event.getEntityType());
                if (config.isConfigurationSection("levels")) {
                    config.getConfigurationSection("levels").getKeys(false).forEach(level -> {
                        var section = "levels." + level;
                        if (config.isDouble(section + ".chance")) {
                            if (getRandomHandler().isTrue(config.getDouble(section + ".chance"))) {
                                if (config.isDouble(section + ".health")) {
                                    var health = config.getDouble(section + ".health");
                                    entity.setMaxHealth(health);
                                    entity.setHealth(health);
                                }
                                if (config.isDouble(section + ".scale")) {
                                    entity.getAttribute(Attribute.SCALE).setBaseValue(config.getDouble(section + ".scale"));
                                }
                                if (config.isString(section + ".main-hand.item") && config.isInt(section + ".main-hand.amount")) {
                                    var itemName = config.getString(section + ".main-hand.item");
                                    var itemAmount = config.getInt(section + ".main-hand.amount");
                                    var item = getMaterials().getItemStack(itemName, itemAmount);
                                    if (config.isConfigurationSection(section + ".main-hand.enchantments")) {
                                        config.getConfigurationSection(section + ".main-hand.enchantments").getKeys(false).forEach(enchantment -> {
                                            if (!config.isInt(section + ".main-hand.enchantments." + enchantment))return;
                                            var enchantLvl = config.getInt(section + ".main-hand.enchantments." + enchantment);
                                            item.addUnsafeEnchantment(getMaterials().getEnchantment(enchantment), enchantLvl);
                                        });
                                    }
                                    equipment.setItemInMainHand(item);
                                }
                                if (config.isDouble(section + ".main-hand.drop-chance")) {
                                    equipment.setItemInMainHandDropChance((float) config.getDouble(section + ".main-hand.drop-chance"));
                                }
                                if (config.isString(section + ".off-hand.item") && config.isInt(section + ".off-hand.amount")) {
                                    var itemName = config.getString(section + ".off-hand.item");
                                    var itemAmount = config.getInt(section + ".off-hand.amount");
                                    var item = getMaterials().getItemStack(itemName, itemAmount);
                                    if (config.isConfigurationSection(section + ".off-hand.enchantments")) {
                                        config.getConfigurationSection(section + ".off-hand.enchantments").getKeys(false).forEach(enchantment -> {
                                            if (!config.isInt(section + ".off-hand.enchantments." + enchantment))return;
                                            var enchantLvl = config.getInt(section + ".off-hand.enchantments." + enchantment);
                                            item.addUnsafeEnchantment(getMaterials().getEnchantment(enchantment), enchantLvl);
                                        });
                                    }
                                    equipment.setItemInOffHand(item);
                                }
                                if (config.isDouble(section + ".off-hand.drop-chance")) {
                                    equipment.setItemInOffHandDropChance((float) config.getDouble(section + ".off-hand.drop-chance"));
                                }
                                if (config.isString(section + ".helmet.item") && config.isInt(section + ".helmet.amount")) {
                                    var itemName = config.getString(section + ".helmet.item");
                                    var itemAmount = config.getInt(section + ".helmet.amount");
                                    var item = getMaterials().getItemStack(itemName, itemAmount);
                                    if (config.isConfigurationSection(section + ".helmet.enchantments")) {
                                        config.getConfigurationSection(section + ".helmet.enchantments").getKeys(false).forEach(enchantment -> {
                                            if (!config.isInt(section + ".helmet.enchantments." + enchantment))return;
                                            var enchantLvl = config.getInt(section + ".helmet.enchantments." + enchantment);
                                            item.addUnsafeEnchantment(getMaterials().getEnchantment(enchantment), enchantLvl);
                                        });
                                    }
                                    equipment.setHelmet(item);
                                }
                                if (config.isDouble(section + ".helmet.drop-chance")) {
                                    equipment.setHelmetDropChance((float) config.getDouble(section + ".helmet.drop-chance"));
                                }
                                if (config.isString(section + ".chestplate.item") && config.isInt(section + ".chestplate.amount")) {
                                    var itemName = config.getString(section + ".chestplate.item");
                                    var itemAmount = config.getInt(section + ".chestplate.amount");
                                    var item = getMaterials().getItemStack(itemName, itemAmount);
                                    if (config.isConfigurationSection(section + ".chestplate.enchantments")) {
                                        config.getConfigurationSection(section + ".chestplate.enchantments").getKeys(false).forEach(enchantment -> {
                                            if (!config.isInt(section + ".chestplate.enchantments." + enchantment))return;
                                            var enchantLvl = config.getInt(section + ".chestplate.enchantments." + enchantment);
                                            item.addUnsafeEnchantment(getMaterials().getEnchantment(enchantment), enchantLvl);
                                        });
                                    }
                                    equipment.setChestplate(item);
                                }
                                if (config.isDouble(section + ".chestplate.drop-chance")) {
                                    equipment.setChestplateDropChance((float) config.getDouble(section + ".chestplate.drop-chance"));
                                }
                                if (config.isString(section + ".leggings.item") && config.isInt(section + ".leggings.amount")) {
                                    var itemName = config.getString(section + ".leggings.item");
                                    var itemAmount = config.getInt(section + ".leggings.amount");
                                    var item = getMaterials().getItemStack(itemName, itemAmount);
                                    if (config.isConfigurationSection(section + ".leggings.enchantments")) {
                                        config.getConfigurationSection(section + ".leggings.enchantments").getKeys(false).forEach(enchantment -> {
                                            if (!config.isInt(section + ".leggings.enchantments." + enchantment))return;
                                            var enchantLvl = config.getInt(section + ".leggings.enchantments." + enchantment);
                                            item.addUnsafeEnchantment(getMaterials().getEnchantment(enchantment), enchantLvl);
                                        });
                                    }
                                    equipment.setLeggings(item);
                                }
                                if (config.isDouble(section + ".leggings.drop-chance")) {
                                    equipment.setLeggingsDropChance((float) config.getDouble(section + ".leggings.drop-chance"));
                                }
                                if (config.isString(section + ".boots.item") && config.isInt(section + ".boots.amount")) {
                                    var itemName = config.getString(section + ".boots.item");
                                    var itemAmount = config.getInt(section + ".boots.amount");
                                    var item = getMaterials().getItemStack(itemName, itemAmount);
                                    if (config.isConfigurationSection(section + ".boots.enchantments")) {
                                        config.getConfigurationSection(section + ".boots.enchantments").getKeys(false).forEach(enchantment -> {
                                            if (!config.isInt(section + ".boots.enchantments." + enchantment))return;
                                            var enchantLvl = config.getInt(section + ".boots.enchantments." + enchantment);
                                            item.addUnsafeEnchantment(getMaterials().getEnchantment(enchantment), enchantLvl);
                                        });
                                    }
                                    equipment.setBoots(item);
                                }
                                if (config.isDouble(section + ".boots.drop-chance")) {
                                    equipment.setBootsDropChance((float) config.getDouble(section + ".boots.drop-chance"));
                                }
                            }
                        }
                    });
                }
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
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }
}