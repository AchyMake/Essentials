package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class ScheduleHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private BukkitScheduler getScheduler() {
        return getInstance().getBukkitScheduler();
    }
    public BukkitTask runLater(Runnable runnable, long timer) {
        return getScheduler().runTaskLater(getInstance(), runnable, timer);
    }
    public BukkitTask runAsynchronously(Runnable runnable) {
        return getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }
    public boolean isQueued(int taskID) {
        return getScheduler().isQueued(taskID);
    }
    public void cancel(int taskID) {
        if (isQueued(taskID)) {
            getScheduler().cancelTask(taskID);
        }
    }
    public void cancelAll() {
        getScheduler().cancelTasks(getInstance());
    }
}