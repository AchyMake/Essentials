package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.plugin.PluginManager;

public class BlockIgnite implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockIgnite() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getIgnitingEntity() instanceof Player player) {
            if (!getUserdata().isDisabled(player))return;
            event.setCancelled(true);
        } else if (event.getIgnitingBlock() != null) {
            var block = event.getIgnitingBlock();
            if (block.isLiquid() && getConfig().getBoolean("fire.disable-lava-fire-spread")) {
                event.setCancelled(true);
            } else if (block.getType().equals(getInstance().getMaterialHandler().get("fire")) && getConfig().getBoolean("fire.disable-fire-spread")) {
                event.setCancelled(true);
            }
        }
    }
}