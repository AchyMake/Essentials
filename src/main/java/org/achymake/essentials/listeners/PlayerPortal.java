package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Portals;
import org.achymake.essentials.data.Warps;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerPortal implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Portals getPortals() {
        return getInstance().getPortals();
    }
    private Warps getWarps() {
        return getInstance().getWarps();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerPortal() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPortal(PlayerPortalEvent event) {
        var player = event.getPlayer();
        if (!getPortals().isWithin(event.getFrom()))return;
        var warpName = getPortals().getWarp(event.getFrom());
        if (getPortals().getPortalType(event.getFrom()).equalsIgnoreCase("nether_portal")) {
            if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL))return;
            event.setCancelled(true);
            event.setCanCreatePortal(false);
            var warp = getWarps().getLocation(warpName);
            if (warp != null) {
                getMessage().sendActionBar(player, getMessage().get("events.teleport.success", warpName));
                player.teleport(warp);
            } else getMessage().sendActionBar(player, getMessage().get("commands.warp.invalid", warpName));
        } else if (getPortals().getPortalType(event.getFrom()).equalsIgnoreCase("end_portal")) {
            if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL))return;
            event.setCancelled(true);
            event.setCanCreatePortal(false);
            var warp = getWarps().getLocation(warpName);
            if (warp != null) {
                getMessage().sendActionBar(player, getMessage().get("events.teleport.success", warpName));
                player.teleport(warp);
            } else getMessage().sendActionBar(player, getMessage().get("commands.warp.invalid", warpName));
        } else if (getPortals().getPortalType(event.getFrom()).equalsIgnoreCase("end_gateway")) {
            if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_GATEWAY))return;
            event.setCancelled(true);
            event.setCanCreatePortal(false);
            var warp = getWarps().getLocation(warpName);
            if (warp != null) {
                getMessage().sendActionBar(player, getMessage().get("events.teleport.success", warpName));
                player.teleport(warp);
            } else getMessage().sendActionBar(player, getMessage().get("commands.warp.invalid", warpName));
        }
    }
}