package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.PluginManager;

public class EntityDeath implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityDeath() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event) {
        var livingEntity = event.getEntity();
        if (getEntityHandler().isNamed(livingEntity)) {
            livingEntity.setCustomName(null);
        }
        if (livingEntity.getKiller() == null)return;
        var value = getEntityHandler().getDroppedEXP(livingEntity);
        if (value > 0) {
            event.setDroppedExp(getEntityHandler().getDroppedEXP(livingEntity));
        }
    }
}