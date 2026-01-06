package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.PluginManager;

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
        if (!getEntityHandler().isCreatureSpawnDisabled(vehicle.getType())) {
            if (!getEntityHandler().isOverChunkLimit(vehicle))return;
            event.setCancelled(true);
        } else event.setCancelled(true);
    }
}