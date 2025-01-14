package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

public class BlockRedstone implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockRedstone() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (!getConfig().getBoolean("physics.disable-redstone"))return;
        if (event.getBlock().getType().equals(getInstance().getMaterialHandler().get("powered_rail")))return;
        if (event.getBlock().getType().equals(getInstance().getMaterialHandler().get("detector_rail")))return;
        if (event.getBlock().getType().equals(getInstance().getMaterialHandler().get("activator_rail")))return;
        event.setNewCurrent(event.getOldCurrent());
    }
}