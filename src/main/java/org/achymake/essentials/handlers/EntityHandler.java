package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.entity.*;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EntityHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private RandomHandler getRandomHandler() {
        return getInstance().getRandomHandler();
    }
    public List<CreatureSpawnEvent.SpawnReason> getSpawnReasons() {
        return new ArrayList<>(Arrays.asList(CreatureSpawnEvent.SpawnReason.values()));
    }
    /**
     * get file of entity/entityType.yml
     * @param entity entity
     * @return persistentDataContainer
     * @since many moons ago
     */
    public PersistentDataContainer getData(Entity entity) {
        return entity.getPersistentDataContainer();
    }
    /**
     * get file of entity/entityType.yml
     * @param entityType entityType
     * @return file
     * @since many moons ago
     */
    public File getFile(EntityType entityType) {
        return new File(getInstance().getDataFolder(), "entity/" + entityType.toString() + ".yml");
    }
    /**
     * exists
     * @param entityType entityType
     * @return true if file exists else false
     * @since many moons ago
     */
    public boolean exists(EntityType entityType) {
        return getFile(entityType).exists();
    }
    /**
     * get config
     * @param entityType entityType
     * @return config
     * @since many moons ago
     */
    public FileConfiguration getConfig(EntityType entityType) {
        return YamlConfiguration.loadConfiguration(getFile(entityType));
    }
    /**
     * get entityType
     * @param entityTypeString string
     * @return entityType else null if entityType does not exist
     * @since many moons ago
     */
    public EntityType getType(String entityTypeString) {
        return EntityType.valueOf(entityTypeString.toUpperCase());
    }
    /**
     * is hostile
     * @param entityType entityType
     * @return true if entityType is hostile else false
     * @since many moons ago
     */
    public boolean isHostile(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.hostile");
    }
    /**
     * is friendly this is the opposite of hostile
     * @param entityType entityType
     * @return true if entityType is friendly else false
     * @since many moons ago
     */
    public boolean isFriendly(EntityType entityType) {
        return !isHostile(entityType);
    }
    /**
     * get chunk limit of entityType
     * @param entityType entityType
     * @return integer
     * @since many moons ago
     */
    public int getChunkLimit(EntityType entityType) {
        return getConfig(entityType).getInt("settings.chunk-limit");
    }
    /**
     * get chunk limit of entityType
     * @param entity entity
     * @return true if chunk is over entity limit else false
     * @since many moons ago
     */
    public boolean isOverChunkLimit(Entity entity) {
        var type = entity.getType();
        var chunkLimit = getChunkLimit(type);
        if (chunkLimit > 0) {
            var chunk = entity.getLocation().getChunk();
            var listed = new ArrayList<Entity>();
            for (var entities : chunk.getEntities()) {
                if (entities.getType().equals(type)) {
                    listed.add(entities);
                }
            }
            return listed.size() >= chunkLimit;
        } else return false;
    }
    /**
     * is creature spawn disabled
     * @param entityType entityType
     * @return true if entityType is disabled for spawning else false
     * @since many moons ago
     */
    public boolean isCreatureSpawnDisabled(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-spawn");
    }
    /**
     * is entity block form disabled
     * @param entityType entityType
     * @return true if entityType is disabled for block forming else false
     * @since many moons ago
     */
    public boolean isEntityBlockFormDisabled(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-block-form");
    }
    /**
     * is entity change block disabled
     * @param entityType entityType
     * @return true if entityType is disabled for block changing else false
     * @since many moons ago
     */
    public boolean isEntityChangeBlockDisabled(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-change-block");
    }
    /**
     * is entity damage disabled
     * @param entityType entityType
     * @return true if entityType is disabled for block changing else false
     * @since many moons ago
     */
    public boolean isEntityDamageByEntityDisabled(EntityType entityType, EntityType targetEntityType) {
        return getConfig(entityType).getBoolean("settings.disable-entity-damage." + targetEntityType.toString());
    }
    /**
     * is entity explode disabled
     * @param entityType entityType
     * @return true if entityType is disabled for explode else false
     * @since many moons ago
     */
    public boolean isEntityExplodeDisabled(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-explode");
    }
    /**
     * is entity interact disabled
     * @param entityType entityType
     * @param material material
     * @return true if entityType is disabled for interact else false
     * @since many moons ago
     */
    public boolean isEntityInteractDisabled(EntityType entityType, Material material) {
        return getConfig(entityType).getBoolean("settings.disable-interact." + material.toString());
    }
    /**
     * is entity target disabled
     * @param entityType entityType
     * @param targetEntityType entityType
     * @return true if entityType is disabled for target targetEntityType else false
     * @since many moons ago
     */
    public boolean isEntityTargetDisabled(EntityType entityType, EntityType targetEntityType) {
        return getConfig(entityType).getBoolean("settings.disable-target." + targetEntityType.toString());
    }
    /**
     * is entity hanging break disabled
     * @param removerEntityType entityType
     * @param hangingEntityType entityType
     * @return true if removerEntityType is disabled for breaking hangingEntityType else false
     * @since many moons ago
     */
    public boolean disableHangingBreakByEntity(EntityType removerEntityType, EntityType hangingEntityType) {
        return getConfig(removerEntityType).getBoolean("settings.disable-hanging-break." + hangingEntityType.toString());
    }
    /**
     * is entity spawn reason disabled
     * @param entityType entityType
     * @param spawnReason spawnReason
     * @return true if entityType is disabled for spawn by spawnReason else false
     * @since many moons ago
     */
    public boolean isSpawnReasonDisabled(EntityType entityType, CreatureSpawnEvent.SpawnReason spawnReason) {
        return getConfig(entityType).getBoolean("settings.disable-spawn-reason." + spawnReason.toString());
    }
    /**
     * set integer
     * @param entityType entityType
     * @param path path
     * @param value integer
     * @since many moons ago
     */
    public boolean setInt(EntityType entityType, String path, int value) {
        var file = getFile(entityType);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * set boolean
     * @param entityType entityType
     * @param path path
     * @param value boolean
     * @since many moons ago
     */
    public boolean setBoolean(EntityType entityType, String path, boolean value) {
        var file = getFile(entityType);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * get dropped exp
     * @param entity entity
     * @return integer
     * @since many moons ago
     */
    public int getDroppedEXP(Entity entity) {
        var value = getData(entity).get(getInstance().getKey("exp"), PersistentDataType.INTEGER);
        if (value != null) {
            return value;
        } else return 0;
    }
    /**
     * set dropped exp
     * @param entity entity
     * @param value integer
     * @since many moons ago
     */
    public void setDroppedEXP(Entity entity, int value) {
        getData(entity).set(getInstance().getKey("exp"), PersistentDataType.INTEGER, value);
    }
    private Set<Map.Entry<String, Double>> getLevels(EntityType entityType) {
        var levels = new HashMap<String, Double>();
        var config = getConfig(entityType);
        config.getConfigurationSection("levels").getKeys(false).forEach(level -> {
            levels.put(level, config.getDouble("levels." + level + ".chance"));
        });
        var list = new ArrayList<>(levels.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        var result = new LinkedHashMap<String, Double>();
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (var entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result.entrySet();
    }
    /**
     * set equipment this is the level section from entity/entityType.yml
     * @param entity entity
     * @since many moons ago
     */
    public void setEquipment(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity))return;
        if (!exists(entity.getType()))return;
        var levels = getLevels(livingEntity.getType());
        if (levels.isEmpty())return;
        var equipment = livingEntity.getEquipment();
        if (equipment == null)return;
        var config = getConfig(entity.getType());
        for (var level : levels) {
            if (!getRandomHandler().isTrue(level.getValue()))return;
            var section = "levels." + level.getKey();
            if (config.isDouble(section + ".health")) {
                var health = config.getDouble(section + ".health");
                livingEntity.setMaxHealth(health);
                livingEntity.setHealth(health);
            }
            if (config.isDouble(section + ".scale")) {
                livingEntity.getAttribute(Attribute.SCALE).setBaseValue(config.getDouble(section + ".scale"));
            } else if (config.isDouble(section + ".scale.min") && config.isDouble(section + ".scale.max")) {
                livingEntity.getAttribute(Attribute.SCALE).setBaseValue(getRandomHandler().nextDouble(config.getDouble(section + ".scale.min"), config.getDouble(section + ".scale.max")));
            }
            if (config.isInt(section + ".exp")) {
                setDroppedEXP(livingEntity, config.getInt(section + ".exp"));
            } else if (config.isInt(section + ".exp.min") && config.isInt(section + ".exp.max")) {
                setDroppedEXP(livingEntity, getRandomHandler().nextInt(config.getInt(section + ".exp.min"), config.getInt(section + ".exp.max")));
            }
            if (config.isString(section + ".main-hand.type") && config.isInt(section + ".main-hand.amount")) {
                var itemName = config.getString(section + ".main-hand.type");
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
            if (config.isString(section + ".off-hand.type") && config.isInt(section + ".off-hand.amount")) {
                var itemName = config.getString(section + ".off-hand.type");
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
            if (config.isString(section + ".helmet.type") && config.isInt(section + ".helmet.amount")) {
                var itemName = config.getString(section + ".helmet.type");
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
            if (config.isString(section + ".chestplate.type") && config.isInt(section + ".chestplate.amount")) {
                var itemName = config.getString(section + ".chestplate.type");
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
            if (config.isString(section + ".leggings.type") && config.isInt(section + ".leggings.amount")) {
                var itemName = config.getString(section + ".leggings.type");
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
            if (config.isString(section + ".boots.type") && config.isInt(section + ".boots.amount")) {
                var itemName = config.getString(section + ".boots.type");
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
    public boolean isTamed(Entity entity) {
        if (entity instanceof Tameable tameable) {
            return tameable.isTamed();
        } else return false;
    }
    public OfflinePlayer getOwner(Entity entity) {
        if (entity instanceof Tameable tameable) {
            if (tameable.getOwner() != null) {
                return getInstance().getOfflinePlayer(tameable.getOwner().getUniqueId());
            } else return null;
        } else return null;
    }
    public AttributeInstance getAttribute(Entity entity, Attribute attribute) {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity.getAttribute(attribute);
        } else return null;
    }
    public void setAttribute(Entity entity, Attribute attribute, double value) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.getAttribute(attribute).setBaseValue(value);
        }
    }
    /**
     * reload entity folder
     * @since many moons ago
     */
    public void reload() {
        new ACACIA_BOAT().reload();
        new ACACIA_CHEST_BOAT().reload();
        new ALLAY().reload();
        new AREA_EFFECT_CLOUD().reload();
        new ARMADILLO().reload();
        new ARMOR_STAND().reload();
        new ARROW().reload();
        new AXOLOTL().reload();
        new BAMBOO_CHEST_RAFT().reload();
        new BAMBOO_RAFT().reload();
        new BAT().reload();
        new BEE().reload();
        new BIRCH_BOAT().reload();
        new BIRCH_CHEST_BOAT().reload();
        new BLAZE().reload();
        new BLOCK_DISPLAY().reload();
        new BOGGED().reload();
        new BREEZE().reload();
        new BREEZE_WIND_CHARGE().reload();
        new CAMEL().reload();
        new CAT().reload();
        new CAVE_SPIDER().reload();
        new CHERRY_BOAT().reload();
        new CHERRY_CHEST_BOAT().reload();
        new CHEST_MINECART().reload();
        new CHICKEN().reload();
        new COD().reload();
        new COMMAND_BLOCK_MINECART().reload();
        new COW().reload();
        new CREAKING().reload();
        new CREAKING_TRANSIENT().reload();
        new CREEPER().reload();
        new DARK_OAK_BOAT().reload();
        new DARK_OAK_CHEST_BOAT().reload();
        new DOLPHIN().reload();
        new DONKEY().reload();
        new DRAGON_FIREBALL().reload();
        new DROWNED().reload();
        new EGG().reload();
        new ELDER_GUARDIAN().reload();
        new END_CRYSTAL().reload();
        new ENDER_DRAGON().reload();
        new ENDER_PEARL().reload();
        new ENDERMAN().reload();
        new ENDERMITE().reload();
        new EVOKER().reload();
        new EVOKER_FANGS().reload();
        new EXPERIENCE_BOTTLE().reload();
        new EXPERIENCE_ORB().reload();
        new EYE_OF_ENDER().reload();
        new FALLING_BLOCK().reload();
        new FIREBALL().reload();
        new FIREWORK_ROCKET().reload();
        new FISHING_BOBBER().reload();
        new FOX().reload();
        new FROG().reload();
        new FURNACE_MINECART().reload();
        new GHAST().reload();
        new GIANT().reload();
        new GLOW_ITEM_FRAME().reload();
        new GLOW_SQUID().reload();
        new GOAT().reload();
        new GUARDIAN().reload();
        new HAPPY_GHAST().reload();
        new HOGLIN().reload();
        new HOPPER_MINECART().reload();
        new HORSE().reload();
        new HUSK().reload();
        new ILLUSIONER().reload();
        new INTERACTION().reload();
        new IRON_GOLEM().reload();
        new ITEM().reload();
        new ITEM_DISPLAY().reload();
        new ITEM_FRAME().reload();
        new JUNGLE_BOAT().reload();
        new JUNGLE_CHEST_BOAT().reload();
        new LEASH_KNOT().reload();
        new LIGHTNING_BOLT().reload();
        new LLAMA().reload();
        new LLAMA_SPIT().reload();
        new MAGMA_CUBE().reload();
        new MANGROVE_BOAT().reload();
        new MANGROVE_CHEST_BOAT().reload();
        new MARKER().reload();
        new MINECART().reload();
        new MOOSHROOM().reload();
        new MULE().reload();
        new OAK_BOAT().reload();
        new OAK_CHEST_BOAT().reload();
        new OCELOT().reload();
        new OMINOUS_ITEM_SPAWNER().reload();
        new PAINTING().reload();
        new PALE_OAK_BOAT().reload();
        new PALE_OAK_CHEST_BOAT().reload();
        new PANDA().reload();
        new PARROT().reload();
        new PHANTOM().reload();
        new PIG().reload();
        new PIGLIN().reload();
        new PIGLIN_BRUTE().reload();
        new PILLAGER().reload();
        new POLAR_BEAR().reload();
        new POTION().reload();
        new PUFFERFISH().reload();
        new RABBIT().reload();
        new RAVAGER().reload();
        new SALMON().reload();
        new SHEEP().reload();
        new SHULKER().reload();
        new SHULKER_BULLET().reload();
        new SILVERFISH().reload();
        new SKELETON().reload();
        new SKELETON_HORSE().reload();
        new SLIME().reload();
        new SMALL_FIREBALL().reload();
        new SNIFFER().reload();
        new SNOW_GOLEM().reload();
        new SNOWBALL().reload();
        new SPAWNER_MINECART().reload();
        new SPECTRAL_ARROW().reload();
        new SPIDER().reload();
        new SPRUCE_BOAT().reload();
        new SPRUCE_CHEST_BOAT().reload();
        new SQUID().reload();
        new STRAY().reload();
        new STRIDER().reload();
        new TADPOLE().reload();
        new TEXT_DISPLAY().reload();
        new TNT().reload();
        new TNT_MINECART().reload();
        new TRADER_LLAMA().reload();
        new TRIDENT().reload();
        new TROPICAL_FISH().reload();
        new TURTLE().reload();
        new UNKNOWN().reload();
        new VEX().reload();
        new VILLAGER().reload();
        new VINDICATOR().reload();
        new WANDERING_TRADER().reload();
        new WARDEN().reload();
        new WIND_CHARGE().reload();
        new WITCH().reload();
        new WITHER().reload();
        new WITHER_SKELETON().reload();
        new WITHER_SKULL().reload();
        new WOLF().reload();
        new ZOGLIN().reload();
        new ZOMBIE().reload();
        new ZOMBIE_HORSE().reload();
        new ZOMBIE_VILLAGER().reload();
        new ZOMBIFIED_PIGLIN().reload();
    }
}