package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;

import java.util.Random;

public class PlayerDeath implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerDeath() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        getUserdata(player).setLocation(player.getLocation(), "death");
        if (getConfig().getBoolean("deaths.drop-player-head.enable")) {
            if (getConfig().getInt("deaths.drop-player-head.chance") > new Random().nextInt(100)) {
                event.getDrops().add(getMaterials().getPlayerHead(player, 1));
            }
        }
        if (getConfig().getBoolean("deaths.drop-economy.enable")) {
            var min = getConfig().getDouble("deaths.drop-economy.min");
            var max = getConfig().getDouble("deaths.drop-economy.max");
            var lost = new Random().nextDouble(min, max);
            if (getEconomy().has(player, lost)) {
                getEconomy().remove(player, lost);
                var message = event.getDeathMessage().replace(player.getName(), "");
                getMessage().send(player, "&cYou lost&a " + getEconomy().currency() + getEconomy().format(lost) + "&c you" + message);
            }
        }
        if (player.hasPermission("essentials.event.death.keep_inventory")) {
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
        if (player.hasPermission("essentials.event.death.keep_exp")) {
            event.setKeepLevel(true);
            event.setDroppedExp(0);
        }
    }
}