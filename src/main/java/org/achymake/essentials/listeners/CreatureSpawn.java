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
    private EntityHandler getEntityHandler(Entity getEntity) {
        return getInstance().getEntityHandler(getEntity);
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
        var chunk = event.getLocation().getChunk();
        var entityHandler = getEntityHandler(entity);
        if (!entityHandler.exists())return;
        if (entityHandler.disableSpawnReason(event.getSpawnReason())) {
            event.setCancelled(true);
        } else {
            var chunkLimit = entityHandler.chunkLimit();
            if (entityHandler.disableSpawn()) {
                event.setCancelled(true);
            } else if (chunkLimit > 0) {
                var listed = new ArrayList<Entity>();
                for (var chunkEntities : chunk.getEntities()) {
                    if (chunkEntities.getType().equals(event.getEntityType())) {
                        listed.add(entity);
                    }
                }
                if (listed.size() >= chunkLimit) {
                    event.setCancelled(true);
                }
                listed.clear();
            }
        }
    }
}