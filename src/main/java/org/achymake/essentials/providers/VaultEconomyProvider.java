package org.achymake.essentials.providers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Bank;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EconomyHandler;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class VaultEconomyProvider implements Economy {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private OfflinePlayer getOfflinePlayer(String playerName) {
        return getInstance().getOfflinePlayer(playerName);
    }
    private Bank getBank() {
        return getInstance().getBank();
    }
    @Override
    public boolean isEnabled() {
        return getInstance().getConfig().getBoolean("economy.enable");
    }
    @Override
    public String getName() {
        return getInstance().name();
    }
    @Override
    public boolean hasBankSupport() {
        return true;
    }
    @Override
    public int fractionalDigits() {
        return -1;
    }
    @Override
    public String format(double amount) {
        return getEconomyHandler().format(amount);
    }
    @Override
    public String currencyNamePlural() {
        return getEconomyHandler().currency();
    }
    @Override
    public String currencyNameSingular() {
        return currencyNamePlural();
    }
    @Override
    public boolean hasAccount(String playerName) {
        return getUserdata().exists(getOfflinePlayer(playerName));
    }
    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return getUserdata().exists(player);
    }
    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return getUserdata().exists(getOfflinePlayer(playerName));
    }
    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return getUserdata().exists(player);
    }
    @Override
    public double getBalance(String playerName) {
        return getUserdata().getAccount(getOfflinePlayer(playerName));
    }
    @Override
    public double getBalance(OfflinePlayer player) {
        return getUserdata().getAccount(player);
    }
    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }
    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }
    @Override
    public boolean has(String playerName, double amount) {
        return getUserdata().getAccount(getOfflinePlayer(playerName)) >= amount;
    }
    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getUserdata().getAccount(player) >= amount;
    }
    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }
    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        if (playerName == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player name cannot be null!");
        } else if (amount > 0.0) {
            getEconomyHandler().remove(getOfflinePlayer(playerName), amount);
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");
    }
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (player == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player cannot be null!");
        } else if (amount > 0.0) {
            getEconomyHandler().remove(player, amount);
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds!");
    }
    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }
    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (playerName == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Player name can not be null.");
        } else if (amount > 0.0) {
            getEconomyHandler().add(getOfflinePlayer(playerName), amount);
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
    }
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (player == null) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "OfflinePlayer can not be null");
        } else if (amount > 0.0) {
            getEconomyHandler().add(player, amount);
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Can not deposit negative funds");
    }
    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }
    @Override
    public boolean createPlayerAccount(String playerName) {
        getUserdata().reload(getOfflinePlayer(playerName));
        return true;
    }
    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        getUserdata().reload(player);
        return true;
    }
    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }
    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
    @Override
    public EconomyResponse createBank(String name, String player) {
        if (getBank().create(name, getOfflinePlayer(player))) {
            return new EconomyResponse(0, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank name already exists");
    }
    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        if (getBank().create(name, player)) {
            return new EconomyResponse(0, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank name already exists");
    }
    @Override
    public EconomyResponse deleteBank(String name) {
        if (getBank().exists(name)) {
            getBank().delete(name);
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank name does not exists");
    }
    @Override
    public EconomyResponse bankBalance(String name) {
        if (getBank().exists(name)) {
            return new EconomyResponse(0, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists");
    }
    @Override
    public EconomyResponse bankHas(String name, double amount) {
        if (getBank().exists(name)) {
            if (getBank().has(name, amount)) {
                return new EconomyResponse(amount, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not have " + amount);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists");
    }
    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        if (getBank().exists(name)) {
            if (getBank().has(name, amount)) {
                getBank().remove(name, amount);
                return new EconomyResponse(amount, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not have " + amount);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists");
    }
    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        if (getBank().exists(name)) {
            getBank().add(name, amount);
            return new EconomyResponse(amount, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists");
    }
    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        if (getBank().exists(name)) {
            if (getBank().isOwner(name, getOfflinePlayer(playerName))) {
                return new EconomyResponse(0, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, playerName + " is not bank owner");
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists");
    }
    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        if (getBank().exists(name)) {
            if (getBank().isOwner(name, player)) {
                return new EconomyResponse(0, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, player.getName() + " is not bank owner");
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists");
    }
    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        if (getBank().exists(name)) {
            if (getBank().isMember(name, getOfflinePlayer(playerName))) {
                return new EconomyResponse(0, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, playerName + " is not bank owner");
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists");
    }
    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        if (getBank().exists(name)) {
            if (getBank().isMember(name, player)) {
                return new EconomyResponse(0, getBank().get(name), EconomyResponse.ResponseType.SUCCESS, null);
            } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, player.getName() + " is not bank owner");
        } else return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Bank does not exists");
    }
    @Override
    public List<String> getBanks() {
        return getBank().getListed();
    }
}