package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.plugin.PluginManager;

public class HangingBreak implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public HangingBreak() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onHangingBreak(HangingBreakEvent event) {
        if (event.getCause().equals(HangingBreakEvent.RemoveCause.EXPLOSION)) {
            if (getEntityHandler().disableDamage(getInstance().getEntityHandler().getType("creeper"), event.getEntity().getType())) {
                event.setCancelled(true);
            } else if (getEntityHandler().disableDamage(getInstance().getEntityHandler().getType("tnt"), event.getEntity().getType())) {
                event.setCancelled(true);
            } else if (getEntityHandler().disableDamage(getInstance().getEntityHandler().getType("tnt_minecart"), event.getEntity().getType())) {
                event.setCancelled(true);
            }
        }
    }
}