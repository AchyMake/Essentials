package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.UpdateChecker;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.achymake.essentials.handlers.TablistHandler;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
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
    private VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    private UpdateChecker getUpdateChecker() {
        return getInstance().getUpdateChecker();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private TablistHandler getTablistHandler() {
        return getInstance().getTablistHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
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
        getTablistHandler().apply(player);
        getInstance().getScoreboardHandler().apply(player);
        if (!getUserdata(player).isVanished()) {
            getVanishHandler().hideVanished(player);
            if (getConfig().getBoolean("connection.join.enable")) {
                sendJoinSound();
                event.setJoinMessage(getMessage().addColor(getConfig().getString("connection.join.message").replaceAll("%player%", player.getName())));
            } else if (player.hasPermission("essentials.event.join.message")) {
                sendJoinSound();
                event.setJoinMessage(getMessage().addColor(getConfig().getString("connection.join.message").replaceAll("%player%", player.getName())));
            } else {
                event.setJoinMessage(null);
                getMessage().sendAll(getMessage().get("events.join.notify", player.getName()), "essentials.event.join.notify");
            }
            getScheduler().runLater(new Runnable() {
                @Override
                public void run() {
                    if (getUserdata(player).hasJoined()) {
                        getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day.welcome-back"));
                    } else getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day.welcome"));
                }
            }, 0);
        } else {
            event.setJoinMessage(null);
            getVanishHandler().setVanish(player, true);
            getVanishHandler().getVanished().forEach(target -> target.sendMessage(getMessage().get("events.join.vanished", player.getName())));
        }
        getUpdateChecker().getUpdate(player);
    }
    private void sendJoinSound() {
        if (!getConfig().getBoolean("connection.join.sound.enable"))return;
        var soundType = getConfig().getString("connection.join.sound.type");
        var volume = (float) getConfig().getDouble("connection.join.sound.volume");
        var pitch = (float) getConfig().getDouble("connection.join.sound.pitch");
        getInstance().getOnlinePlayers().forEach(target -> target.playSound(target, Sound.valueOf(soundType), volume, pitch));
    }
}