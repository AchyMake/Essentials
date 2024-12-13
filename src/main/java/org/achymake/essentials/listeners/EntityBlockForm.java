package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Entities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.plugin.PluginManager;

public class EntityBlockForm implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Entities getEntities() {
        return getInstance().getEntities();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityBlockForm() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (!getEntities().disableEntityBlockForm(event.getEntity().getType()))return;
        event.setCancelled(true);
    }
}