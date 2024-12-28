package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class EntityPlace implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityPlace() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPlace(EntityPlaceEvent event) {
        var type = event.getEntityType();
        if (!getEntityHandler().disableCreatureSpawn(type)) {
            if (!getUserdata().isDisabled(event.getPlayer())) {
                var chunkLimit = getEntityHandler().chunkLimit(type);
                if (chunkLimit > 0) {
                    var chunk = event.getEntity().getLocation().getChunk();
                    var listed = new ArrayList<Entity>();
                    for (var entities : chunk.getEntities()) {
                        if (entities.getType().equals(type)) {
                            listed.add(event.getEntity());
                        }
                    }
                    if (listed.size() >= chunkLimit) {
                        event.setCancelled(true);
                    }
                    listed.clear();
                }
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }
}
