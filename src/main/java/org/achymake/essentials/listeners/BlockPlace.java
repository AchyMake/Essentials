package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;

public class BlockPlace implements Listener {
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
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockPlace() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        var block = event.getBlockPlaced();
        var material = block.getType();
        if (!getUserdata().isDisabled(player)) {
            if (material.equals(getMaterials().get("spawner"))) {
                getWorldHandler().updateSpawner(block, event.getItemInHand());
            }
            if (getConfig().getBoolean("physics.disable-redstone")) {
                if (material.equals(getMaterials().get("redstone_torch"))) {
                    block.setBlockData(getMaterials().disableTorch(block.getBlockData()));
                } else if (material.equals(getMaterials().get("redstone_wall_torch"))) {
                    block.setBlockData(getMaterials().disableTorch(block.getBlockData()));
                }
            }
            if (getConfig().getBoolean("notification.enable")) {
                if (!getConfig().getStringList("notification.block-place").contains(material.toString()))return;
                var name = player.getName();
                var worldName = block.getWorld().getName();
                var x = String.valueOf(block.getX());
                var y = String.valueOf(block.getY());
                var z = String.valueOf(block.getZ());
                getConfig().getStringList("notification.message").forEach(messages -> getMessage().sendAll(messages.replaceAll("%player%", name)
                        .replaceAll("%material%", getMessage().toTitleCase(material.toString()))
                        .replaceAll("%world%", worldName)
                        .replaceAll("%x%", x)
                        .replaceAll("%y%", y)
                        .replaceAll("%z%", z), "essentials.event.block_place.notify"));
            }
        } else event.setCancelled(true);
    }
}