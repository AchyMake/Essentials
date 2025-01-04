package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Bank;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.providers.VaultEconomyProvider;
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
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Bank getBank() {
        return getInstance().getBank();
    }
    private VaultEconomyProvider getEconomy() {
        return getInstance().getVaultEconomyProvider();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public InventoryClick() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        var inventory = event.getClickedInventory();
        if (inventory == null)return;
        var player = (Player) event.getWhoClicked();
        if (!getBank().hasBankOpened(player))return;
        if (getBank().getBanks().get(player) != event.getView())return;
        if (inventory instanceof AnvilInventory anvilInventory) {
            var anvilView = (AnvilView) event.getView();
            event.setCancelled(true);
            var title = anvilView.getTitle().toLowerCase();
            if (event.getSlot() == 2) {
                var itemStack = anvilInventory.getResult();
                if (itemStack == null)return;
                var amount = Double.parseDouble(anvilView.getRenameText());
                if (title.contains("withdraw")) {
                    if (amount >= getEconomy().getMinimumBankWithdraw()) {
                        if (getBank().has(getUserdata().getBank(player), amount)) {
                            getBank().remove(getUserdata().getBank(player), amount);
                            getEconomy().depositPlayer(player, amount);
                            player.sendMessage(getMessage().get("commands.bank.withdraw.success", getEconomy().currencyNamePlural() + getEconomy().format(amount)));
                            player.sendMessage(getMessage().get("commands.bank.withdraw.left", getEconomy().currencyNamePlural() + getEconomy().format(getBank().get(getUserdata().getBank(player)))));
                            getBank().closeBank(player);
                            event.getInventory().close();
                        } else player.sendMessage(getMessage().get("commands.bank.withdraw.insufficient-funds", getEconomy().currencyNamePlural() + getEconomy().format(amount)));
                    } else player.sendMessage(getMessage().get("commands.bank.withdraw.minimum", getEconomy().currencyNamePlural() + getEconomy().format(getEconomy().getMinimumBankWithdraw())));
                } else if (title.contains("deposit")) {
                    if (amount >= getEconomy().getMinimumBankDeposit()) {
                        if (getEconomy().has(player, amount)) {
                            getEconomy().withdrawPlayer(player, amount);
                            getBank().add(getUserdata().getBank(player), amount);
                            player.sendMessage(getMessage().get("commands.bank.deposit.success", getEconomy().currencyNamePlural() + getEconomy().format(amount)));
                            player.sendMessage(getMessage().get("commands.bank.deposit.left", getEconomy().currencyNamePlural() + getEconomy().format(getBank().get(getUserdata().getBank(player)))));
                            getBank().closeBank(player);
                            event.getInventory().close();
                        } else player.sendMessage(getMessage().get("commands.bank.deposit.insufficient-funds", getEconomy().currencyNamePlural() + getEconomy().format(amount)));
                    } else player.sendMessage(getMessage().get("commands.bank.deposit.minimum", getEconomy().currencyNamePlural() + getEconomy().format(getEconomy().getMinimumBankDeposit())));
                }
            } else player.sendMessage(getMessage().addColor("&cYou have to click the result item"));
        } else event.setCancelled(true);
    }
}