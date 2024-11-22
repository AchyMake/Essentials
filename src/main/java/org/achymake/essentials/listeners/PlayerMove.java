package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerMove implements Listener {
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
    public PlayerMove() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() != null) {
            var from = event.getFrom();
            var to = event.getTo();
            if (!hasMoved(from, to))return;
            var player = event.getPlayer();
            var userdata = getUserdata(player);
            if (userdata.isFrozen()) {
                event.setCancelled(true);
            } else if (userdata.hasTaskID("teleport")) {
                if (!getConfig().getBoolean("teleport.cancel-on-move"))return;
                userdata.disableTask("teleport");
                player.sendMessage(getMessage().get("events.move"));
            }
        } else event.setCancelled(true);
    }
    private boolean hasMoved(Location from, Location to) {
        if (from.getX() != to.getX()) {
            return true;
        } else if (from.getY() != to.getY()) {
            return true;
        } else return from.getZ() != to.getZ();
    }
}