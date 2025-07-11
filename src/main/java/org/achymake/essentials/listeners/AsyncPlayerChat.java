package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;

import java.text.MessageFormat;

public class AsyncPlayerChat implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
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
        var username = getMessage().addPlaceholder(player, getUserdata().getChatFormat(player));
        var message = getMessage().censor(event.getMessage().replace("%", "%%"));
        var colored = getMessage().addColor(message);
        if (!getUserdata().isMuted(player)) {
            if (!getUserdata().isVanished(player)) {
                if (getMessage().isURL(event.getMessage())) {
                    if (player.hasPermission("essentials.event.chat.url")) {
                        if (player.hasPermission("essentials.event.chat.color")) {
                            event.setFormat(MessageFormat.format("{0}{1}", username, colored));
                        } else event.setFormat(MessageFormat.format("{0}{1}", username, message));
                    } else event.setCancelled(true);
                } else if (player.hasPermission("essentials.event.chat.color")) {
                    event.setFormat(MessageFormat.format("{0}{1}", username, colored));
                } else event.setFormat(MessageFormat.format("{0}{1}", username, message));
            } else {
                event.setCancelled(true);
                getVanishHandler().getVanished().forEach(vanished -> {
                    if (player.hasPermission("essentials.event.chat.color")) {
                        vanished.sendMessage(username + colored);
                    } else vanished.sendMessage(username + message);
                });
            }
        } else event.setCancelled(true);
    }
}