package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.events.PlayerChangedChunkEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class PlayerChangedChunk implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerChangedChunk() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangedChunk(PlayerChangedChunkEvent event) {
        if (!event.isCancelled())return;
        event.getPlayer().teleport(event.getFrom());
    }
}