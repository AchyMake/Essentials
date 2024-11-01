package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerCommandPreprocess implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerCommandPreprocess() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        var player = event.getPlayer();
        if (getUserdata(player).isDisabled()) {
            event.setCancelled(true);
        } else if (!player.hasPermission("essentials.event.command.exempt")) {
            if (!isDisabled(event.getMessage()))return;
            event.setCancelled(true);
        }
    }
    private boolean isDisabled(String message) {
        for (var disabled : getInstance().getConfig().getStringList("commands.disable")) {
            if (message.toLowerCase().startsWith("/" + disabled.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}