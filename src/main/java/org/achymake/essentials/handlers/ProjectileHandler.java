package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.entity.Projectile;

import java.util.HashMap;
import java.util.Map;

public class ProjectileHandler {
    private final Map<Projectile, Integer> projectiles = new HashMap<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    public void addRemovalTask(Projectile projectile, long timer) {
        int taskID = getScheduler().runLater(new Runnable() {
            @Override
            public void run() {
                if (projectile != null) {
                    remove(projectile);
                }
            }
        }, timer * 20L).getTaskId();
        getProjectiles().put(projectile, taskID);
    }
    public void remove(Projectile projectile) {
        if (projectiles.containsKey(projectile)) {
            var taskID = projectiles.get(projectile);
            if (getScheduler().isQueued(taskID)) {
                getScheduler().cancel(taskID);
            }
            projectiles.remove(projectile);
            projectile.remove();
        }
    }
    public void cancel(Projectile projectile) {
        if (projectiles.containsKey(projectile)) {
            var taskID = projectiles.get(projectile);
            if (getScheduler().isQueued(taskID)) {
                getScheduler().cancel(taskID);
            }
            projectiles.remove(projectile);
        }
    }
    public void disable() {
        if (!projectiles.isEmpty()) {
            projectiles.forEach((projectile, taskID) -> {
                if (projectile != null) {
                    projectiles.remove(projectile);
                }
                if (getScheduler().isQueued(taskID)) {
                    getScheduler().cancel(taskID);
                }
            });
        }
    }
    public Map<Projectile, Integer> getProjectiles() {
        return projectiles;
    }
}