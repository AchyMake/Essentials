package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Spawn;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
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
    private WorldHandler getWorldHandler(World world) {
        return getInstance().getWorldHandler(world);
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
        if (player.hasPermission("essentials.event.death.location")) {
            var location = userdata.getLocation("death");
            if (location == null)return;
            var world = getWorldHandler(location.getWorld()).getDisplayName();
            var x = String.valueOf(location.getBlockX());
            var y = String.valueOf(location.getBlockY());
            var z = String.valueOf(location.getBlockZ());
            player.sendMessage(getMessage().get("events.respawn.title"));
            player.sendMessage(getMessage().get("events.respawn.location", world, x, y, z));
        }
        if (event.isAnchorSpawn())return;
        if (event.isBedSpawn())return;
        var home = userdata.getLocation("home");
        if (home != null) {
            event.setRespawnLocation(home);
        } else if (getSpawn().getLocation() != null) {
            event.setRespawnLocation(getSpawn().getLocation());
        }
    }
}