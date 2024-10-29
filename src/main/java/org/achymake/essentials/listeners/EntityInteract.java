package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.plugin.PluginManager;

public class EntityInteract implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler(Entity getEntity) {
        return getInstance().getEntityHandler(getEntity);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityInteract() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityInteract(EntityInteractEvent event) {
        var entity = event.getEntity();
        if (entity instanceof Player)return;
        var entityHandler = getEntityHandler(entity);
        if (!entityHandler.exists())return;
        if (!entityHandler.disableBlockInteract(event.getBlock()))return;
        event.setCancelled(true);
    }
}