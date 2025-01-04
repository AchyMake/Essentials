package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
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
        var entity = event.getEntity();
        if (entity instanceof Player)return;
        if (!getEntityHandler().isSpawnReasonDisabled(event.getEntityType(), event.getSpawnReason())) {
            if (!getEntityHandler().isCreatureSpawnDisabled(event.getEntityType())) {
                var chunkLimit = getEntityHandler().getChunkLimit(event.getEntityType());
                if (chunkLimit > 0) {
                    var chunk = event.getLocation().getChunk();
                    var listed = new ArrayList<Entity>();
                    for (var entities : chunk.getEntities()) {
                        if (entities.getType().equals(event.getEntityType())) {
                            listed.add(entities);
                        }
                    }
                    if (chunkLimit > listed.size()) {
                        getEntityHandler().setEquipment(entity);
                    } else event.setCancelled(true);
                    listed.clear();
                } else getEntityHandler().setEquipment(entity);
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }
}