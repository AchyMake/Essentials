package org.achymake.essentials.listeners;

import io.papermc.paper.event.block.PlayerShearBlockEvent;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class PlayerShearBlock implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerShearBlock() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerShearEntity(PlayerShearBlockEvent event) {
        if (!getUserdata().isDisabled(event.getPlayer()))return;
        event.setCancelled(true);
    }
}