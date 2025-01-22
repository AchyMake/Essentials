package org.achymake.essentials.handlers;

import net.milkbowl.vault.economy.Economy;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Bank;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.providers.VaultEconomyProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class EconomyHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Bank getBank() {
        return getInstance().getBank();
    }
    private ServicesManager getServicesManager() {
        return getInstance().getServicesManager();
    }
    public double get(OfflinePlayer offlinePlayer) {
        return getUserdata().getAccount(offlinePlayer);
    }
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return getUserdata().getAccount(offlinePlayer) >= amount;
    }
    public boolean set(OfflinePlayer offlinePlayer, double amount) {
        return getUserdata().setDouble(offlinePlayer, "account", amount);
    }
    public boolean add(OfflinePlayer offlinePlayer, double amount) {
        return set(offlinePlayer, amount + get(offlinePlayer));
    }
    public boolean remove(OfflinePlayer offlinePlayer, double amount) {
        if (has(offlinePlayer, get(offlinePlayer) - amount)) {
            return set(offlinePlayer, get(offlinePlayer) - amount);
        } else return false;
    }
    /**
     * get set map offlinePlayer, double
     * @return set map offlinePlayer, double
     * @since many moons ago
     */
    public Set<Map.Entry<OfflinePlayer, Double>> getTopAccounts() {
        var accounts = new HashMap<OfflinePlayer, Double>();
        for (var offlinePlayer : getInstance().getOfflinePlayers()) {
            if (!getUserdata().isBanned(offlinePlayer) || !getUserdata().isDisabled(offlinePlayer)) {
                accounts.put(offlinePlayer, get(offlinePlayer));
            }
        }
        var list = new ArrayList<>(accounts.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        var result = new LinkedHashMap<OfflinePlayer, Double>();
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (var entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result.entrySet();
    }
    /**
     * get set map string, double
     * @return set map string, double
     * @since many moons ago
     */
    public Set<Map.Entry<String, Double>> getTopBanks() {
        var bankAccounts = new HashMap<String, Double>();
        for (var bankName : getBank().getListed()) {
            bankAccounts.put(bankName, getBank().get(bankName));
        }
        if (!bankAccounts.isEmpty()) {
            var list = new ArrayList<>(bankAccounts.entrySet());
            list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
            var result = new LinkedHashMap<String, Double>();
            result.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(10)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            for (var entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
            return result.entrySet();
        } else return new HashSet<>();
    }
    public String format(double amount) {
        return new DecimalFormat(getConfig().getString("economy.format")).format(amount);
    }
    public String currency() {
        return getConfig().getString("economy.currency");
    }
    public double getStartingBalance() {
        return getConfig().getDouble("economy.starting-balance");
    }
    public double getStartingBank() {
        return getConfig().getDouble("economy.bank.starting-balance");
    }
    public double getMinimumPayment() {
        return getConfig().getDouble("economy.minimum-payment");
    }
    public double getMinimumBankWithdraw() {
        return getConfig().getDouble("economy.bank.minimum-withdraw");
    }
    public double getMinimumBankDeposit() {
        return getConfig().getDouble("economy.bank.minimum-withdraw");
    }
}