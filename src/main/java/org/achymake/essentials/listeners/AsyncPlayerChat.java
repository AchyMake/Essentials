package org.achymake.essentials.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;

public class AsyncPlayerChat implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
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
        var message = event.getMessage();
        if (!userdata.isMuted()) {
            if (getVanishHandler().isVanish(player)) {
                var vanishColor = getInstance().getConfig().getString("vanish-chat-color");
                var vanishFormat = getMessage().addColor(vanishColor + userdata.getDisplayName() + "&f: ");
                event.setCancelled(true);
                getVanishHandler().getVanished().forEach(vanished -> {
                    if (player.hasPermission("essentials.event.chat.color")) {
                        getMessage().send(vanished, vanishFormat + getMessage().addColor(message));
                    } else {
                        getMessage().send(vanished, vanishFormat + message);
                    }
                });
            } else if (player.isOp()) {
                var opFormat = getMessage().addColor(prefix(player) + "&4" + userdata.getDisplayName() + "&f" + suffix(player) + "&f: ");
                event.setFormat(opFormat + getMessage().addColor(message));
            } else {
                var format = getMessage().addColor(prefix(player) + userdata.getDisplayName() + suffix(player) + "&f: ");
                if (player.hasPermission("essentials.event.chat.color")) {
                    event.setFormat(format + getMessage().addColor(message));
                } else event.setFormat(format + message);
            }
        } else event.setCancelled(true);
    }
    public String prefix(Player player) {
        if (PlaceholderAPI.isRegistered("vault")) {
            return getMessage().addColor(PlaceholderAPI.setPlaceholders(player, "%vault_prefix%"));
        } else return "";
    }
    public String suffix(Player player) {
        if (PlaceholderAPI.isRegistered("vault")) {
            return getMessage().addColor(PlaceholderAPI.setPlaceholders(player, "%vault_suffix%"));
        } else return "";
    }
}