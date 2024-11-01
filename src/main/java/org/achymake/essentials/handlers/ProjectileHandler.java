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
                    projectile.remove();
                }
            }
        }, timer * 20L).getTaskId();
        getProjectiles().put(projectile, taskID);
    }
    public void remove(Projectile projectile) {
        if (getProjectiles().containsKey(projectile)) {
            var taskID = getProjectiles().get(projectile);
            if (getScheduler().isQueued(taskID)) {
                getScheduler().cancel(taskID);
            }
            getProjectiles().remove(projectile);
            projectile.remove();
        }
    }
    public void cancel(Projectile projectile) {
        if (getProjectiles().containsKey(projectile)) {
            var taskID = getProjectiles().get(projectile);
            if (getScheduler().isQueued(taskID)) {
                getScheduler().cancel(taskID);
            }
            getProjectiles().remove(projectile);
        }
    }
    public void cancelAll() {
        if (!getProjectiles().isEmpty()) {
            getProjectiles().forEach((projectile, taskID) -> {
                if (getScheduler().isQueued(taskID)) {
                    getScheduler().cancel(taskID);
                }
                getProjectiles().remove(projectile);
            });
        }
    }
    public Map<Projectile, Integer> getProjectiles() {
        return projectiles;
    }
}