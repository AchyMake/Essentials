package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.plugin.PluginManager;

public class HangingBreakByEntity implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntities() {
        return getInstance().getEntityHandler();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public HangingBreakByEntity() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if (!getEntities().disableHangingBreakByEntity(event.getRemover().getType(), event.getEntity().getType())) {
            if (event.getRemover() instanceof Player player) {
                if (!getUserdata().isDisabled(player))return;
                event.setCancelled(true);
            }
        } else event.setCancelled(true);
    }
}