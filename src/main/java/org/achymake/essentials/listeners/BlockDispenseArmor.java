package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.plugin.PluginManager;

public class BlockDispenseArmor implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockDispenseArmor() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDispenseArmor(BlockDispenseArmorEvent event) {
        if (!getConfig().getBoolean("physics.disable-redstone"))return;
        event.setCancelled(true);
    }
}