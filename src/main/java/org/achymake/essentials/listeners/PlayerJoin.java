package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.UpdateChecker;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerJoin implements Listener {
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
    private VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    private UpdateChecker getUpdateChecker() {
        return getInstance().getUpdateChecker();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerJoin() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (!getUserdata(player).isVanished()) {
            if (!getVanishHandler().getVanished().isEmpty()) {
                getVanishHandler().getVanished().forEach(vanished -> player.hidePlayer(getInstance(), vanished));
            }
            if (getConfig().getBoolean("connection.join.enable")) {
                sendJoinSound();
                event.setJoinMessage(getJoinMessage(player));
            } else if (player.hasPermission("essentials.event.join.message")) {
                sendJoinSound();
                event.setJoinMessage(getJoinMessage(player));
            } else {
                event.setJoinMessage(null);
                getMessage().sendAll(getMessage().get("events.join.notify", player.getName()), "essentials.event.join.notify");
            }
            sendMotd(player);
        } else {
            event.setJoinMessage(null);
            getVanishHandler().setVanish(player, true);
            getVanishHandler().getVanished().forEach(vanished -> vanished.sendMessage(getMessage().get("events.join.vanished", player.getName())));
        }
        getUpdateChecker().getUpdate(player);
    }
    private void sendMotd(Player player) {
        var userdata = getUserdata(player);
        getInstance().getScheduleHandler().runLater(new Runnable() {
            @Override
            public void run() {
                if (userdata.hasJoined()) {
                    getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day.welcome-back"));
                } else getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day.welcome"));
            }
        }, 0);
    }
    private void sendJoinSound() {
        if (!getConfig().getBoolean("connection.join.sound.enable"))return;
        var soundType = getConfig().getString("connection.join.sound.type");
        var volume = (float) getConfig().getDouble("connection.join.sound.volume");
        var pitch = (float) getConfig().getDouble("connection.join.sound.pitch");
        getInstance().getOnlinePlayers().forEach(players -> players.playSound(players, Sound.valueOf(soundType), volume, pitch));
    }
    private String getJoinMessage(Player player) {
        return getMessage().addColor(getConfig().getString("connection.join.message").replaceAll("%player%", player.getName()));
    }
}