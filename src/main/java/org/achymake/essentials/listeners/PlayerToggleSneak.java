package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerToggleSneak implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerToggleSneak() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        var player = event.getPlayer();
        if (!getUserdata().isVanished(player))return;
        if (!getConfig().getBoolean("vanish.sneaking-picks-item"))return;
        if (!player.isOnGround())return;
        player.setCanPickupItems(!player.isSneaking());
    }
}