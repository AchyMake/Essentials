package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;

public class AsyncPlayerChat implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
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
        var username = getMessage().addPlaceholder(player, getUserdata().getChatFormat(player));
        var message = event.getMessage().replace("%", "%%");
        var censored = getMessage().censor(message);
        if (!getUserdata().isMuted(player)) {
            if (!getUserdata().isVanished(player)) {
                if (getMessage().isURL(event.getMessage())) {
                    if (player.hasPermission("essentials.event.chat.url")) {
                        if (player.hasPermission("essentials.event.chat.color")) {
                            event.setFormat(getMessage().addColor(username + "&r") + getMessage().addColor(censored));
                        } else event.setFormat(getMessage().addColor(username + "&r") + censored);
                    } else event.setCancelled(true);
                } else if (player.hasPermission("essentials.event.chat.color")) {
                    event.setFormat(getMessage().addColor(username + "&r") + getMessage().addColor(censored));
                } else event.setFormat(getMessage().addColor(username + "&r") + censored);
            } else {
                event.setCancelled(true);
                getInstance().getVanishHandler().getVanished().forEach(vanished -> {
                    if (player.hasPermission("essentials.event.chat.color")) {
                        vanished.sendMessage(getMessage().addColor(username + "&r") + getMessage().addColor(censored));
                    } else vanished.sendMessage(getMessage().addColor(username + "&r") + censored);
                });
            }
        } else event.setCancelled(true);
    }
}