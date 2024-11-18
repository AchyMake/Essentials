package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Worlds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginManager;

public class ServerLoad implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Worlds getWorlds() {
        return getInstance().getWorlds();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public ServerLoad() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onServerLoad(ServerLoadEvent event) {
        if (event.getType().equals(ServerLoadEvent.LoadType.RELOAD))return;
        getWorlds().setup();
    }
}