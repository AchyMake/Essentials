package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MaterialHandler;
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
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
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
        var material = event.getBlock().getType();
        if (material.equals(getMaterials().get("powered_rail")))return;
        if (material.equals(getMaterials().get("detector_rail")))return;
        if (material.equals(getMaterials().get("activator_rail")))return;
        if (material.equals(getMaterials().get("daylight_detector")))return;
        if (material.equals(getMaterials().get("redstone_lamp")))return;
        event.setNewCurrent(event.getOldCurrent());
    }
}