package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.OfflinePlayer;
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
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public BlockPlace() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        if (getUserdata(player).isDisabled()) {
            event.setCancelled(true);
        } else {
            var block = event.getBlockPlaced();
            if (block.getType().equals(getMaterials().get("spawner"))) {
                getMaterials().updateSpawner(block, event.getItemInHand());
            }
            if (getConfig().getBoolean("notification.enable")) {
                if (!getConfig().getStringList("notification.block-place").contains(block.getType().toString()))return;
                var worldName = block.getWorld().getName();
                var x = String.valueOf(block.getX());
                var y = String.valueOf(block.getY());
                var z = String.valueOf(block.getZ());
                getConfig().getStringList("notification.message").forEach(messages -> {
                    var addPlayer = messages.replaceAll("%player%", player.getName());
                    var addMaterial = addPlayer.replaceAll("%material%", getMessage().toTitleCase(block.getType().toString()));
                    var addWorldName = addMaterial.replaceAll("%world", worldName);
                    var addX = addWorldName.replaceAll("%x%", x);
                    var addY = addX.replaceAll("%y%", y);
                    var result = addY.replaceAll("%z%", z);
                    getMessage().sendAll(result, "essentials.event.block_place.notify");
                });
            }
        }
    }
}