package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.achymake.essentials.handlers.GameModeHandler;
import org.achymake.essentials.handlers.ProjectileHandler;
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
    private GameModeHandler getGameModeHandler() {
        return getInstance().getGameModeHandler();
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
        var entityType = event.getEntityType();
        var entity = event.getEntity();
        var entityHit = event.getHitEntity();
        if (entityHit != null) {
            if (!getEntityHandler().isEntityDamageByEntityDisabled(entityType, entityHit.getType())) {
                getProjectileHandler().cancel(entity);
            } else event.setCancelled(true);
        } else if (event.getHitBlock() != null) {
            if (entity.getShooter() instanceof Player player) {
                if (player.getGameMode().equals(getGameModeHandler().get("creative"))) {
                    if (!getConfig().getBoolean("projectile.creative.instant-remove." + entityType.toString().toLowerCase()))return;
                    getProjectileHandler().remove(entity);
                } else getProjectileHandler().cancel(entity);
            }
        }
    }
}