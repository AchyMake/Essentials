package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Portals;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;

public class BlockBreak implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Portals getPortals() {
        return getInstance().getPortals();
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
    public BlockBreak() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();
        if (getUserdata(player).isDisabled()) {
            event.setCancelled(true);
        } else if (getPortals().hasWand(player.getInventory().getItemInMainHand())) {
            event.setCancelled(true);
            getPortals().setPrimary(player.getInventory().getItemInMainHand(), event.getBlock());
            player.sendMessage(getMessage().get("events.portal.primary"));
        } else {
            var block = event.getBlock();
            if (block.getType().equals(getMaterials().get("spawner"))) {
                if (getMaterials().hasEnchantment(player.getInventory().getItemInMainHand(), "silk_touch")) {
                    if (player.hasPermission("essentials.event.block_break.spawner")) {
                        getMaterials().dropSpawner(block);
                        event.setExpToDrop(0);
                    }
                }
            }
            if (getConfig().getBoolean("notification.enable")) {
                if (!getConfig().getStringList("notification.block-break").contains(block.getType().toString()))return;
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
                    getMessage().sendAll(result, "essentials.event.block_break.notify");
                });
            }
        }
    }
}