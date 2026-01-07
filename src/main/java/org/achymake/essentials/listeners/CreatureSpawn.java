package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.PluginManager;

public class CreatureSpawn implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public CreatureSpawn() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!getEntityHandler().isSpawnReasonDisabled(event.getEntityType(), event.getSpawnReason())) {
            if (!getEntityHandler().isCreatureSpawnDisabled(event.getEntityType())) {
                var entity = event.getEntity();
                if (!getEntityHandler().isOverChunkLimit(entity)) {
                    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM))return;
                    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BREEDING))return;
                    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER_EGG))return;
                    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CURED))return;
                    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.INFECTION))return;
                    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.DROWNED))return;
                    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.FROZEN))return;
                    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SHEARED))return;
                    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.PIGLIN_ZOMBIFIED))return;
                    getEntityHandler().setEquipment(entity);
                } else event.setCancelled(true);
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }
}