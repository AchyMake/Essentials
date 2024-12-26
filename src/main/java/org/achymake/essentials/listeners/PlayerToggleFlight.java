package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerToggleFlight implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerToggleFlight() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        if (!getUserdata().isDisabled(event.getPlayer()))return;
        event.setCancelled(true);
    }
}