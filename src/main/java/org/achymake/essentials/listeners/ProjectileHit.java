package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
import org.achymake.essentials.handlers.ProjectileHandler;
import org.bukkit.GameMode;
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
    private Entities getEntities() {
        return getInstance().getEntities();
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
        if (event.getHitEntity() != null) {
            if (!getEntities().disableEntityDamageByEntity(event.getEntityType(), event.getHitEntity().getType())) {
                getProjectileHandler().cancel(event.getEntity());
            } else event.setCancelled(true);
        } else if (event.getHitBlock() != null) {
            if (event.getEntity().getShooter() instanceof Player player) {
                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    getProjectileHandler().remove(event.getEntity());
                } else getProjectileHandler().cancel(event.getEntity());
            }
        }
    }
}