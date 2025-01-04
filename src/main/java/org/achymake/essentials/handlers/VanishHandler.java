package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.runnable.Vanish;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishHandler {
    private final List<Player> vanished = new ArrayList<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    /**
     * is vanished
     * @param offlinePlayer offlinePlayer
     * @return true if offlinePlayer is vanished else false
     * @since many moons ago
     */
    public boolean isVanish(OfflinePlayer offlinePlayer) {
        return getUserdata().isVanished(offlinePlayer);
    }
    /**
     * hide vanished for target
     * @param player target
     * @since many moons ago
     */
    public void hideVanished(Player player) {
        if (!getVanished().isEmpty()) {
            getVanished().forEach(vanished -> player.hidePlayer(getInstance(), vanished));
        }
    }
    /**
     * toggle vanish
     * @param player target
     * @return true if target is vanish else false
     * @since many moons ago
     */
    public boolean toggleVanish(Player player) {
        return setVanish(player, !isVanish(player));
    }
    /**
     * toggle vanish
     * @param offlinePlayer offlinePlayer
     * @return true if target is vanish else false
     * @since many moons ago
     */
    public boolean toggleVanish(OfflinePlayer offlinePlayer) {
        return setVanish(offlinePlayer, !isVanish(offlinePlayer));
    }
    /**
     * set vanish
     * @param offlinePlayer offlinePlayer
     * @param value boolean
     * @return true if target is vanish else false
     * @since many moons ago
     */
    public boolean setVanish(OfflinePlayer offlinePlayer, boolean value) {
        getUserdata().setBoolean(offlinePlayer, "settings.vanished", value);
        return isVanish(offlinePlayer);
    }
    /**
     * set vanish
     * @param player target
     * @param value boolean
     * @return true if target is vanish else false
     * @since many moons ago
     */
    public boolean setVanish(Player player, boolean value) {
        if (value) {
            player.setAllowFlight(true);
            player.setInvulnerable(true);
            player.setSleepingIgnored(true);
            player.setCollidable(false);
            player.setSilent(true);
            player.setCanPickupItems(false);
            getInstance().getOnlinePlayers().forEach(players -> {
                if (!getVanished().contains(players)) {
                    players.hidePlayer(getInstance(), player);
                }
            });
            getUserdata().setBoolean(player, "settings.vanished", true);
            getVanished().add(player);
            getMessage().sendActionBar(player, getMessage().get("events.vanish", getMessage().get("enable")));
            var taskID = getScheduler().runTimer(new Vanish(player), 0, 50).getTaskId();
            getUserdata().addTaskID(player, "vanish", taskID);
        } else {
            if (!player.hasPermission("essentials.command.fly")) {
                player.setAllowFlight(false);
            }
            player.setInvulnerable(false);
            player.setSleepingIgnored(false);
            player.setCollidable(true);
            player.setSilent(false);
            player.setCanPickupItems(true);
            getUserdata().setBoolean(player, "settings.vanished", false);
            getUserdata().removeTask(player, "vanish");
            getVanished().remove(player);
            getInstance().getOnlinePlayers().forEach(players -> players.showPlayer(getInstance(), player));
            if (!getVanished().isEmpty()) {
                getVanished().forEach(vanished -> player.hidePlayer(getInstance(), vanished));
            }
            getMessage().sendActionBar(player, getMessage().get("events.vanish", getMessage().get("disable")));
        }
        return isVanish(player);
    }
    /**
     * disable this is for onDisable
     * @since many moons ago
     */
    public void disable() {
        if (!getVanished().isEmpty()) {
            getVanished().forEach(player -> getUserdata().removeTask(player, "vanish"));
            getVanished().clear();
        }
    }
    /**
     * get vanished
     * @return list player
     * @since many moons ago
     */
    public List<Player> getVanished() {
        return vanished;
    }
}