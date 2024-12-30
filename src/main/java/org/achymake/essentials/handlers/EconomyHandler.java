package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class EconomyHandler {
    private final Map<Player, InventoryView> banks = new HashMap<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private InventoryHandler getInventoryHandler() {
        return getInstance().getInventoryHandler();
    }
    public HashMap<OfflinePlayer, Double> getAccounts() {
        var accounts = new HashMap<OfflinePlayer, Double>();
        for (var offlinePlayer : getInstance().getOfflinePlayers()) {
            if (!getUserdata().isBanned(offlinePlayer) || !getUserdata().isDisabled(offlinePlayer)) {
                accounts.put(offlinePlayer, get(offlinePlayer));
            }
        }
        return accounts;
    }
    public HashMap<OfflinePlayer, Double> getBankAccounts() {
        var bankAccounts = new HashMap<OfflinePlayer, Double>();
        for (var offlinePlayer : getInstance().getOfflinePlayers()) {
            if (!getUserdata().isBanned(offlinePlayer) || !getUserdata().isDisabled(offlinePlayer)) {
                bankAccounts.put(offlinePlayer, getBank(offlinePlayer));
            }
        }
        return bankAccounts;
    }
    public Set<Map.Entry<OfflinePlayer, Double>> getTopAccounts(HashMap<OfflinePlayer, Double> accounts, int size) {
        var list = new ArrayList<>(accounts.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        var result = new LinkedHashMap<OfflinePlayer, Double>();
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(size)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (var entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result.entrySet();
    }
    public String currency() {
        return getConfig().getString("economy.currency");
    }
    public String getFormat() {
        return getConfig().getString("economy.format");
    }
    public double get(OfflinePlayer offlinePlayer) {
        return getUserdata().getAccount(offlinePlayer);
    }
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return get(offlinePlayer) >= amount;
    }
    public void add(OfflinePlayer offlinePlayer, double amount) {
        getUserdata().setDouble(offlinePlayer, "account", amount + get(offlinePlayer));
    }
    public void remove(OfflinePlayer offlinePlayer, double amount) {
        getUserdata().setDouble(offlinePlayer, "account", get(offlinePlayer) - amount);
    }
    public void reset(OfflinePlayer offlinePlayer) {
        getUserdata().setDouble(offlinePlayer, "account", getConfig().getDouble("economy.starting-balance"));
    }
    public void set(OfflinePlayer offlinePlayer, double amount) {
        getUserdata().setDouble(offlinePlayer, "account", amount);
    }
    public String format(double value) {
        return new DecimalFormat(getFormat()).format(value);
    }
    public double getBank(OfflinePlayer offlinePlayer) {
        return getUserdata().getBankAccount(offlinePlayer);
    }
    public boolean hasBank(OfflinePlayer offlinePlayer, double amount) {
        return getBank(offlinePlayer) >= amount;
    }
    public void removeBank(OfflinePlayer offlinePlayer, double amount) {
        getUserdata().setDouble(offlinePlayer, "bank.account", getBank(offlinePlayer) - amount);
    }
    public void addBank(OfflinePlayer offlinePlayer, double amount) {
        getUserdata().setDouble(offlinePlayer, "bank.account", amount + getBank(offlinePlayer));
    }
    public boolean hasBankOpened(Player player) {
        return banks.containsKey(player);
    }
    public void openBankWithdraw(Player player) {
        var anvil = getInventoryHandler().openAnvil(player);
        if (anvil != null) {
            var itemStack = getMaterialHandler().getItemStack("paper", 1);
            var itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("0.0");
            itemStack.setItemMeta(itemMeta);
            anvil.setTitle("Bank: Withdraw");
            anvil.setItem(0, itemStack);
            player.updateInventory();
            banks.put(player, anvil);
        } else player.sendMessage(getInstance().getMessage().get("error.not-provided"));
    }
    public void openBankDeposit(Player player) {
        var anvil = getInventoryHandler().openAnvil(player);
        if (anvil != null) {
            var itemStack = getMaterialHandler().getItemStack("paper", 1);
            var itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("0.0");
            itemStack.setItemMeta(itemMeta);
            anvil.setTitle("Bank: Deposit");
            anvil.setItem(0, itemStack);
            player.updateInventory();
            banks.put(player, anvil);
        } else player.sendMessage(getInstance().getMessage().get("error.not-provided"));
    }
    public void closeBank(Player player) {
        if (banks.get(player).getTopInventory().isEmpty())return;
        banks.get(player).getTopInventory().forEach(itemStack -> {
            if (itemStack == null)return;
            itemStack.setAmount(0);
        });
        banks.remove(player);
    }
    public double getStartingBalance() {
        return getConfig().getDouble("economy.starting-balance");
    }
    public double getStartingBank() {
        return getConfig().getDouble("economy.bank.starting-balance");
    }
    public double getMinimumPayment() {
        return getInstance().getConfig().getDouble("economy.minimum-payment");
    }
    public double getMinimumBankWithdraw() {
        return getInstance().getConfig().getDouble("economy.bank.minimum-withdraw");
    }
    public double getMinimumBankDeposit() {
        return getInstance().getConfig().getDouble("economy.bank.minimum-withdraw");
    }
    public Map<Player, InventoryView> getBanks() {
        return banks;
    }
}