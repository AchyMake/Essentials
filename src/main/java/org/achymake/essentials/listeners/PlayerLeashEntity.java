package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerLeashEntity implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerLeashEntity() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        if (!getUserdata(event.getPlayer()).isDisabled())return;
        event.setCancelled(true);
    }
}