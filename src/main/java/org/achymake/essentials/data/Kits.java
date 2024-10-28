package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Kits {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "kits.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public Set<String> getListed() {
        return config.getKeys(false);
    }
    public int getCooldown(String kitName) {
        return config.getInt(kitName + ".cooldown");
    }
    public boolean hasPrice(String kitName) {
        return config.isDouble(kitName + ".price");
    }
    public double getPrice(String kitName) {
        return config.getDouble(kitName + ".price");
    }
    public List<ItemStack> getKit(String kitName) {
        var itemStacks = new ArrayList<ItemStack>();
        config.getConfigurationSection(kitName + ".items").getKeys(false).forEach(sections -> {
            var section = kitName + ".items." + sections;
            var type = config.getString(section + ".type");
            var amount = config.getInt(section + ".amount");
            var itemStack = getMaterialHandler().getItem(type, amount);
            if (itemStack != null) {
                var meta = itemStack.getItemMeta();
                var name = config.getString(section + ".name");
                if (name != null) {
                    meta.setDisplayName(getInstance().getMessage().addColor(name));
                }
                if (config.isList(section + ".lore")) {
                    var lore = new ArrayList<String>();
                    for (var listedLore : config.getStringList(section + ".lore")) {
                        lore.add(getInstance().getMessage().addColor(listedLore));
                    }
                    meta.setLore(lore);
                    lore.clear();
                }
                if (config.isConfigurationSection(section + ".enchantments")) {
                    config.getConfigurationSection(section + ".enchantments").getKeys(false).forEach(enchantmentString -> {
                        var enchantment = Enchantment.getByName(enchantmentString.toUpperCase());
                        if (enchantment != null) {
                            var lvl = config.getInt(section + ".enchantments." + enchantmentString);
                            if (lvl > 0) {
                                meta.addEnchant(enchantment, lvl, true);
                            }
                        }
                    });
                }
                itemStack.setItemMeta(meta);
                itemStacks.add(itemStack);
            }
        });
        return itemStacks;
    }
    private void setup() {
        var lore = new ArrayList<String>();
        lore.add("&9Kit");
        lore.add("&7- &6Starter");
        config.addDefault("starter.price", 75.0);
        config.addDefault("starter.cooldown", 3600);
        config.addDefault("starter.items.sword.type", "STONE_SWORD");
        config.addDefault("starter.items.sword.amount", 1);
        config.addDefault("starter.items.sword.name", "&6Stone Sword");
        config.addDefault("starter.items.sword.lore", lore);
        config.addDefault("starter.items.sword.enchantments.unbreaking", 1);
        config.addDefault("starter.items.pickaxe.type", "STONE_PICKAXE");
        config.addDefault("starter.items.pickaxe.amount", 1);
        config.addDefault("starter.items.pickaxe.name", "&6Stone Pickaxe");
        config.addDefault("starter.items.pickaxe.lore", lore);
        config.addDefault("starter.items.pickaxe.enchantments.unbreaking", 1);
        config.addDefault("starter.items.axe.type", "STONE_AXE");
        config.addDefault("starter.items.axe.amount", 1);
        config.addDefault("starter.items.axe.name", "&6Stone Axe");
        config.addDefault("starter.items.axe.lore", lore);
        config.addDefault("starter.items.axe.enchantments.unbreaking", 1);
        config.addDefault("starter.items.shovel.type", "STONE_SHOVEL");
        config.addDefault("starter.items.shovel.amount", 1);
        config.addDefault("starter.items.shovel.name", "&6Stone Shovel");
        config.addDefault("starter.items.shovel.lore", lore);
        config.addDefault("starter.items.shovel.enchantments.unbreaking", 1);
        config.addDefault("starter.items.steak.type", "COOKED_BEEF");
        config.addDefault("starter.items.steak.amount", 16);
        config.addDefault("food.price", 25.0);
        config.addDefault("food.cooldown", 1800);
        config.addDefault("food.items.steak.type", "COOKED_BEEF");
        config.addDefault("food.item.steak.amount", 16);
        config.options().copyDefaults(true);
        save();
    }
    public void reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else setup();
    }
    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
}
