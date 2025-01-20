package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.achymake.essentials.handlers.RandomHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerDeath implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private RandomHandler getRandomHandler() {
        return getInstance().getRandomHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerDeath() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        if (getConfig().getBoolean("deaths.drop-player-head.enable")) {
            if (!getRandomHandler().isTrue(getConfig().getDouble("deaths.drop-player-head.chance")))return;
            event.getDrops().add(getMaterials().getPlayerHead(player, 1));
        }
        if (getConfig().getBoolean("deaths.drop-economy.enable")) {
            var lost = getRandomHandler().nextDouble(getConfig().getDouble("deaths.drop-economy.min"), getConfig().getDouble("deaths.drop-economy.max"));
            if (getEconomy().has(player, lost)) {
                getEconomy().remove(player, lost);
                player.sendMessage(getMessage().get("events.death", getEconomy().currency() + getEconomy().format(lost), event.getDeathMessage().replace(player.getName(), "you")));
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
        getUserdata().setLocation(player, player.getLocation(), "death");
    }
}