package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MaterialHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    /**
     * get material
     * @param materialName string
     * @return material
     * @since many moons ago
     */
    public Material get(String materialName) {
        return Material.valueOf(materialName.toUpperCase());
    }
    /**
     * get enchantment
     * @param enchantmentName string
     * @return enchantment
     * @since many moons ago
     */
    public Enchantment getEnchantment(String enchantmentName) {
        return Enchantment.getByName(enchantmentName.toUpperCase());
    }
    /**
     * set enchantment
     * @param itemStack itemStack
     * @param enchantmentName string
     * @param level integer
     * @since many moons ago
     */
    public void setEnchantment(ItemStack itemStack, String enchantmentName, int level) {
        if (level > 0) {
            itemStack.addUnsafeEnchantment(getEnchantment(enchantmentName), level);
        } else itemStack.removeEnchantment(getEnchantment(enchantmentName));
    }
    /**
     * has enchantment
     * @param itemStack itemStack
     * @param enchantmentName string
     * @return true if itemStack has enchantmentName else false
     * @since many moons ago
     */
    public boolean hasEnchantment(ItemStack itemStack, String enchantmentName) {
        return itemStack.getItemMeta().hasEnchant(getEnchantment(enchantmentName));
    }
    /**
     * get enchantments
     * @return list enchantments
     * @since many moons ago
     */
    public List<Enchantment> getEnchantments() {
        return new ArrayList<>(Arrays.asList(Enchantment.values()));
    }
    /**
     * get persistent data container of itemMeta
     * @param itemMeta itemMeta
     * @return persistentDataContainer
     * @since many moons ago
     */
    public PersistentDataContainer getData(ItemMeta itemMeta) {
        return itemMeta.getPersistentDataContainer();
    }
    /**
     * get new itemStack
     * @param materialName string
     * @param amount integer
     * @return itemStack if materialName is null returns null
     * @since many moons ago
     */
    public ItemStack getItemStack(String materialName, int amount) {
        var material = get(materialName.toUpperCase());
        if (material != null) {
            return new ItemStack(material, amount);
        } else return null;
    }
    /**
     * get copy of itemStack
     * @param itemStack itemStack
     * @return itemStack copy of itemStack if itemStack is null returns null
     * @since many moons ago
     */
    public ItemStack getItemStack(ItemStack itemStack, int amount) {
        if (itemStack != null) {
            var copy = new ItemStack(itemStack);
            copy.setAmount(amount);
            return copy;
        } else return null;
    }
    /**
     * give itemStack
     * @param player target
     * @param itemStack itemStack
     * @since many moons ago
     */
    public void giveItemStack(Player player, ItemStack itemStack) {
        if (itemStack != null) {
            if (Arrays.asList(player.getInventory().getStorageContents()).contains(null)) {
                player.getInventory().addItem(itemStack);
            } else getWorldHandler().dropItem(player.getLocation(), itemStack);
        }
    }
    /**
     * give itemStack
     * @param player target
     * @param itemStacks collection itemStacks
     * @since many moons ago
     */
    public void giveItemStacks(Player player, Collection<ItemStack> itemStacks) {
        for (var itemStack : itemStacks) {
            giveItemStack(player, itemStack);
        }
    }
    /**
     * get spawner
     * @param entityType string
     * @param amount integer
     * @since many moons ago
     */
    public ItemStack getSpawner(String entityType, int amount) {
        var spawner = getItemStack("spawner", amount);
        var itemMeta = spawner.getItemMeta();
        if (getInstance().getConfig().isList("spawner.lore")) {
            var listed = new ArrayList<String>();
            for(var string : getInstance().getConfig().getStringList("spawner.lore")) {
                listed.add(getInstance().getMessage().addColor(string));
            }
            itemMeta.setLore(listed);
        }
        if (getInstance().getConfig().isString("spawner.display")) {
            var name = getInstance().getMessage().addColor(getInstance().getConfig().getString("spawner.display"));
            itemMeta.setDisplayName(name.replaceAll("%entity_type%", getInstance().getMessage().toTitleCase(entityType.toUpperCase())));
        }
        getData(itemMeta).set(getInstance().getKey("entity_type"), PersistentDataType.STRING, entityType.toUpperCase());
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        spawner.setItemMeta(itemMeta);
        return spawner;
    }
    /**
     * get player head
     * @param offlinePlayer or player
     * @param amount integer
     * @since many moons ago
     */
    public ItemStack getPlayerHead(OfflinePlayer offlinePlayer, int amount) {
        var itemStack = getItemStack("player_head", amount);
        var skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(offlinePlayer);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
    /**
     * repair itemStack
     * @param itemStack itemStack
     * @return true if itemStack gets repaired else false
     * @since many moons ago
     */
    public boolean repair(ItemStack itemStack) {
        var damageable = (Damageable) itemStack.getItemMeta();
        if (damageable.hasDamage()) {
            damageable.setDamage(0);
            itemStack.setItemMeta(damageable);
            return true;
        } else return false;
    }
    /**
     * is air
     * @param itemStack itemStack
     * @return true if itemStack is null or air else false
     * @since many moons ago
     */
    public boolean isAir(ItemStack itemStack) {
        return itemStack == null || itemStack.getType().equals(get("air"));
    }
}