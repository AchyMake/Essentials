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
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public CreatureSpawn() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Player)return;
        if (getEntityHandler().disabledSpawnReason(event.getEntityType(), event.getSpawnReason())) {
            event.setCancelled(true);
        } else if (getEntityHandler().disableSpawn(event.getEntityType())) {
            event.setCancelled(true);
        } else {
            var chunkLimit = getEntityHandler().chunkLimit(event.getEntityType());
            if (chunkLimit > 0) {
                var chunk = event.getLocation().getChunk();
                var listed = new ArrayList<Entity>();
                for (var entities : chunk.getEntities()) {
                    if (entities.getType().equals(event.getEntityType())) {
                        listed.add(event.getEntity());
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