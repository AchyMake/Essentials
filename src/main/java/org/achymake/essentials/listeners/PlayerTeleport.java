package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerTeleport implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerTeleport() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        var player = event.getPlayer();
        if (!getUserdata().isDisabled(player)) {
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND) || event.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN)) {
                getUserdata().setLocation(player, player.getLocation(), "recent");
            }
        } else event.setCancelled(true);
    }
}