package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerQuit implements Listener {
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
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerQuit() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        var userdata = getUserdata(player);
        if (userdata.isVanished()) {
            getVanishHandler().getVanished().remove(player);
            event.setQuitMessage(null);
        } else if (getConfig().getBoolean("connection.quit.enable")) {
            event.setQuitMessage(getQuitMessage(player));
            playSound();
        } else if (player.hasPermission("essentials.event.quit.message")) {
            event.setQuitMessage(getQuitMessage(player));
            playSound();
        } else {
            event.setQuitMessage(null);
            getMessage().sendAll(player.getName() + "&7 left the Server", "essentials.event.quit.notify");
        }
        removeTeleport(player);
        removeVanish(player);
        removeTPA(player);
        removeTPAHere(player);
        userdata.setLocation(player.getLocation(), "quit");
    }
    private void removeTeleport(Player player) {
        var userdata = getUserdata(player);
        if (!userdata.hasTaskID("teleport"))return;
        userdata.disableTask("teleport");
    }
    private void removeVanish(Player player) {
        var userdata = getUserdata(player);
        if (!userdata.hasTaskID("vanish"))return;
        userdata.disableTask("vanish");
    }
    private void removeTPA(Player player) {
        var userdata = getUserdata(player);
        if (userdata.getTpaFrom() != null) {
            var tpaFrom = userdata.getTpaFrom();
            var userdataTarget = getUserdata(tpaFrom);
            if (getScheduler().isQueued(userdataTarget.getTaskID("tpa"))) {
                userdata.disableTask("tpa");
            }
            userdataTarget.setString("tpa.sent", null);
            userdata.setString("tpa.from", null);
        } else if (userdata.getTpaSent() != null) {
            var tpaSent = userdata.getTpaSent();
            var userdataTarget = getUserdata(tpaSent);
            userdataTarget.setString("tpa.from", null);
            if (getScheduler().isQueued(userdata.getTaskID("tpa"))) {
                userdata.disableTask("tpa");
            }
            userdata.setString("tpa.sent", null);
        }
    }
    private void removeTPAHere(Player player) {
        var userdata = getUserdata(player);
        if (userdata.getTpaHereFrom() != null) {
            var tpaHereFrom = userdata.getTpaHereFrom();
            var userdataTarget = getUserdata(tpaHereFrom);
            if (getScheduler().isQueued(userdataTarget.getTaskID("tpahere"))) {
                userdataTarget.disableTask("tpahere");
            }
            userdataTarget.setString("tpahere.sent", null);
            userdata.setString("tpahere.from", null);
        } else if (userdata.getTpaHereSent() != null) {
            var tpaHereSent = userdata.getTpaHereSent();
            var userdataTarget = getUserdata(tpaHereSent);
            userdataTarget.setString("tpahere.from", null);
            if (getScheduler().isQueued(userdata.getTaskID("tpahere"))) {
                userdata.disableTask("tpahere");
            }
            userdata.setString("tpahere.sent", null);
        }
    }
    private void playSound() {
        if (!getConfig().getBoolean("connection.quit.sound.enable"))return;
        var soundType = getConfig().getString("connection.quit.sound.type");
        var volume = (float) getConfig().getDouble("connection.quit.sound.volume");
        var pitch = (float) getConfig().getDouble("connection.quit.sound.pitch");
        getInstance().getOnlinePlayers().forEach(players -> players.playSound(players, Sound.valueOf(soundType), volume, pitch));
    }
    private String getQuitMessage(Player player) {
        return getMessage().addColor(getConfig().getString("connection.quit.message").replaceAll("%player%", player.getName()));
    }
}