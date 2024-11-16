package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Portals;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.data.Warps;
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
    private Portals getPortals() {
        return getInstance().getPortals();
    }
    private Warps getWarps() {
        return getInstance().getWarps();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerTeleport() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled())return;
        var userdata = getUserdata(event.getPlayer());
        if (userdata.isDisabled()) {
            event.setCancelled(true);
        } else if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
            if (getPortals().isWithin(event.getFrom())) {
                var warp = getWarps().getLocation(getPortals().getWarp(event.getFrom()));
                if (warp != null) {
                    event.setTo(warp);
                }
            }
        } else if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
            if (getPortals().isWithin(event.getFrom())) {
                var warp = getWarps().getLocation(getPortals().getWarp(event.getFrom()));
                if (warp != null) {
                    event.setTo(warp);
                }
            }
        } else if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_GATEWAY)) {
            if (getPortals().isWithin(event.getFrom())) {
                var warp = getWarps().getLocation(getPortals().getWarp(event.getFrom()));
                if (warp != null) {
                    event.setTo(warp);
                }
            }
        } else if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND) || event.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN)) {
            if (userdata.getLocation("death") != null && userdata.getLocation("death") == event.getTo()) {
                userdata.setString("locations.death", null);
            }
            userdata.setLocation(event.getFrom(), "recent");
        }
    }
}