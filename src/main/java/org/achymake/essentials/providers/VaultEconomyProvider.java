package org.achymake.essentials.providers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.achymake.essentials.Essentials;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import java.text.DecimalFormat;
import java.util.List;

public class VaultEconomyProvider implements Economy {
    private final Essentials ess;
    public VaultEconomyProvider(Essentials ess) {
        this.ess = ess;
    }
    public void register() {
        ess.getServicesManager().register(Economy.class, this, ess, ServicePriority.Normal);
    }
    @Override
    public boolean isEnabled() {
        return ess.getConfig().getBoolean("economy.enable");
    }
    @Override
    public String getName() {
        return ess.name();
    }
    @Override
    public int fractionalDigits() {
        return -1;
    }
    @Override
    public String format(double amount) {
        return new DecimalFormat(ess.getConfig().getString("economy.format")).format(amount);
    }
    @Override
    public String currencyNamePlural() {
        return ess.getConfig().getString("economy.currency");
    }
    @Override
    public String currencyNameSingular() {
        return currencyNamePlural();
    }
    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return ess.getUserdata().exists(player);
    }
    @Override
    public boolean hasAccount(String playerName) {
        return hasAccount(ess.getOfflinePlayer(playerName));
    }
    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }
    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }
    @Override
    public double getBalance(OfflinePlayer player) {
        return ess.getEconomyHandler().getAccount(player);
    }
    @Override
    public double getBalance(String playerName) {
        return getBalance(ess.getOfflinePlayer(playerName));
    }
    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }
    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }
    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return ess.getEconomyHandler().getAccount(player) >= amount;
    }
    @Override
    public boolean has(String playerName, double amount) {
        return has(ess.getOfflinePlayer(playerName), amount);
    }
    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }
    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (player == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player cannot be null!");
        } else if (amount > 0.0) {
            if (ess.getEconomyHandler().remove(player, amount)) {
                return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Could not save file");
        } else return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");
    }
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(ess.getOfflinePlayer(playerName), amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (player == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "OfflinePlayer can not be null");
        } else if (amount > 0.0) {
            if (ess.getEconomyHandler().add(player, amount)) {
                return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Could not save file");
        } else return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Can not deposit negative funds");
    }
    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (playerName == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player name can not be null.");
        } else if (amount > 0.0) {
            return depositPlayer(ess.getOfflinePlayer(playerName), amount);
        } else return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
    }
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }
    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }
    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return ess.getUserdata().reload(player);
    }
    @Override
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(ess.getOfflinePlayer(playerName));
    }
    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }
    @Override
    public boolean hasBankSupport() {
        return ess.getConfig().getBoolean("economy.bank.enable");
    }
    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        if (ess.getBank().create(name, player)) {
            return new EconomyResponse(0, ess.getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank name already exists: " + name);
    }
    @Override
    public EconomyResponse createBank(String name, String player) {
        return createBank(name, ess.getOfflinePlayer(player));
    }
    @Override
    public EconomyResponse deleteBank(String name) {
        if (ess.getBank().exists(name)) {
            ess.getBank().delete(name);
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank name does not exists: " + name);
    }
    @Override
    public EconomyResponse bankBalance(String name) {
        if (ess.getBank().exists(name)) {
            return new EconomyResponse(0, ess.getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists: " + name);
    }
    @Override
    public EconomyResponse bankHas(String name, double amount) {
        if (ess.getBank().exists(name)) {
            if (ess.getBank().has(name, amount)) {
                return new EconomyResponse(amount, ess.getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not have " + amount);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists: " + name);
    }
    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        if (ess.getBank().exists(name)) {
            if (ess.getBank().has(name, amount)) {
                if (ess.getBank().remove(name, amount)) {
                    return new EconomyResponse(amount, ess.getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
                } else return new EconomyResponse(amount, ess.getBank().get(name), EconomyResponse.ResponseType.FAILURE, "Could not save file");
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not have " + amount);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists: " + name);
    }
    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        if (ess.getBank().exists(name)) {
            if (ess.getBank().add(name, amount)) {
                return new EconomyResponse(amount, ess.getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, ess.getBank().get(name), EconomyResponse.ResponseType.FAILURE, "Could not save file");
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists: " + name);
    }
    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        if (ess.getBank().exists(name)) {
            if (ess.getBank().isOwner(name, player)) {
                return new EconomyResponse(0, ess.getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, player.getName() + " is not bank owner");
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists: " + name);
    }
    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return isBankOwner(name, ess.getOfflinePlayer(playerName));
    }
    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        if (ess.getBank().exists(name)) {
            if (ess.getBank().isMember(name, player)) {
                return new EconomyResponse(0, ess.getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, player.getName() + " is not bank owner");
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists: " + name);
    }
    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return isBankMember(name, ess.getOfflinePlayer(playerName));
    }
    @Override
    public List<String> getBanks() {
        return ess.getBank().getListed();
    }
}