package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.plugin.PluginManager;

public class BlockPistonRetract implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockPistonRetract() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (!getConfig().getBoolean("physics.disable-pistons"))return;
        event.setCancelled(true);
    }
}