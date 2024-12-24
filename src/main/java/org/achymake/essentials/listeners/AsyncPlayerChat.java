package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.OfflinePlayer;
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
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
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
        var userdata = getUserdata(player);
        if (!userdata.isMuted()) {
            var message = censor(event.getMessage());
            if (getVanishHandler().isVanish(player)) {
                var formatString = getMessage().addColor(getConfig().getString("chat.format.vanished"));
                var addPrefix = formatString.replace("%prefix%", userdata.getPrefix());
                var addPlayer = addPrefix.replace("%player%", userdata.getDisplayName());
                var result = addPlayer.replace("%suffix%", userdata.getSuffix());
                event.setCancelled(true);
                getVanishHandler().getVanished().forEach(vanished -> {
                    if (player.hasPermission("essentials.event.chat.color")) {
                        vanished.sendMessage(result + getMessage().addColor(message));
                    } else vanished.sendMessage(result + message);
                });
            } else if (player.isOp()) {
                var formatString = getMessage().addColor(getConfig().getString("chat.format.op"));
                var addPrefix = formatString.replace("%prefix%", userdata.getPrefix());
                var addPlayer = addPrefix.replace("%player%", userdata.getDisplayName());
                var result = addPlayer.replace("%suffix%", userdata.getSuffix());
                event.setFormat(result + getMessage().addColor(message));
            } else {
                var formatString = getMessage().addColor(getConfig().getString("chat.format.default"));
                var addPrefix = formatString.replace("%prefix%", userdata.getPrefix());
                var addPlayer = addPrefix.replace("%player%", userdata.getDisplayName());
                var result = addPlayer.replace("%suffix%", userdata.getSuffix());
                if (player.hasPermission("essentials.event.chat.color")) {
                    event.setFormat(result + getMessage().addColor(message));
                } else event.setFormat(result + message);
            }
        } else event.setCancelled(true);
    }
    private String censor(String message) {
        for (var censored : getConfig().getStringList("chat.censor")) {
            if (message.toLowerCase().contains(censored.toLowerCase())) {
                return message.toLowerCase().replace(censored.toLowerCase(), "*".repeat(censored.length()));
            }
        }
        return message;
    }
}