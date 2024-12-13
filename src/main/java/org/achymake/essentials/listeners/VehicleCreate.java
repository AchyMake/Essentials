package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
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
    private Entities getEntities() {
        return getInstance().getEntities();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public VehicleCreate() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleCreate(VehicleCreateEvent event) {
        if (!getEntities().disableCreatureSpawn(event.getVehicle().getType())) {
            var chunkLimit = getEntities().chunkLimit(event.getVehicle().getType());
            if (chunkLimit > 0) {
                var chunk = event.getVehicle().getLocation().getChunk();
                var listed = new ArrayList<Entity>();
                for (var entities : chunk.getEntities()) {
                    if (entities.getType().equals(event.getVehicle().getType())) {
                        listed.add(event.getVehicle());
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