package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.bukkit.plugin.PluginManager;

public class EntityEnterLoveMode implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityEnterLoveMode() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {
        if (event.getHumanEntity() instanceof Player player) {
            if (!getEntityHandler().isOverChunkLimit(event.getEntity()))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, getMessage().get("events.breed", getMessage().toTitleCase(event.getEntityType().toString()), String.valueOf(getEntityHandler().getChunkLimit(event.getEntityType()))));
        }
    }
}