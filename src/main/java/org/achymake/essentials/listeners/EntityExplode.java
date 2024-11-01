package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.PluginManager;

public class EntityExplode implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler(Entity getEntity) {
        return getInstance().getEntityHandler(getEntity);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityExplode() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Player)return;
        if (!getEntityHandler(event.getEntity()).disableBlockDamage())return;
        event.setCancelled(true);
    }
}