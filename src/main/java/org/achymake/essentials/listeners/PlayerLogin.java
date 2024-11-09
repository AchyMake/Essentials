package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.DateHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerLogin implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private DateHandler getDateHandler() {
        return getInstance().getDateHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerLogin() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        var player = event.getPlayer();
        var userdata = getUserdata(player);
        var server = getInstance().getServer();
        if (server.hasWhitelist()) {
            if (server.getWhitelistedPlayers().contains(player)) {
                if (server.getOnlinePlayers().size() >= server.getMaxPlayers()) {
                    if (player.hasPermission("essentials.event.login.full_server")) {
                        if (userdata.exists()) {
                            if (userdata.isBanned()) {
                                if (!getInstance().getDateHandler().expired(userdata.getBanExpire())) {
                                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor(getMessage().get("events.login.banned", userdata.getBanReason(), getDateHandler().getFormatted(userdata.getBanExpire()))));
                                } else allow(event, player);
                            } else allow(event, player);
                        } else allow(event, player);
                    } else event.disallow(PlayerLoginEvent.Result.KICK_FULL, getConfig().getString("connection.login.full"));
                } else if (userdata.exists()) {
                    if (userdata.isBanned()) {
                        if (!getInstance().getDateHandler().expired(userdata.getBanExpire())) {
                            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor(getMessage().get("events.login.banned", userdata.getBanReason(), getDateHandler().getFormatted(userdata.getBanExpire()))));
                        } else allow(event, player);
                    } else allow(event, player);
                } else allow(event, player);
            } else event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, getConfig().getString("connection.login.whitelisted"));
        } else {
            if (server.getOnlinePlayers().size() >= server.getMaxPlayers()) {
                if (player.hasPermission("essentials.event.login.full_server")) {
                    if (userdata.exists()) {
                        if (userdata.isBanned()) {
                            if (!getInstance().getDateHandler().expired(userdata.getBanExpire())) {
                                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor(getMessage().get("events.login.banned", userdata.getBanReason(), getDateHandler().getFormatted(userdata.getBanExpire()))));
                            } else allow(event, player);
                        } else allow(event, player);
                    } else allow(event, player);
                } else event.disallow(PlayerLoginEvent.Result.KICK_FULL, getConfig().getString("connection.login.full"));
            } else if (userdata.exists()) {
                if (userdata.isBanned()) {
                    if (!getInstance().getDateHandler().expired(userdata.getBanExpire())) {
                        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor(getMessage().get("events.login.banned", userdata.getBanReason(), getDateHandler().getFormatted(userdata.getBanExpire()))));
                    } else allow(event, player);
                } else allow(event, player);
            } else allow(event, player);
        }
    }
    private void allow(PlayerLoginEvent event, Player player) {
        event.allow();
        getUserdata(player).reload();
    }
}