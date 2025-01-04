package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;

public class AsyncPlayerChat implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public AsyncPlayerChat() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();
        if (!getUserdata().isMuted(player)) {
            var message = getMessage().censor(event.getMessage());
            if (!getUserdata().isVanished(player)) {
                String formatString = getMessage().addPlaceholder(player, getUserdata().getChat(player));
                if (player.hasPermission("essentials.event.chat.color")) {
                    event.setFormat(formatString + getMessage().addColor(message));
                } else event.setFormat(formatString + message);
            } else {
                event.setCancelled(true);
                var formatString = getMessage().addPlaceholder(player, getMessage().addColor(getConfig().getString("chat.format.vanished")));
                for (var vanished : getVanishHandler().getVanished()) {
                    if (player.hasPermission("essentials.event.chat.color")) {
                        vanished.sendMessage(formatString + getMessage().addColor(message));
                    } else vanished.sendMessage(formatString + message);
                }
            }
        } else event.setCancelled(true);
    }
}