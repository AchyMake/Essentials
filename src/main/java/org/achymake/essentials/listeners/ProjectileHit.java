package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.achymake.essentials.handlers.ProjectileHandler;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.PluginManager;

public class ProjectileHit implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private ProjectileHandler getProjectileHandler() {
        return getInstance().getProjectileHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public ProjectileHit() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitEntity() != null) {
            if (!getEntityHandler().isEntityDamageByEntityDisabled(event.getEntityType(), event.getHitEntity().getType())) {
                getProjectileHandler().cancel(event.getEntity());
            } else event.setCancelled(true);
        } else if (event.getHitBlock() != null) {
            if (event.getEntity().getShooter() instanceof Player player) {
                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    if (!getConfig().getBoolean("projectile.creative.instant-remove"))return;
                    getProjectileHandler().remove(event.getEntity());
                } else getProjectileHandler().cancel(event.getEntity());
            }
        }
    }
}