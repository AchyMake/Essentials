package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Player;
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
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Player)return;
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM))return;
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.BREEDING))return;
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CURED))return;
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.INFECTION))return;
        if (!getEntityHandler().isSpawnReasonDisabled(event.getEntityType(), event.getSpawnReason())) {
            if (!getEntityHandler().isCreatureSpawnDisabled(event.getEntityType())) {
                if (!getEntityHandler().isOverChunkLimit(event.getEntity())) {
                    getEntityHandler().setEquipment(event.getEntity());
                } else event.setCancelled(true);
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }
}