package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.entity.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;

import java.io.File;
import java.io.IOException;

public class Entities {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    public EntityType getType(String entityTypeString) {
        return EntityType.valueOf(entityTypeString.toUpperCase());
    }
    public File getFile(EntityType entityType) {
        return new File(getInstance().getDataFolder(), "entity/" + entityType.toString() + ".yml");
    }
    public boolean exists(EntityType entityType) {
        return getFile(entityType).exists();
    }
    public FileConfiguration getConfig(EntityType entityType) {
        return YamlConfiguration.loadConfiguration(getFile(entityType));
    }
    public boolean isHostile(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.hostile");
    }
    public boolean isFriendly(EntityType entityType) {
        return !isHostile(entityType);
    }
    public int chunkLimit(EntityType entityType) {
        return getConfig(entityType).getInt("settings.chunk-limit");
    }
    public boolean disableCreatureSpawn(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-spawn");
    }
    public boolean disableEntityBlockForm(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-block-form");
    }
    public boolean disableEntityChangeBlock(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-change-block");
    }
    public boolean disableEntityDamageByEntity(EntityType entityType, EntityType targetEntityType) {
        return getConfig(entityType).getBoolean("settings.disable-entity-damage." + targetEntityType.toString());
    }
    public boolean disableEntityExplode(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-explode");
    }
    public boolean disableEntityInteract(EntityType entityType, Material blockMaterial) {
        return getConfig(entityType).getBoolean("settings.disable-interact." + blockMaterial.toString());
    }
    public boolean disableEntityTarget(EntityType entityType, EntityType targetEntityType) {
        return getConfig(entityType).getBoolean("settings.disable-target." + targetEntityType.toString());
    }
    public boolean disableHangingBreakByEntity(EntityType entityType, EntityType targetEntityType) {
        return getConfig(entityType).getBoolean("settings.disable-hanging-break." + targetEntityType.toString());
    }
    public boolean disableSpawnReason(EntityType entityType, CreatureSpawnEvent.SpawnReason spawnReason) {
        return getConfig(entityType).getBoolean("settings.disable-spawn-reason." + spawnReason.toString());
    }
    public void setInt(EntityType entityType, String path, int value) {
        var file = getFile(entityType);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setBoolean(EntityType entityType, String path, boolean value) {
        var file = getFile(entityType);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
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
}