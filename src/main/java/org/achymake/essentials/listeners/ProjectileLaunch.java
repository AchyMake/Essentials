package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
import org.achymake.essentials.handlers.ProjectileHandler;
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
    private Entities getEntities() {
        return getInstance().getEntities();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public ProjectileLaunch() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (getEntities().disableSpawn(event.getEntityType())) {
            event.setCancelled(true);
        } else if (event.getEntity().getShooter() instanceof Player) {
            getProjectileHandler().addRemovalTask(event.getEntity(), 10);
        } else getProjectileHandler().addRemovalTask(event.getEntity(), 3);
    }
}