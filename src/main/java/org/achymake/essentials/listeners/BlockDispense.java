package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.plugin.PluginManager;

public class BlockDispense implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public BlockDispense() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDispense(BlockDispenseEvent event) {
        if (!getConfig().getBoolean("physics.disable-redstone"))return;
        event.setCancelled(true);
    }
}