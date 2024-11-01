package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.achymake.essentials.handlers.ProjectileHandler;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
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
    private EntityHandler getEntityHandler(Entity entity) {
        return getInstance().getEntityHandler(entity);
    }
    private ProjectileHandler getProjectileHandler() {
        return getInstance().getProjectileHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public ProjectileHit() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        var entity = event.getEntity();
        if (event.getHitEntity() != null) {
            if (getEntityHandler(entity).disableDamage(event.getHitEntity())) {
                event.setCancelled(true);
            } else getProjectileHandler().cancel(entity);
        } else if (event.getHitBlock() != null) {
            if (entity.getShooter() instanceof Player player) {
                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    getProjectileHandler().remove(entity);
                } else getProjectileHandler().cancel(entity);
            }
        }
    }
}