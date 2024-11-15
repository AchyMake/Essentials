package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.PluginManager;

public class InventoryClose implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public InventoryClose() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryCloseEvent event) {
        var player = (Player) event.getPlayer();
        if (!getEconomyHandler().hasBankOpened(player))return;
        if (getEconomyHandler().getBanks().get(player) != event.getView())return;
        getEconomyHandler().closeBank(player);
    }
}