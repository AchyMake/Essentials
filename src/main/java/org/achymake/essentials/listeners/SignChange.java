package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.PluginManager;

public class SignChange implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
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
    public SignChange() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent event) {
        var player = event.getPlayer();
        var userdata = getUserdata(player);
        if (userdata.isDisabled()) {
            event.setCancelled(true);
        } else if (player.hasPermission("essentials.event.sign.color")) {
            for (int i = 0; i < event.getLines().length; i++) {
                if (!event.getLine(i).contains("&"))return;
                event.setLine(i, getMessage().addColor(event.getLine(i)));
            }
        }
    }
}