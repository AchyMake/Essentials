package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BellRingEvent;
import org.bukkit.plugin.PluginManager;

public class BellRing implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public BellRing() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBellRing(BellRingEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!getUserdata(player).isDisabled())return;
            event.setCancelled(true);
        }
    }
}