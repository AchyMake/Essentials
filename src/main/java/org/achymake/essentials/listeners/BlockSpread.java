package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.plugin.PluginManager;

public class BlockSpread implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockSpread() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.getSource().getType().equals(Material.FIRE)) {
            if (!getConfig().getBoolean("fire.disable-fire-spread"))return;
            event.setCancelled(true);
        } else if (event.getSource().getType().equals(Material.LAVA)) {
            if (!getConfig().getBoolean("fire.disable-lava-fire-spread"))return;
            event.setCancelled(true);
        }
    }
}