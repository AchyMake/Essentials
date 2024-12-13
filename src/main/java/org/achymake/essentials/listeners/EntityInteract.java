package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
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
    private Entities getEntities() {
        return getInstance().getEntities();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityInteract() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.getEntity() instanceof Player)return;
        if (!getEntities().disableEntityInteract(event.getEntityType(), event.getBlock().getType()))return;
        event.setCancelled(true);
    }
}