package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.PluginManager;

public class EntityChangeBlock implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityChangeBlock() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!getEntityHandler().disableEntityChangeBlock(event.getEntityType()))return;
        event.setCancelled(true);
    }
}