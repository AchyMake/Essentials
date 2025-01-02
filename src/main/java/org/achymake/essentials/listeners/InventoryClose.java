package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Bank;
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
    private Bank getBank() {
        return getInstance().getBank();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public InventoryClose() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent event) {
        var player = (Player) event.getPlayer();
        if (!getBank().hasBankOpened(player))return;
        if (getBank().getBanks().get(player) != event.getView())return;
        getBank().closeBank(player);
    }
}