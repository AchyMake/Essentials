package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.achymake.essentials.handlers.ProjectileHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.PluginManager;

public class ProjectileLaunch implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ProjectileHandler getProjectileHandler() {
        return getInstance().getProjectileHandler();
    }
    private EntityHandler getEntityHandler(Entity entity) {
        return getInstance().getEntityHandler(entity);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public ProjectileLaunch() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        var entity = event.getEntity();
        if (getEntityHandler(entity).disableSpawn()) {
            event.setCancelled(true);
        } else if (entity.getShooter() instanceof Player) {
            getProjectileHandler().addRemovalTask(entity, 10);
        } else getProjectileHandler().addRemovalTask(entity, 3);
    }
}