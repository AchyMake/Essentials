package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScoreboardHandler;
import org.achymake.essentials.handlers.TablistHandler;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
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
    private Userdata getUserdata() {
        return getInstance().getUserdata();
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
    public PlayerQuit() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        getTablistHandler().disable(player);
        if (getUserdata().hasBoard(player)) {
            getScoreboardHandler().disable(player);
        }
        if (!getUserdata().isVanished(player)) {
            if (getConfig().getBoolean("connection.quit.enable")) {
                event.setQuitMessage(getMessage().addPlaceholder(player, getConfig().getString("connection.quit.message")));
                playSound();
            } else if (player.hasPermission("essentials.event.quit.message")) {
                event.setQuitMessage(getMessage().addPlaceholder(player, getConfig().getString("connection.quit.message")));
                playSound();
            } else {
                event.setQuitMessage(null);
                getMessage().sendAll(getMessage().get("events.quit.notify", player.getName()), "essentials.event.quit.notify");
            }
        } else {
            getVanishHandler().getVanished().remove(player);
            event.setQuitMessage(null);
        }
        getUserdata().disableTasks(player);
        if (getUserdata().getTpaSent(player) != null) {
            var sent = getUserdata().getTpaSent(player);
            getUserdata().setString(sent, "tpa.from", null);
            getUserdata().setString(player, "tpa.sent", null);
            getUserdata().removeTask(sent, "tpa");
            getUserdata().removeTask(player, "tpa");
        } else if (getUserdata().getTpaFrom(player) != null) {
            var from = getUserdata().getTpaFrom(player);
            getUserdata().setString(from, "tpa.sent", null);
            getUserdata().setString(player, "tpa.from", null);
            getUserdata().removeTask(from, "tpa");
            getUserdata().removeTask(player, "tpa");
        }
        if (getUserdata().getTpaHereSent(player) != null) {
            var sent = getUserdata().getTpaHereSent(player);
            getUserdata().setString(sent, "tpahere.from", null);
            getUserdata().setString(player, "tpahere.sent", null);
            getUserdata().removeTask(sent, "tpahere");
            getUserdata().removeTask(player, "tpahere");
        } else if (getUserdata().getTpaHereFrom(player) != null) {
            var from = getUserdata().getTpaHereFrom(player);
            getUserdata().setString(from, "tpahere.sent", null);
            getUserdata().setString(player, "tpahere.from", null);
            getUserdata().removeTask(from, "tpahere");
            getUserdata().removeTask(player, "tpahere");
        }
        if (getUserdata().getBankSent(player) != null) {
            var sent = getUserdata().getBankSent(player);
            getUserdata().setString(sent, "bank-invite.from", null);
            getUserdata().setString(player, "bank-invite.sent", null);
            getUserdata().removeTask(sent, "bank-invite");
            getUserdata().removeTask(player, "bank-invite");
        } else if (getUserdata().getBankFrom(player) != null) {
            var from = getUserdata().getBankFrom(player);
            getUserdata().setString(from, "bank-invite.sent", null);
            getUserdata().setString(player, "bank-invite.from", null);
            getUserdata().removeTask(from, "bank-invite");
            getUserdata().removeTask(player, "bank-invite");
        }
        getUserdata().setLocation(player, player.getLocation(), "quit");
    }
    private void playSound() {
        if (!getConfig().getBoolean("connection.quit.sound.enable"))return;
        var soundType = getConfig().getString("connection.quit.sound.type");
        var volume = (float) getConfig().getDouble("connection.quit.sound.volume");
        var pitch = (float) getConfig().getDouble("connection.quit.sound.pitch");
        getInstance().getOnlinePlayers().forEach(target -> target.playSound(target, Sound.valueOf(soundType), volume, pitch));
    }
}