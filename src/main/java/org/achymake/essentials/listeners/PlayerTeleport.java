package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerTeleport implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerTeleport() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        var userdata = getUserdata(event.getPlayer());
        if (!userdata.isDisabled()) {
            if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND))return;
            if (userdata.getLocation("death") != null && userdata.getLocation("death") == event.getTo()) {
                userdata.setString("locations.death", null);
            }
            userdata.setLocation(event.getFrom(), "recent");
        } else event.setCancelled(true);
    }
}