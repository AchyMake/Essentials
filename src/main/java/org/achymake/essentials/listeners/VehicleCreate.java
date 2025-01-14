package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class VehicleCreate implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public VehicleCreate() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleCreate(VehicleCreateEvent event) {
        var vehicle = event.getVehicle();
        var type = vehicle.getType();
        if (!getEntityHandler().isCreatureSpawnDisabled(type)) {
            var chunkLimit = getEntityHandler().getChunkLimit(type);
            if (chunkLimit > 0) {
                var chunk = vehicle.getLocation().getChunk();
                var listed = new ArrayList<Entity>();
                for (var entities : chunk.getEntities()) {
                    if (entities.getType().equals(type)) {
                        listed.add(entities);
                    }
                }
                if (listed.size() >= chunkLimit) {
                    event.setCancelled(true);
                }
                listed.clear();
            }
        } else event.setCancelled(true);
    }
}