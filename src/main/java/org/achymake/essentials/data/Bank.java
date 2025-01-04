package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.InventoryHandler;
import org.achymake.essentials.handlers.MaterialHandler;
import org.achymake.essentials.providers.VaultEconomyProvider;
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
    private VaultEconomyProvider getEconomy() {
        return getInstance().getVaultEconomyProvider();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    /**
     * get file bank/bankName.yml
     * @param bankName string
     * @return file
     * @since many moons ago
     */
    public File getFile(String bankName) {
        return new File(getInstance().getDataFolder(), "bank/" + bankName + ".yml");
    }
    /**
     * if file exists
     * @param bankName string
     * @return true if file exists else false
     * @since many moons ago
     */
    public boolean exists(String bankName) {
        return getFile(bankName).exists();
    }
    /**
     * get listed
     * @return list string
     * @since many moons ago
     */
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
    /**
     * get bankName config
     * @param bankName string
     * @return config
     * @since many moons ago
     */
    public FileConfiguration getConfig(String bankName) {
        return YamlConfiguration.loadConfiguration(getFile(bankName));
    }
    /**
     * create bank
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if file was created else false if file exists
     * @since many moons ago
     */
    public boolean create(String bankName, OfflinePlayer offlinePlayer) {
        if (!exists(bankName)) {
            var file = getFile(bankName);
            var config = YamlConfiguration.loadConfiguration(file);
            config.set("owner", offlinePlayer.getUniqueId().toString());
            config.set("account", getEconomy().getStartingBank());
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
    /**
     * rename bank
     * @param bankName string
     * @param name string
     * @return true if file was renamed else false if file exists
     * @since many moons ago
     */
    public boolean rename(String bankName, String name) {
        if (exists(bankName)) {
            if (!exists(name)) {
                return getFile(bankName).renameTo(getFile(name));
            } else return false;
        } else return false;
    }
    /**
     * delete bank
     * @param bankName string
     * @return true if file was deleted else false if file does not exist
     * @since many moons ago
     */
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
    /**
     * get owner uuid string
     * @param bankName string
     * @return string if bank exists else null
     * @since many moons ago
     */
    public String getOwnerUUIDString(String bankName) {
        if (exists(bankName)) {
            return getConfig(bankName).getString("owner");
        } else return null;
    }
    /**
     * get owner uuid
     * @param bankName string
     * @return uuid if bank exists else null
     * @since many moons ago
     */
    public UUID getOwnerUUID(String bankName) {
        if (exists(bankName)) {
            return UUID.fromString(getOwnerUUIDString(bankName));
        } else return null;
    }
    /**
     * get owner offlinePlayer
     * @param bankName string
     * @return offlinePlayer if bank exists else null
     * @since many moons ago
     */
    public OfflinePlayer getOwner(String bankName) {
        if (exists(bankName)) {
            return getInstance().getOfflinePlayer(getOwnerUUID(bankName));
        } else return null;
    }
    /**
     * is owner
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if target is bank owner else false
     * @since many moons ago
     */
    public boolean isOwner(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            return getOwner(bankName).equals(offlinePlayer);
        } else return false;
    }
    /**
     * get list offlinePlayer
     * @param bankName string
     * @return list offlinePlayer if exists else empty list
     * @since many moons ago
     */
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
    /**
     * get list uuid
     * @param bankName string
     * @return list uuid if exists else empty list
     * @since many moons ago
     */
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
    /**
     * get list uuid string
     * @param bankName string
     * @return list uuid string if exists else empty list
     * @since many moons ago
     */
    public List<String> getMembersString(String bankName) {
        if (exists(bankName)) {
            return getConfig(bankName).getStringList("members");
        } else return new ArrayList<>();
    }
    /**
     * is member
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if target is member else false
     * @since many moons ago
     */
    public boolean isMember(String bankName, OfflinePlayer offlinePlayer) {
        if (exists(bankName)) {
            return getMembers(bankName).contains(offlinePlayer);
        } else return false;
    }
    /**
     * add member, if target is not a member and bankName exists
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if target is added else false
     * @since many moons ago
     */
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
    /**
     * remove member, if target is a member and bankName exists
     * @param bankName string
     * @param offlinePlayer or player
     * @return true if target is removed else false
     * @since many moons ago
     */
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
    /**
     * get bank account
     * @param bankName string
     * @return double if bank exists else 0
     * @since many moons ago
     */
    public double get(String bankName) {
        if (exists(bankName)) {
            return getConfig(bankName).getDouble("account");
        } else return 0;
    }
    /**
     * if bankName has amount
     * @param bankName string
     * @return true if bankName has or over the amount else false
     * @since many moons ago
     */
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
            var result = amount + get(bankName);
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