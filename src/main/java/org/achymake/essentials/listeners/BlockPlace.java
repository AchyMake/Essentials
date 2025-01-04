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
        if (!getUserdata().isDisabled(player)) {
            if (event.getBlockPlaced().getType().equals(getMaterials().get("spawner"))) {
                getWorldHandler().updateSpawner(event.getBlockPlaced(), event.getItemInHand());
            }
            if (getConfig().getBoolean("notification.enable")) {
                if (!getConfig().getStringList("notification.block-place").contains(event.getBlockPlaced().getType().toString()))return;
                var worldName = event.getBlockPlaced().getWorld().getName();
                var x = String.valueOf(event.getBlockPlaced().getX());
                var y = String.valueOf(event.getBlockPlaced().getY());
                var z = String.valueOf(event.getBlockPlaced().getZ());
                getConfig().getStringList("notification.message").forEach(messages -> {
                    var addPlayer = messages.replaceAll("%player%", player.getName());
                    var addMaterial = addPlayer.replaceAll("%material%", getMessage().toTitleCase(event.getBlockPlaced().getType().toString()));
                    var addWorldName = addMaterial.replaceAll("%world", worldName);
                    var addX = addWorldName.replaceAll("%x%", x);
                    var addY = addX.replaceAll("%y%", y);
                    var result = addY.replaceAll("%z%", z);
                    getMessage().sendAll(result, "essentials.event.block_place.notify");
                });
            }
        } else event.setCancelled(true);
    }
}