package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.plugin.PluginManager;

public class InventoryClick implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public InventoryClick() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        var inventory = event.getClickedInventory();
        if (inventory == null)return;
        var player = (Player) event.getWhoClicked();
        if (!getEconomy().hasBankOpened(player))return;
        if (getEconomy().getBanks().get(player) != event.getView())return;
        if (inventory instanceof AnvilInventory anvilInventory) {
            var userdata = getUserdata(player);
            var anvilView = (AnvilView) event.getView();
            event.setCancelled(true);
            var title = anvilView.getTitle().toLowerCase();
            if (event.getSlot() == 2) {
                var itemStack = anvilInventory.getResult();
                if (itemStack == null)return;
                var amount = Double.parseDouble(anvilView.getRenameText());
                if (title.contains("withdraw")) {
                    if (amount >= getEconomy().getMinimumBankWithdraw()) {
                        if (getEconomy().hasBank(player, amount)) {
                            getEconomy().removeBank(player, amount);
                            getEconomy().add(player, amount);
                            player.sendMessage(getMessage().get("commands.bank.withdraw.success", getEconomy().currency() + getEconomy().format(amount)));
                            player.sendMessage(getMessage().get("commands.bank.withdraw.left", getEconomy().currency() + getEconomy().format(userdata.getBankAccount())));
                            event.getInventory().close();
                            getEconomy().closeBank(player);
                        } else player.sendMessage(getMessage().get("commands.bank.withdraw.insufficient-funds", getEconomy().currency() + getEconomy().format(amount)));
                    } else player.sendMessage(getMessage().get("commands.bank.withdraw.minimum", getEconomy().currency() + getEconomy().format(getEconomy().getMinimumBankWithdraw())));
                } else if (title.contains("deposit")) {
                    if (amount >= getEconomy().getMinimumBankDeposit()) {
                        if (getEconomy().has(player, amount)) {
                            getEconomy().remove(player, amount);
                            getEconomy().addBank(player, amount);
                            player.sendMessage(getMessage().get("commands.bank.deposit.success", getEconomy().currency() + getEconomy().format(amount)));
                            player.sendMessage(getMessage().get("commands.bank.deposit.left", getEconomy().currency() + getEconomy().format(userdata.getBankAccount())));
                            event.getInventory().close();
                            getEconomy().closeBank(player);
                        } else player.sendMessage(getMessage().get("commands.bank.deposit.insufficient-funds", getEconomy().currency() + getEconomy().format(amount)));
                    } else player.sendMessage(getMessage().get("commands.bank.deposit.minimum", getEconomy().currency() + getEconomy().format(getEconomy().getMinimumBankDeposit())));
                }
            } else player.sendMessage(getMessage().addColor("&cYou have to click the result item"));
        } else event.setCancelled(true);
    }
}