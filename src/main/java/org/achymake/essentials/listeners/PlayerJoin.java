package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.UpdateChecker;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.achymake.essentials.handlers.ScoreboardHandler;
import org.achymake.essentials.handlers.TablistHandler;
import org.achymake.essentials.handlers.VanishHandler;
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
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private UpdateChecker getUpdateChecker() {
        return getInstance().getUpdateChecker();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private ScoreboardHandler getScoreboardHandler() {
        return getInstance().getScoreboardHandler();
    }
    private TablistHandler getTablistHandler() {
        return getInstance().getTablistHandler();
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
    public PlayerJoin() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (getUserdata().hasBoard(player)) {
            getScoreboardHandler().apply(player);
        }
        getTablistHandler().apply(player);
        if (!getUserdata().isVanished(player)) {
            getVanishHandler().hideVanished(player);
            if (getConfig().getBoolean("connection.join.enable")) {
                sendJoinSound();
                event.setJoinMessage(getMessage().addPlaceholder(player, getConfig().getString("connection.join.message")));
            } else if (player.hasPermission("essentials.event.join.message")) {
                sendJoinSound();
                event.setJoinMessage(getMessage().addPlaceholder(player, getConfig().getString("connection.join.message")));
            } else {
                event.setJoinMessage(null);
                getMessage().sendAll(getMessage().get("events.join.notify", player.getName()), "essentials.event.join.notify");
            }
            getScheduler().runLater(() -> {
                if (getUserdata().hasJoined(player)) {
                    getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day.welcome-back"));
                } else getMessage().sendStringList(player, getConfig().getStringList("message-of-the-day.welcome"));
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