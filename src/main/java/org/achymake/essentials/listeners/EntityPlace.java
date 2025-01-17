package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.plugin.PluginManager;

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
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityPlace() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPlace(EntityPlaceEvent event) {
        if (!getEntityHandler().isCreatureSpawnDisabled(event.getEntityType())) {
            if (!getEntityHandler().isOverChunkLimit(event.getEntity())) {
                if (event.getPlayer() == null)return;
                if (!getUserdata().isDisabled(event.getPlayer()))return;
                event.setCancelled(true);
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }
}