package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.data.Entities;
import org.bukkit.OfflinePlayer;
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
    private Entities getEntities() {
        return getInstance().getEntities();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
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
                if (!getUserdata(player).isDisabled())return;
                event.setCancelled(true);
            }
        } else event.setCancelled(true);
    }
}