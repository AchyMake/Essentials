package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
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

public class MaterialHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    public Material get(String materialName) {
        return Material.valueOf(materialName.toUpperCase());
    }
    public Enchantment getEnchantment(String enchantmentName) {
        return Enchantment.getByName(enchantmentName.toUpperCase());
    }
    public void setEnchantment(ItemStack itemStack, String enchantmentName, int amount) {
        var meta = itemStack.getItemMeta();
        if (amount > 0) {
            meta.addEnchant(getEnchantment(enchantmentName), amount, true);
            itemStack.setItemMeta(meta);
        } else itemStack.removeEnchantment(getEnchantment(enchantmentName));
    }
    public boolean hasEnchantment(ItemStack itemStack, String enchantmentName) {
        return itemStack.getItemMeta().hasEnchant(getEnchantment(enchantmentName));
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
        getData(itemMeta).set(getKey("entity_type"), PersistentDataType.STRING, entityType.toUpperCase());
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        spawner.setItemMeta(itemMeta);
        return spawner;
    }
    public void updateSpawner(Block blockPlaced, ItemStack heldItem) {
        var container = getData(heldItem.getItemMeta());
        if (container.has(getKey("entity_type"), PersistentDataType.STRING)) {
            var creatureSpawner = (CreatureSpawner) blockPlaced.getState();
            creatureSpawner.setSpawnedType(EntityType.valueOf(container.get(getKey("entity_type"), PersistentDataType.STRING)));
            creatureSpawner.update();
        }
    }
    public Item dropSpawner(Block block) {
        var creatureSpawner = (CreatureSpawner) block.getState();
        var itemStack = getItemStack("spawner", 1);
        if (creatureSpawner.getSpawnedType() != null) {
            return dropItem(block.getLocation().add(0.5, 0.3, 0.5), getSpawner(creatureSpawner.getSpawnedType().toString(), 1));
        } else return dropItem(block.getLocation().add(0.5, 0.3, 0.5), itemStack);
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