package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.plugin.PluginManager;

public class EntityBlockForm implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityBlockForm() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (!getEntityHandler().disableEntityBlockForm(event.getEntity().getType()))return;
        event.setCancelled(true);
    }
}