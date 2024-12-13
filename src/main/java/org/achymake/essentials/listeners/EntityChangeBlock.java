package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.PluginManager;

public class EntityChangeBlock implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Entities getEntities() {
        return getInstance().getEntities();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityChangeBlock() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!getEntities().disableEntityChangeBlock(event.getEntityType()))return;
        event.setCancelled(true);
    }
}