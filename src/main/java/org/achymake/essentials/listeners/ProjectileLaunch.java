package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.achymake.essentials.handlers.ProjectileHandler;
import org.bukkit.configuration.file.FileConfiguration;
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
    public ProjectileLaunch() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        var projectile = event.getEntity();
        if (!getEntityHandler().isCreatureSpawnDisabled(event.getEntityType())) {
            if (projectile.getShooter() instanceof Player player) {
                if (getConfig().getBoolean("projectile.removal-timer.enable")) {
                    if (!getConfig().isInt("projectile.removal-timer.player"))return;
                    var timer = getConfig().getInt("projectile.removal-timer.player");
                    if (timer > 0) {
                        getProjectileHandler().addRemovalTask(event.getEntity(), timer);
                    }
                }
                if (getConfig().getBoolean("projectile.cooldown.enable")) {
                    var heldItem = player.getInventory().getItemInMainHand();
                    var materialString = heldItem.getType().toString().toLowerCase();
                    if (!getConfig().isInt("projectile.cooldown." + materialString))return;
                    var cooldown = getConfig().getInt("projectile.cooldown." + materialString);
                    if (cooldown > 0) {
                        player.setCooldown(heldItem.getType(), cooldown);
                    }
                }
            } else if (projectile.getShooter() instanceof Entity entity) {
                var entityType = entity.getType().toString().toLowerCase();
                if (!getConfig().getBoolean("projectile.removal-timer.enable"))return;
                if (!getConfig().isInt("projectile.removal-timer." + entityType))return;
                var timer = getConfig().getInt("projectile.removal-timer." + entityType);
                if (timer > 0) {
                    getProjectileHandler().addRemovalTask(event.getEntity(), timer);
                }
            }
        } else event.setCancelled(true);
    }
}