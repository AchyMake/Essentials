package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Spawn;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerRespawn implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Spawn getSpawn() {
        return getInstance().getSpawn();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerRespawn() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.getRespawnReason().equals(PlayerRespawnEvent.RespawnReason.DEATH))return;
        var player = event.getPlayer();
        var userdata = getUserdata(player);
        if (event.isAnchorSpawn())return;
        if (event.isBedSpawn())return;
        var home = userdata.getLocation("home");
        if (home != null) {
            event.setRespawnLocation(home);
        } else if (getSpawn().getLocation() != null) {
            event.setRespawnLocation(getSpawn().getLocation());
        }
        if (player.hasPermission("essentials.event.death.location")) {
            var location = userdata.getLocation("death");
            if (location == null) return;
            var world = location.getWorld().getEnvironment().toString().toLowerCase();
            var x = location.getBlockX();
            var y = location.getBlockY();
            var z = location.getBlockZ();
            getMessage().send(player, "&6Death location:");
            getMessage().send(player, "&6World:&f " + world + "&6 X:&f " + x + "&6 Y:&f " + y + "&6 Z:&f " + z);
        }
    }
}