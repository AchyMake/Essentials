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
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public AsyncPlayerChat() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        var player = event.getPlayer();
        if (!getUserdata().isMuted(player)) {
            var message = getMessage().censor(event.getMessage());
            if (!getUserdata().isVanished(player)) {
                if (!player.isOp()) {
                    String formatString;
                    if (!getUserdata().getRank(player).isEmpty()) {
                        formatString = getMessage().addPlaceholder(player, getConfig().getString("chat.format." + getUserdata().getRank(player)));
                    } else formatString = getMessage().addPlaceholder(player, getConfig().getString("chat.format.default"));
                    if (player.hasPermission("essentials.event.chat.color")) {
                        event.setFormat(formatString + getMessage().addColor(message));
                    } else event.setFormat(formatString + message);
                } else event.setFormat(getMessage().addPlaceholder(player, getConfig().getString("chat.format.op")) + getMessage().addColor(message));
            } else {
                var formatString = getMessage().addPlaceholder(player, getMessage().addColor(getConfig().getString("chat.format.vanished")));
                event.setCancelled(true);
                getVanishHandler().getVanished().forEach(vanished -> {
                    if (player.hasPermission("essentials.event.chat.color")) {
                        vanished.sendMessage(formatString + getMessage().addColor(message));
                    } else vanished.sendMessage(formatString + message);
                });
            }
        } else event.setCancelled(true);
    }
}