package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

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
    public boolean isHostile() {
        return getEntities().isHostile(getType());
    }
    public boolean isFriendly() {
        return !isHostile();
    }
    public int chunkLimit() {
        return getEntities().chunkLimit(getType());
    }
    public boolean disableSpawn() {
        return getEntities().disableSpawn(getType());
    }
    public boolean disableBlockForm() {
        return getEntities().disableBlockForm(getType());
    }
    public boolean disableBlockDamage() {
        return getEntities().disableBlockDamage(getType());
    }
    public boolean disableBlockChange() {
        return getEntities().disableBlockChange(getType());
    }
    public boolean disableBlockInteract(Block block) {
        return getEntities().disableBlockInteract(getType(), block.getType());
    }
    public boolean disableTarget(Entity entity) {
        return getEntities().disableTarget(getType(), entity.getType());
    }
    public boolean disableDamage(Entity entity) {
        return getEntities().disableDamage(getType(), entity.getType());
    }
    public boolean disableSpawnReason(CreatureSpawnEvent.SpawnReason spawnReason) {
        return getEntities().disabledSpawnReason(getType(), spawnReason);
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