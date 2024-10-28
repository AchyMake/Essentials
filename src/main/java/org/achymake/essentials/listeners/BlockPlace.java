package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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
        var userdata = getUserdata(player);
        if (userdata.isDisabled()) {
            event.setCancelled(true);
        } else notifyBlockPlace(player, event.getBlockPlaced());
    }
    private void notifyBlockPlace(Player player, Block block) {
        if (getConfig().getBoolean("notification.enable")) {
            var blockType = block.getType().toString();
            if (!getConfig().getStringList("notification.block-place").contains(blockType))return;
            String worldName = block.getWorld().getName();
            var x = block.getX();
            var y = block.getY();
            var z = block.getZ();
            getInstance().getOnlinePlayers().forEach(players -> {
                if (!players.hasPermission("essentials.event.block_place.notify"))return;
                getConfig().getStringList("notification.message").forEach(messages -> {
                    var addPlayer = messages.replaceAll("%player%", player.getName());
                    var addMaterial = addPlayer.replaceAll("%material%", getMessage().toTitleCase(blockType));
                    var addWorldName = addMaterial.replaceAll("%world", worldName);
                    var addX = addWorldName.replaceAll("%x%", String.valueOf(x));
                    var addY = addX.replaceAll("%y%", String.valueOf(y));
                    var result = addY.replaceAll("%z%", String.valueOf(z));
                    getMessage().send(players, result);
                });
            });
        }
    }
}