package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.InventoryHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Bank {
    private final Map<Player, InventoryView> banks = new HashMap<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private InventoryHandler getInventoryHandler() {
        return getInstance().getInventoryHandler();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private File getFile(String bankName) {
        return new File(getInstance().getDataFolder(), "bank/" + bankName + ".yml");
    }
    public boolean exists(String bankName) {
        return getFile(bankName).exists();
    }
    public List<String> getListed() {
        var listed = new ArrayList<String>();
        var folder = new File(getInstance().getDataFolder(), "bank");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    listed.add(file.getName().replace(".yml", ""));
                }
            }
        }
        return listed;
    }
    public FileConfiguration getConfig(String bankName) {
        return YamlConfiguration.loadConfiguration(getFile(bankName));
    }
    public boolean create(String bankName, OfflinePlayer offlinePlayer) {
        if (!exists(bankName)) {
            var file = getFile(bankName);
            var config = YamlConfiguration.loadConfiguration(file);
            config.set("owner", offlinePlayer.getUniqueId().toString());
            config.set("account", getEconomyHandler().getStartingBank());
            config.set("members", new ArrayList<String>());
            try {
                config.save(file);
                getUserdata().setString(offlinePlayer, "bank", bankName);
                getUserdata().setString(offlinePlayer, "bank-rank", "owner");
                return true;
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return false;
    }
    public boolean rename(String bankName, String name) {
        if (exists(bankName)) {
            if (!exists(name)) {
                return getFile(bankName).renameTo(getFile(name));
            } else return false;
        } else return false;
    }
    public boolean delete(String bankName) {
        if (exists(bankName)) {
            if (!getMembers(bankName).isEmpty()) {
                for (var member : getMembers(bankName)) {
                    getUserdata().setString(member, "bank", "");
                    getUserdata().setString(member, "bank-rank", "default");
                }
            }
            var owner = getOwner(bankName);
            getUserdata().setString(owner, "bank", "");
            getUserdata().setString(owner, "bank-rank", "default");
            getFile(bankName).delete();
            return true;
        } else return false;
    }
    public String getOwnerUUIDString(String bankName) {
        if (exists(bankName)) {
            return getConfig(bankName).getString("owner");
        } else return null;
    }
    public UUID getOwnerUUID(String bankName) {
        if (exists(bankName)) {
            return UUID.fromString(getOwnerUUIDString(bankName));
        } else return null;
    }
    public OfflinePlayer getOwner(String bankName) {
        if (exists(bankName)) {
            return getInstance().getOfflinePlayer(getOwnerUUID(bankName));
        } else return null;
    }
    public boolean isOwner(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            return getOwner(bankName).equals(offlinePlayer);
        } else return false;
    }
    public List<OfflinePlayer> getMembers(String bankName) {
        if (exists(bankName)) {
            var listed = new ArrayList<OfflinePlayer>();
            var members = getMembersUUID(bankName);
            if (!members.isEmpty()) {
                for (var memberUUID : members) {
                    listed.add(getInstance().getOfflinePlayer(memberUUID));
                }
            }
            return listed;
        } else return new ArrayList<>();
    }
    public List<UUID> getMembersUUID(String bankName) {
        if (exists(bankName)) {
            var listed = new ArrayList<UUID>();
            var members = getMembersString(bankName);
            if (!members.isEmpty()) {
                for (var memberUUIDString : members) {
                    listed.add(UUID.fromString(memberUUIDString));
                }
            }
            return listed;
        } else return new ArrayList<>();
    }
    public List<String> getMembersString(String banName) {
        if (exists(banName)) {
            return getConfig(banName).getStringList("members");
        } else return new ArrayList<>();
    }
    public boolean isMember(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            return getMembers(bankName).contains(offlinePlayer);
        } else return false;
    }
    public boolean addMember(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            if (!isMember(bankName, offlinePlayer)) {
                var file = getFile(bankName);
                var config = YamlConfiguration.loadConfiguration(file);
                var listed = getMembersString(bankName);
                listed.add(offlinePlayer.getUniqueId().toString());
                config.set("members", listed);
                try {
                    config.save(file);
                    getUserdata().setString(offlinePlayer, "bank", bankName);
                    getUserdata().setString(offlinePlayer, "bank-rank", "default");
                    return true;
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                    return false;
                }
            } else return false;
        } else return false;
    }
    public boolean removeMember(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            if (isMember(bankName, offlinePlayer)) {
                var file = getFile(bankName);
                var config = YamlConfiguration.loadConfiguration(file);
                var listed = getMembersString(bankName);
                listed.remove(offlinePlayer.getUniqueId().toString());
                config.set("members", listed);
                try {
                    config.save(file);
                    getUserdata().setString(offlinePlayer, "bank", "");
                    getUserdata().setString(offlinePlayer, "bank-rank", "default");
                    return true;
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                    return false;
                }
            } else return false;
        } else return false;
    }
    public double get(String bankName) {
        if (exists(bankName)) {
            return getConfig(bankName).getDouble("account");
        } else return 0;
    }
    public boolean has(String bankName, double amount) {
        return get(bankName) >= amount;
    }
    public boolean remove(String bankName, double amount) {
        if (exists(bankName)) {
            if (has(bankName, amount)) {
                var file = getFile(bankName);
                var config = YamlConfiguration.loadConfiguration(file);
                var result = get(bankName) - amount;
                config.set("account", result);
                try {
                    config.save(file);
                    return true;
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                    return false;
                }
            } else return false;
        } else return false;
    }
    public boolean add(String bankName, double amount) {
        if (exists(bankName)) {
            var file = getFile(bankName);
            var config = YamlConfiguration.loadConfiguration(file);
            var result = get(bankName) + amount;
            config.set("account", result);
            try {
                config.save(file);
                return true;
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return false;
    }
    public boolean hasBankOpened(Player player) {
        return banks.containsKey(player);
    }
    public void openBankWithdraw(Player player) {
        var anvil = getInventoryHandler().openAnvil(player);
        if (anvil != null) {
            var itemStack = getMaterials().getItemStack("paper", 1);
            var itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("0.0");
            itemStack.setItemMeta(itemMeta);
            anvil.setTitle("Bank: Withdraw");
            anvil.setItem(0, itemStack);
            player.updateInventory();
            banks.put(player, anvil);
        } else player.sendMessage(getInstance().getMessage().get("error.invalid"));
    }
    public void openBankDeposit(Player player) {
        var anvil = getInventoryHandler().openAnvil(player);
        if (anvil != null) {
            var itemStack = getMaterials().getItemStack("paper", 1);
            var itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("0.0");
            itemStack.setItemMeta(itemMeta);
            anvil.setTitle("Bank: Deposit");
            anvil.setItem(0, itemStack);
            player.updateInventory();
            banks.put(player, anvil);
        } else player.sendMessage(getInstance().getMessage().get("error.invalid"));
    }
    public void closeBank(Player player) {
        if (banks.get(player).getTopInventory().isEmpty())return;
        banks.get(player).getTopInventory().forEach(itemStack -> {
            if (itemStack == null)return;
            itemStack.setAmount(0);
        });
        banks.remove(player);
    }
    public void reload() {
        var folder = new File(getInstance().getDataFolder(), "bank");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    var config = YamlConfiguration.loadConfiguration(file);
                    try {
                        config.load(file);
                    } catch (IOException | InvalidConfigurationException e) {
                        getInstance().sendWarning(e.getMessage());
                    }
                }
            }
        }
    }
    public Map<Player, InventoryView> getBanks() {
        return banks;
    }
}