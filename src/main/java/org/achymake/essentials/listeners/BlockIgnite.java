package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
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
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
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
            var material = block.getType();
            if (material.equals(getMaterials().get("lava"))) {
                if (!getConfig().getBoolean("fire.disable-lava-fire-spread")) return;
                event.setCancelled(true);
            } else if (material.equals(getMaterials().get("fire"))) {
                if (!getConfig().getBoolean("fire.disable-fire-spread"))return;
                event.setCancelled(true);
            }
        }
    }
}