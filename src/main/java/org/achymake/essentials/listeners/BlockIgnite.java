package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public BlockIgnite() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getIgnitingEntity() instanceof Player player) {
            var userdata = getUserdata(player);
            if (!userdata.isDisabled())return;
            event.setCancelled(true);
        } else if (event.getIgnitingBlock() != null) {
            var block = event.getIgnitingBlock();
            if (block.isLiquid() && getConfig().getBoolean("fire.disable-lava-fire-spread")) {
                event.setCancelled(true);
            } else if (block.getType().equals(Material.FIRE) && getConfig().getBoolean("fire.disable-fire-spread")) {
                event.setCancelled(true);
            }
        }
    }
}