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
    public boolean isVanish(OfflinePlayer offlinePlayer) {
        return getUserdata().isVanished(offlinePlayer);
    }
    public void hideVanished(Player player) {
        if (!getVanished().isEmpty()) {
            getVanished().forEach(target -> player.hidePlayer(getInstance(), target));
        }
    }
    public void toggleVanish(Player player) {
        setVanish(player, !isVanish(player));
    }
    public void toggleVanish(OfflinePlayer offlinePlayer) {
        setVanish(offlinePlayer, !isVanish(offlinePlayer));
    }
    public void setVanish(OfflinePlayer offlinePlayer, boolean value) {
        getUserdata().setBoolean(offlinePlayer, "settings.vanished", value);
    }
    public void setVanish(Player player, boolean value) {
        var userdata = getUserdata();
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
            userdata.setBoolean(player, "settings.vanished", true);
            getVanished().add(player);
            addVanishTask(player);
            getMessage().sendActionBar(player, getMessage().get("events.vanish", getMessage().get("enable")));
        } else {
            if (!player.hasPermission("essentials.command.fly")) {
                player.setAllowFlight(false);
            }
            player.setInvulnerable(false);
            player.setSleepingIgnored(false);
            player.setCollidable(true);
            player.setSilent(false);
            player.setCanPickupItems(true);
            userdata.setBoolean(player, "settings.vanished", false);
            userdata.removeTask(player, "vanish");
            getVanished().remove(player);
            getInstance().getOnlinePlayers().forEach(players -> players.showPlayer(getInstance(), player));
            if (!getVanished().isEmpty()) {
                getVanished().forEach(vanished -> player.hidePlayer(getInstance(), vanished));
            }
            getMessage().sendActionBar(player, getMessage().get("events.vanish", getMessage().get("disable")));
        }
    }
    private void addVanishTask(Player player) {
        var taskID = getScheduler().runTimer(new Vanish(player), 50, 50).getTaskId();
        getUserdata().addTaskID(player, "vanish", taskID);
    }
    public void disable() {
        if (!getVanished().isEmpty()) {
            getVanished().forEach(player -> getUserdata().removeTask(player, "vanish"));
            getVanished().clear();
        }
    }
    public List<Player> getVanished() {
        return vanished;
    }
}