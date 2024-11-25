package org.achymake.essentials.listeners;

import io.papermc.paper.event.block.PlayerShearBlockEvent;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class PlayerShearBlock implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerShearBlock() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerShearEntity(PlayerShearBlockEvent event) {
        if (!getEntityHandler().disableBlockChange(event.getPlayer().getType())) {
            if (!getUserdata(event.getPlayer()).isDisabled())return;
            event.setCancelled(true);
        } else event.setCancelled(true);
    }
}