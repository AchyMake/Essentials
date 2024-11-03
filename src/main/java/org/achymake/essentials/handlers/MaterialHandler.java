package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MaterialHandler {
    public Material get(String materialName) {
        return Material.valueOf(materialName.toUpperCase());
    }
    public Enchantment getEnchantment(String enchantmentName) {
        return Enchantment.getByName(enchantmentName.toUpperCase());
    }
    public ArrayList<Enchantment> getEnchantments() {
        return new ArrayList<Enchantment>(Arrays.asList(Enchantment.values()));
    }
    public PersistentDataContainer getData(ItemMeta itemMeta) {
        return itemMeta.getPersistentDataContainer();
    }
    public NamespacedKey getKey(String key) {
        return new NamespacedKey(Essentials.getInstance(), key);
    }
    public ItemStack getItemStack(String materialName, int amount) {
        var material = get(materialName.toUpperCase());
        if (material != null) {
            return new ItemStack(get(materialName.toUpperCase()), amount);
        } else return null;
    }
    public void giveItemStack(Player player, ItemStack itemStack) {
        if (itemStack != null) {
            if (Arrays.asList(player.getInventory().getStorageContents()).contains(null)) {
                player.getInventory().addItem(itemStack);
            } else dropItem(player.getLocation(), itemStack);
        }
    }
    public void giveItemStacks(Player player, Collection<ItemStack> itemStacks) {
        for (var itemStack : itemStacks) {
            giveItemStack(player, itemStack);
        }
    }
    public Item dropItem(Location location, ItemStack itemStack) {
        return location.getWorld().dropItem(location, itemStack);
    }
    public ItemStack getPlayerHead(OfflinePlayer offlinePlayer, int amount) {
        var itemStack = getItemStack("player_head", amount);
        var skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(offlinePlayer);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
    public boolean repair(ItemStack itemStack) {
        var damageable = (Damageable) itemStack.getItemMeta();
        if (damageable.hasDamage()) {
            damageable.setDamage(0);
            itemStack.setItemMeta(damageable);
            return true;
        } else return false;
    }
    public boolean isAir(ItemStack itemStack) {
        return itemStack == null || itemStack.getType().equals(get("air"));
    }
    public boolean isAir(Block block) {
        return block.getType().equals(get("air"));
    }
}