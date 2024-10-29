package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.entity.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.io.File;
import java.io.IOException;

public class Entities {
    private Essentials getInstance() {
        return Essentials.getInstance();
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
        return getConfig(entityType).getBoolean("hostile");
    }
    public boolean isFriendly(EntityType entityType) {
        return !isHostile(entityType);
    }
    public int chunkLimit(EntityType entityType) {
        return getConfig(entityType).getInt("chunk-limit");
    }
    public boolean disableSpawn(EntityType entityType) {
        return getConfig(entityType).getBoolean("disable-spawn");
    }
    public boolean disableBlockForm(EntityType entityType) {
        return getConfig(entityType).getBoolean("disable-block-form");
    }
    public boolean disableBlockDamage(EntityType entityType) {
        return getConfig(entityType).getBoolean("disable-block-damage");
    }
    public boolean disableBlockChange(EntityType entityType) {
        return getConfig(entityType).getBoolean("disable-block-change");
    }
    public boolean disableBlockInteract(EntityType entityType, Material blockMaterial) {
        return getConfig(entityType).getBoolean("disable-block-interact." + blockMaterial.toString());
    }
    public boolean disableTarget(EntityType entityType, EntityType targetEntityType) {
        return getConfig(entityType).getBoolean("disable-target." + targetEntityType.toString());
    }
    public boolean disableDamage(EntityType entityType, EntityType targetEntityType) {
        return getConfig(entityType).getBoolean("disable-damage." + targetEntityType.toString());
    }
    public boolean disabledSpawnReason(EntityType entityType, CreatureSpawnEvent.SpawnReason spawnReason) {
        return getConfig(entityType).getBoolean("disabled-spawn-reason." + spawnReason.toString());
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
        new ALLAY().reload();
        new ARMADILLO().reload();
        new ARMOR_STAND().reload();
        new AXOLOTL().reload();
        new BAT().reload();
        new BEE().reload();
        new BLAZE().reload();
        new BOGGED().reload();
        new BREEZE().reload();
        new CAMEL().reload();
        new CAT().reload();
        new CAVE_SPIDER().reload();
        new CHICKEN().reload();
        new COD().reload();
        new COW().reload();
        new CREEPER().reload();
        new DOLPHIN().reload();
        new DONKEY().reload();
        new DROWNED().reload();
        new ELDER_GUARDIAN().reload();
        new ENDER_DRAGON().reload();
        new ENDERMAN().reload();
        new ENDERMITE().reload();
        new EVOKER().reload();
        new FOX().reload();
        new FROG().reload();
        new GHAST().reload();
        new GIANT().reload();
        new GLOW_SQUID().reload();
        new GOAT().reload();
        new GUARDIAN().reload();
        new HOGLIN().reload();
        new HORSE().reload();
        new HUSK().reload();
        new ILLUSIONER().reload();
        new IRON_GOLEM().reload();
        new LLAMA().reload();
        new MAGMA_CUBE().reload();
        new MOOSHROOM().reload();
        new MULE().reload();
        new OCELOT().reload();
        new PANDA().reload();
        new PARROT().reload();
        new PHANTOM().reload();
        new PIG().reload();
        new PIGLIN().reload();
        new PIGLIN_BRUTE().reload();
        new PILLAGER().reload();
        new POLAR_BEAR().reload();
        new PUFFERFISH().reload();
        new RABBIT().reload();
        new RAVAGER().reload();
        new SALMON().reload();
        new SHEEP().reload();
        new SHULKER().reload();
        new SILVERFISH().reload();
        new SKELETON().reload();
        new SKELETON_HORSE().reload();
        new SLIME().reload();
        new SNIFFER().reload();
        new SNOW_GOLEM().reload();
        new SPIDER().reload();
        new SQUID().reload();
        new STRAY().reload();
        new STRIDER().reload();
        new TADPOLE().reload();
        new TRADER_LLAMA().reload();
        new TROPICAL_FISH().reload();
        new TURTLE().reload();
        new VEX().reload();
        new VILLAGER().reload();
        new VINDICATOR().reload();
        new WANDERING_TRADER().reload();
        new WARDEN().reload();
        new WITCH().reload();
        new WITHER().reload();
        new WITHER_SKELETON().reload();
        new WOLF().reload();
        new ZOGLIN().reload();
        new ZOMBIE().reload();
        new ZOMBIE_HORSE().reload();
        new ZOMBIE_VILLAGER().reload();
        new ZOMBIFIED_PIGLIN().reload();
    }
}