package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
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
        if (!userdata.isVanished()) {
            if (getConfig().getBoolean("connection.quit.enable")) {
                event.setQuitMessage(getQuitMessage(player));
                playSound();
            } else if (player.hasPermission("essentials.event.quit.message")) {
                event.setQuitMessage(getQuitMessage(player));
                playSound();
            } else {
                event.setQuitMessage(null);
                getMessage().sendAll(getMessage().get("events.quit.notify", player.getName()), "essentials.event.quit.notify");
            }
        } else {
            getVanishHandler().getVanished().remove(player);
            event.setQuitMessage(null);
        }
        userdata.disableTasks();
        if (userdata.getTpaSent() != null) {
            var sent = userdata.getTpaSent();
            getUserdata(sent).setString("tpa.from", null);
            userdata.setString("tpa.sent", null);
        } else if (userdata.getTpaFrom() != null) {
            var from = userdata.getTpaFrom();
            getUserdata(from).setString("tpa.sent", null);
            userdata.setString("tpa.from", null);
        } else if (userdata.getTpaHereSent() != null) {
            var sent = userdata.getTpaHereSent();
            getUserdata(sent).setString("tpahere.from", null);
            userdata.setString("tpahere.sent", null);
        } else if (userdata.getTpaHereFrom() != null) {
            var from = userdata.getTpaHereFrom();
            getUserdata(from).setString("tpahere.sent", null);
            userdata.setString("tpahere.from", null);
        }
        userdata.setLocation(player.getLocation(), "quit");
    }
    private void playSound() {
        if (!getConfig().getBoolean("connection.quit.sound.enable"))return;
        getInstance().getOnlinePlayers().forEach(target
                -> target.playSound(target,
                Sound.valueOf(getConfig().getString("connection.quit.sound.type")),
                (float) getConfig().getDouble("connection.quit.sound.volume"),
                (float) getConfig().getDouble("connection.quit.sound.pitch")));
    }
    private String getQuitMessage(Player player) {
        return getMessage().addColor(getConfig().getString("connection.quit.message").replaceAll("%player%", player.getName()));
    }
}