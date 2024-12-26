package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.PluginManager;

public class EntityTarget implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityTarget() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() == null)return;
        if (event.getEntity() instanceof Player)return;
        if (!getEntityHandler().disableEntityTarget(event.getEntityType(), event.getTarget().getType())) {
            if (event.getTarget() instanceof Player player) {
                if (!getUserdata().isVanished(player))return;
                event.setCancelled(true);
            }
        } else event.setCancelled(true);
    }
}