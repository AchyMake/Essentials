package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VanishHandler {
    private final List<Player> vanished = new ArrayList<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public List<Player> getOnlinePlayers() {
        var onlinePlayers = new ArrayList<Player>();
        getInstance().getServer().getOnlinePlayers().forEach(player -> {
            if (!getVanished().contains(player)) {
                onlinePlayers.add(player);
            }
        });
        return onlinePlayers;
    }
    public boolean isVanish(OfflinePlayer offlinePlayer) {
        return getUserdata(offlinePlayer).isVanished();
    }
    public void toggleVanish(Player player) {
        setVanish(player, !isVanish(player));
    }
    public void toggleVanish(OfflinePlayer offlinePlayer) {
        setVanish(offlinePlayer, !isVanish(offlinePlayer));
    }
    public void setVanish(OfflinePlayer offlinePlayer, boolean value) {
        var userdata = getUserdata(offlinePlayer);
        userdata.setBoolean("settings.vanished", value);
    }
    public void setVanish(Player player, boolean value) {
        var userdata = getUserdata(player);
        if (value) {
            player.setAllowFlight(true);
            player.setInvulnerable(true);
            player.setSleepingIgnored(true);
            player.setCollidable(false);
            player.setSilent(true);
            player.setCanPickupItems(false);
            getInstance().getServer().getOnlinePlayers().forEach(players -> players.hidePlayer(getInstance(), player));
            getVanished().forEach(vanished -> player.showPlayer(getInstance(), vanished));
            userdata.setBoolean("settings.vanished", true);
            getVanished().add(player);
            addVanishTask(player);
            getMessage().sendActionBar(player, "&6&lVanish:&a Enabled");
        } else {
            if (!player.hasPermission("essentials.command.fly")) {
                player.setAllowFlight(false);
            }
            player.setInvulnerable(false);
            player.setSleepingIgnored(false);
            player.setCollidable(true);
            player.setSilent(false);
            player.setCanPickupItems(true);
            getInstance().getServer().getOnlinePlayers().forEach(players -> players.showPlayer(getInstance(), player));
            getVanished().forEach(vanished -> player.hidePlayer(getInstance(), vanished));
            userdata.setBoolean("settings.vanished", false);
            userdata.disableTask("vanish");
            getVanished().remove(player);
            getMessage().sendActionBar(player, "&6&lVanish:&c Disabled");
        }
    }
    private void addVanishTask(Player player) {
        var userdata = getUserdata(player);
        int id = getScheduleHandler().runLater(new Runnable() {
            @Override
            public void run() {
                getMessage().sendActionBar(player, "&6&lVanish:&a Enabled");
                addVanishTask(player);
            }
        }, 50).getTaskId();
        userdata.addTaskID("vanish", id);
    }
    public void disable() {
        if (!getVanished().isEmpty()) {
            getVanished().forEach(player -> getUserdata(player).disableTask("vanish"));
            getVanished().clear();
        }
    }
    public List<Player> getVanished() {
        return vanished;
    }
}