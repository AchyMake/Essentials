package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerBucketEmpty implements Listener {
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
    public PlayerBucketEmpty() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        var player = event.getPlayer();
        var userdata = getUserdata(player);
        if (userdata.isDisabled()) {
            event.setCancelled(true);
        } else notifyBucketEmpty(player, event.getBlock(), event.getBucket());
    }
    private void notifyBucketEmpty(Player player, Block block, Material material) {
        if (!getConfig().getBoolean("notification.enable"))return;
        if (!getConfig().getStringList("notification.bucket-empty").contains(material.toString()))return;
        var worldName = block.getWorld().getName();
        var x = block.getX();
        var y = block.getY();
        var z = block.getZ();
        getInstance().getOnlinePlayers().forEach(players -> {
            if (!players.hasPermission("essentials.event.bucket-empty.notify"))return;
            getConfig().getStringList("notification.message").forEach(messages -> {
                var addPlayer = messages.replaceAll("%player%", player.getName());
                var addMaterial = addPlayer.replaceAll("%material%", getMessage().toTitleCase(material.toString()));
                var addWorldName = addMaterial.replaceAll("%world", worldName);
                var addX = addWorldName.replaceAll("%x%", String.valueOf(x));
                var addY = addX.replaceAll("%y%", String.valueOf(y));
                var result = addY.replaceAll("%z%", String.valueOf(z));
                getMessage().send(players, result);
            });
        });
    }
}