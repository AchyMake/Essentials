package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginManager;

import java.text.SimpleDateFormat;

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
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Server getServer() {
        return getInstance().getServer();
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
        if (getServer().hasWhitelist()) {
            if (getServer().getWhitelistedPlayers().contains(player)) {
                if (getServer().getOnlinePlayers().size() >= getServer().getMaxPlayers()) {
                    if (player.hasPermission("essentials.event.login.full_server")) {
                        if (userdata.exists()) {
                            if (userdata.isBanned()) {
                                if (!getInstance().getDateHandler().expired(userdata.getBanExpire())) {
                                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor("Reason: " + userdata.getBanReason() + ", expires: " + SimpleDateFormat.getInstance().format(userdata.getBanExpire())));
                                } else allow(event, player);
                            } else allow(event, player);
                        } else allow(event, player);
                    } else event.disallow(PlayerLoginEvent.Result.KICK_FULL, getConfig().getString("connection.login.full"));
                } else if (userdata.exists()) {
                    if (userdata.isBanned()) {
                        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor("Reason: " + userdata.getBanReason()));
                    } else allow(event, player);
                } else allow(event, player);
            } else event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, getConfig().getString("connection.login.whitelisted"));
        } else {
            if (getServer().getOnlinePlayers().size() >= getServer().getMaxPlayers()) {
                if (player.hasPermission("essentials.event.login.full_server")) {
                    if (userdata.exists()) {
                        if (userdata.isBanned()) {
                            if (!getInstance().getDateHandler().expired(userdata.getBanExpire())) {
                                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor("Reason: " + userdata.getBanReason() + ", expires: " + SimpleDateFormat.getInstance().format(userdata.getBanExpire())));
                            } else allow(event, player);
                        } else allow(event, player);
                    } else allow(event, player);
                } else event.disallow(PlayerLoginEvent.Result.KICK_FULL, getConfig().getString("connection.login.full"));
            } else if (userdata.exists()) {
                if (userdata.isBanned()) {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, getMessage().addColor("Reason: " + userdata.getBanReason()));
                } else allow(event, player);
            } else allow(event, player);
        }
    }
    private void allow(PlayerLoginEvent event, Player player) {
        event.allow();
        getUserdata(player).reload();
    }
}