package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Spawn;
import org.achymake.essentials.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnLocation implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Spawn getSpawn() {
        return getInstance().getSpawn();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerSpawnLocation() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        if (getUserdata().hasJoined(event.getPlayer()))return;
        if (getSpawn().getLocation() == null)return;
        event.setSpawnLocation(getSpawn().getLocation());
    }
}