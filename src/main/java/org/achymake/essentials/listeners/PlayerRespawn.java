package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Spawn;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerRespawn implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Spawn getSpawn() {
        return getInstance().getSpawn();
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
        if (getConfig().getBoolean("deaths.send-location")) {
            userdata.sendDeathLocation();
        } else if (player.hasPermission("essentials.event.death.location")) {
            userdata.sendDeathLocation();
        }
        if (event.isAnchorSpawn())return;
        if (event.isBedSpawn())return;
        var home = getUserdata(player).getLocation("home");
        if (home != null) {
            event.setRespawnLocation(home);
        } else if (getSpawn().getLocation() != null) {
            event.setRespawnLocation(getSpawn().getLocation());
        }
    }
}