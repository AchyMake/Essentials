package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.io.File;

public record EntityHandler(Entity getEntity) {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Entities getEntities() {
        return getInstance().getEntities();
    }
    public boolean exists() {
        return getEntities().exists(getType());
    }
    public FileConfiguration getConfig() {
        return getEntities().getConfig(getType());
    }
    public boolean isHostile() {
        return getConfig().getBoolean("hostile");
    }
    public boolean isFriendly() {
        return !isHostile();
    }
    public int chunkLimit() {
        return getConfig().getInt("chunk-limit");
    }
    public boolean disableSpawn() {
        return getConfig().getBoolean("disable-spawn");
    }
    public boolean disableBlockForm() {
        return getConfig().getBoolean("disable-block-form");
    }
    public boolean disableBlockDamage() {
        return getConfig().getBoolean("disable-block-damage");
    }
    public boolean disableBlockChange() {
        return getConfig().getBoolean("disable-block-change");
    }
    public boolean disableBlockInteract(Block block) {
        return getConfig().getBoolean("disable-block-interact." + block.getType());
    }
    public boolean disableTarget(Entity entity) {
        return getConfig().getBoolean("disable-target." + entity.getType());
    }
    public boolean disableDamage(Entity entity) {
        return getConfig().getBoolean("disable-damage." + entity.getType());
    }
    public boolean disableSpawnReason(CreatureSpawnEvent.SpawnReason spawnReason) {
        return getConfig().getBoolean("disable-spawn-reason." + spawnReason.toString());
    }
    public String getName() {
        if (getEntity().getType().equals(EntityType.PLAYER)) {
            return getEntity().getName();
        } else if (getEntity().getCustomName() == null) {
            return getInstance().getMessage().toTitleCase(getType().toString());
        } else return getEntity().getCustomName();
    }
    public EntityType getType() {
        return getEntity().getType();
    }
    @Override
    public Entity getEntity() {
        return getEntity;
    }
}