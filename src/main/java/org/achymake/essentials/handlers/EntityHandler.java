package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.entity.*;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
     * get equipment
     * @param entity entity
     * @return entityEquipment
     * @since many moons ago
     */
    public EntityEquipment getEquipment(Entity entity) {
        switch (entity) {
            case Allay e -> {
                return e.getEquipment();
            }
            case Armadillo e -> {
                return e.getEquipment();
            }
            case ArmorStand e -> {
                return e.getEquipment();
            }
            case Axolotl e -> {
                return e.getEquipment();
            }
            case Bat e -> {
                return e.getEquipment();
            }
            case Bee e -> {
                return e.getEquipment();
            }
            case Blaze e -> {
                return e.getEquipment();
            }
            case Bogged e -> {
                return e.getEquipment();
            }
            case Breeze e -> {
                return e.getEquipment();
            }
            case Camel e -> {
                return e.getEquipment();
            }
            case Cat e -> {
                return e.getEquipment();
            }
            case CaveSpider e -> {
                return e.getEquipment();
            }
            case Chicken e -> {
                return e.getEquipment();
            }
            case Cod e -> {
                return e.getEquipment();
            }
            case MushroomCow e -> {
                return e.getEquipment();
            }
            case Cow e -> {
                return e.getEquipment();
            }
            case Creaking e -> {
                return e.getEquipment();
            }
            case Creeper e -> {
                return e.getEquipment();
            }
            case Dolphin e -> {
                return e.getEquipment();
            }
            case Donkey e -> {
                return e.getEquipment();
            }
            case Drowned e -> {
                return e.getEquipment();
            }
            case ElderGuardian e -> {
                return e.getEquipment();
            }
            case EnderDragon e -> {
                return e.getEquipment();
            }
            case Enderman e -> {
                return e.getEquipment();
            }
            case Endermite e -> {
                return e.getEquipment();
            }
            case Evoker e -> {
                return e.getEquipment();
            }
            case Fox e -> {
                return e.getEquipment();
            }
            case Frog e -> {
                return e.getEquipment();
            }
            case Ghast e -> {
                return e.getEquipment();
            }
            case Giant e -> {
                return e.getEquipment();
            }
            case GlowSquid e -> {
                return e.getEquipment();
            }
            case Goat e -> {
                return e.getEquipment();
            }
            case Guardian e -> {
                return e.getEquipment();
            }
            case Hoglin e -> {
                return e.getEquipment();
            }
            case Horse e -> {
                return e.getEquipment();
            }
            case Husk e -> {
                return e.getEquipment();
            }
            case Illusioner e -> {
                return e.getEquipment();
            }
            case IronGolem e -> {
                return e.getEquipment();
            }
            case Llama e -> {
                return e.getEquipment();
            }
            case MagmaCube e -> {
                return e.getEquipment();
            }
            case Mule e -> {
                return e.getEquipment();
            }
            case Ocelot e -> {
                return e.getEquipment();
            }
            case Panda e -> {
                return e.getEquipment();
            }
            case Parrot e -> {
                return e.getEquipment();
            }
            case Phantom e -> {
                return e.getEquipment();
            }
            case Pig e -> {
                return e.getEquipment();
            }
            case Piglin e -> {
                return e.getEquipment();
            }
            case PiglinBrute e -> {
                return e.getEquipment();
            }
            case Pillager e -> {
                return e.getEquipment();
            }
            case PolarBear e -> {
                return e.getEquipment();
            }
            case PufferFish e -> {
                return e.getEquipment();
            }
            case Rabbit e -> {
                return e.getEquipment();
            }
            case Ravager e -> {
                return e.getEquipment();
            }
            case Salmon e -> {
                return e.getEquipment();
            }
            case Sheep e -> {
                return e.getEquipment();
            }
            case Shulker e -> {
                return e.getEquipment();
            }
            case Silverfish e -> {
                return e.getEquipment();
            }
            case Skeleton e -> {
                return e.getEquipment();
            }
            case SkeletonHorse e -> {
                return e.getEquipment();
            }
            case Slime e -> {
                return e.getEquipment();
            }
            case Sniffer e -> {
                return e.getEquipment();
            }
            case Snowman e -> {
                return e.getEquipment();
            }
            case Spider e -> {
                return e.getEquipment();
            }
            case Squid e -> {
                return e.getEquipment();
            }
            case Stray e -> {
                return e.getEquipment();
            }
            case Strider e -> {
                return e.getEquipment();
            }
            case Tadpole e -> {
                return e.getEquipment();
            }
            case TropicalFish e -> {
                return e.getEquipment();
            }
            case Turtle e -> {
                return e.getEquipment();
            }
            case Vex e -> {
                return e.getEquipment();
            }
            case ZombieVillager e -> {
                return e.getEquipment();
            }
            case PigZombie e -> {
                return e.getEquipment();
            }
            case Villager e -> {
                return e.getEquipment();
            }
            case Vindicator e -> {
                return e.getEquipment();
            }
            case WanderingTrader e -> {
                return e.getEquipment();
            }
            case Warden e -> {
                return e.getEquipment();
            }
            case Witch e -> {
                return e.getEquipment();
            }
            case Wither e -> {
                return e.getEquipment();
            }
            case WitherSkeleton e -> {
                return e.getEquipment();
            }
            case Wolf e -> {
                return e.getEquipment();
            }
            case Zoglin e -> {
                return e.getEquipment();
            }
            case Zombie e -> {
                return e.getEquipment();
            }
            case ZombieHorse e -> {
                return e.getEquipment();
            }
            default -> {
                return null;
            }
        }
    }
    /**
     * set equipment this is the level section from entity/entityType.yml
     * @param entity entity
     * @since many moons ago
     */
    public void setEquipment(Entity entity) {
        var equipment = getEquipment(entity);
        if (equipment != null) {
            var config = getConfig(entity.getType());
            if (config.isConfigurationSection("levels")) {
                config.getConfigurationSection("levels").getKeys(false).forEach(level -> {
                    var section = "levels." + level;
                    if (getRandomHandler().isTrue(config.getDouble(section + ".chance"))) {
                        if (config.isDouble(section + ".health")) {
                            var health = config.getDouble(section + ".health");
                            setHealth(entity, health);
                        }
                        if (config.isDouble(section + ".scale")) {
                            var scale = getAttribute(entity, Attribute.SCALE);
                            if (scale != null) {
                                scale.setBaseValue(config.getDouble(section + ".scale"));
                            }
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
                });
            }
        }
    }
    /**
     * set health
     * @param target entity
     * @param value double
     * @since many moons ago
     */
    public void setHealth(Entity target, double value) {
        switch (target) {
            case Allay entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Armadillo entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case ArmorStand entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Axolotl entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Bat entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Bee entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Blaze entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Bogged entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Breeze entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Camel entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Cat entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case CaveSpider entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Chicken entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Cod entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case MushroomCow entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Cow entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Creaking entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Creeper entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Dolphin entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Donkey entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Drowned entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case ElderGuardian entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case EnderDragon entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Enderman entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Endermite entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Evoker entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Fox entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Frog entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Ghast entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Giant entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case GlowSquid entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Goat entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Guardian entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Hoglin entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Horse entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Husk entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Illusioner entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case IronGolem entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case TraderLlama entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Llama entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case MagmaCube entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Mule entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Ocelot entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Panda entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Parrot entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Phantom entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Pig entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Piglin entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case PiglinBrute entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Pillager entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case PolarBear entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case PufferFish entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Rabbit entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Ravager entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Salmon entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Sheep entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Shulker entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Silverfish entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Skeleton entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case SkeletonHorse entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Slime entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Sniffer entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Snowman entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Spider entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Squid entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Stray entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Strider entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Tadpole entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case TropicalFish entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Turtle entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Vex entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case ZombieVillager entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case PigZombie entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Villager entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Vindicator entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case WanderingTrader entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Warden entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Witch entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Wither entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case WitherSkeleton entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Wolf entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Zoglin entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case Zombie entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case ZombieHorse entity -> {
                entity.setMaxHealth(value);
                entity.setHealth(value);
            }
            case null, default -> {}
        }
    }
    /**
     * get attribute
     * @param target entity
     * @param attribute attribute
     * @return attributeInstance
     * @since many moons ago
     */
    public AttributeInstance getAttribute(Entity target, Attribute attribute) {
        switch (target) {
            case Allay entity -> {
                return entity.getAttribute(attribute);
            }
            case Armadillo entity -> {
                return entity.getAttribute(attribute);
            }
            case ArmorStand entity -> {
                return entity.getAttribute(attribute);
            }
            case Axolotl entity -> {
                return entity.getAttribute(attribute);
            }
            case Bat entity -> {
                return entity.getAttribute(attribute);
            }
            case Bee entity -> {
                return entity.getAttribute(attribute);
            }
            case Blaze entity -> {
                return entity.getAttribute(attribute);
            }
            case Bogged entity -> {
                return entity.getAttribute(attribute);
            }
            case Breeze entity -> {
                return entity.getAttribute(attribute);
            }
            case Camel entity -> {
                return entity.getAttribute(attribute);
            }
            case Cat entity -> {
                return entity.getAttribute(attribute);
            }
            case CaveSpider entity -> {
                return entity.getAttribute(attribute);
            }
            case Chicken entity -> {
                return entity.getAttribute(attribute);
            }
            case Cod entity -> {
                return entity.getAttribute(attribute);
            }
            case MushroomCow entity -> {
                return entity.getAttribute(attribute);
            }
            case Cow entity -> {
                return entity.getAttribute(attribute);
            }
            case Creaking entity -> {
                return entity.getAttribute(attribute);
            }
            case Creeper entity -> {
                return entity.getAttribute(attribute);
            }
            case Dolphin entity -> {
                return entity.getAttribute(attribute);
            }
            case Donkey entity -> {
                return entity.getAttribute(attribute);
            }
            case Drowned entity -> {
                return entity.getAttribute(attribute);
            }
            case ElderGuardian entity -> {
                return entity.getAttribute(attribute);
            }
            case EnderDragon entity -> {
                return entity.getAttribute(attribute);
            }
            case Enderman entity -> {
                return entity.getAttribute(attribute);
            }
            case Endermite entity -> {
                return entity.getAttribute(attribute);
            }
            case Evoker entity -> {
                return entity.getAttribute(attribute);
            }
            case Fox entity -> {
                return entity.getAttribute(attribute);
            }
            case Frog entity -> {
                return entity.getAttribute(attribute);
            }
            case Ghast entity -> {
                return entity.getAttribute(attribute);
            }
            case Giant entity -> {
                return entity.getAttribute(attribute);
            }
            case GlowSquid entity -> {
                return entity.getAttribute(attribute);
            }
            case Goat entity -> {
                return entity.getAttribute(attribute);
            }
            case Guardian entity -> {
                return entity.getAttribute(attribute);
            }
            case Hoglin entity -> {
                return entity.getAttribute(attribute);
            }
            case Horse entity -> {
                return entity.getAttribute(attribute);
            }
            case Husk entity -> {
                return entity.getAttribute(attribute);
            }
            case Illusioner entity -> {
                return entity.getAttribute(attribute);
            }
            case IronGolem entity -> {
                return entity.getAttribute(attribute);
            }
            case TraderLlama entity -> {
                return entity.getAttribute(attribute);
            }
            case Llama entity -> {
                return entity.getAttribute(attribute);
            }
            case MagmaCube entity -> {
                return entity.getAttribute(attribute);
            }
            case Mule entity -> {
                return entity.getAttribute(attribute);
            }
            case Ocelot entity -> {
                return entity.getAttribute(attribute);
            }
            case Panda entity -> {
                return entity.getAttribute(attribute);
            }
            case Parrot entity -> {
                return entity.getAttribute(attribute);
            }
            case Phantom entity -> {
                return entity.getAttribute(attribute);
            }
            case Pig entity -> {
                return entity.getAttribute(attribute);
            }
            case Piglin entity -> {
                return entity.getAttribute(attribute);
            }
            case PiglinBrute entity -> {
                return entity.getAttribute(attribute);
            }
            case Pillager entity -> {
                return entity.getAttribute(attribute);
            }
            case PolarBear entity -> {
                return entity.getAttribute(attribute);
            }
            case PufferFish entity -> {
                return entity.getAttribute(attribute);
            }
            case Rabbit entity -> {
                return entity.getAttribute(attribute);
            }
            case Ravager entity -> {
                return entity.getAttribute(attribute);
            }
            case Salmon entity -> {
                return entity.getAttribute(attribute);
            }
            case Sheep entity -> {
                return entity.getAttribute(attribute);
            }
            case Shulker entity -> {
                return entity.getAttribute(attribute);
            }
            case Silverfish entity -> {
                return entity.getAttribute(attribute);
            }
            case Skeleton entity -> {
                return entity.getAttribute(attribute);
            }
            case SkeletonHorse entity -> {
                return entity.getAttribute(attribute);
            }
            case Slime entity -> {
                return entity.getAttribute(attribute);
            }
            case Sniffer entity -> {
                return entity.getAttribute(attribute);
            }
            case Snowman entity -> {
                return entity.getAttribute(attribute);
            }
            case Spider entity -> {
                return entity.getAttribute(attribute);
            }
            case Squid entity -> {
                return entity.getAttribute(attribute);
            }
            case Stray entity -> {
                return entity.getAttribute(attribute);
            }
            case Strider entity -> {
                return entity.getAttribute(attribute);
            }
            case Tadpole entity -> {
                return entity.getAttribute(attribute);
            }
            case TropicalFish entity -> {
                return entity.getAttribute(attribute);
            }
            case Turtle entity -> {
                return entity.getAttribute(attribute);
            }
            case Vex entity -> {
                return entity.getAttribute(attribute);
            }
            case ZombieVillager entity -> {
                return entity.getAttribute(attribute);
            }
            case PigZombie entity -> {
                return entity.getAttribute(attribute);
            }
            case Villager entity -> {
                return entity.getAttribute(attribute);
            }
            case Vindicator entity -> {
                return entity.getAttribute(attribute);
            }
            case WanderingTrader entity -> {
                return entity.getAttribute(attribute);
            }
            case Warden entity -> {
                return entity.getAttribute(attribute);
            }
            case Witch entity -> {
                return entity.getAttribute(attribute);
            }
            case Wither entity -> {
                return entity.getAttribute(attribute);
            }
            case WitherSkeleton entity -> {
                return entity.getAttribute(attribute);
            }
            case Wolf entity -> {
                return entity.getAttribute(attribute);
            }
            case Zoglin entity -> {
                return entity.getAttribute(attribute);
            }
            case Zombie entity -> {
                return entity.getAttribute(attribute);
            }
            case ZombieHorse entity -> {
                return entity.getAttribute(attribute);
            }
            case null, default -> {
                return null;
            }
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