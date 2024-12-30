package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private final File file = new File(getInstance().getDataFolder(), "kits.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public Set<String> getListed() {
        return config.getKeys(false);
    }
    public boolean isListed(String kitName) {
        return getListed().contains(kitName);
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
    public void sendKits(Player player) {
        if (!getListed().isEmpty()) {
            player.sendMessage(getMessage().get("commands.kit.title"));
            getListed().forEach(kits -> {
                if (player.hasPermission("essentials.command.kit." + kits)) {
                    player.sendMessage(getMessage().get("commands.kit.listed", kits));
                }
            });
        } else player.sendMessage(getMessage().get("commands.kit.empty"));
    }
    public List<ItemStack> get(String kitName) {
        var itemStacks = new ArrayList<ItemStack>();
        config.getConfigurationSection(kitName + ".items").getKeys(false).forEach(sections -> {
            var section = kitName + ".items." + sections;
            if (config.isString(section + ".type")) {
                var itemStack = getMaterials().getItemStack(config.getString(section + ".type"), 1);
                if (itemStack != null) {
                    if (config.getInt(section + ".amount") > 0) {
                        itemStack.setAmount(config.getInt(section + ".amount"));
                    }
                    var meta = itemStack.getItemMeta();
                    var name = config.getString(section + ".name");
                    if (name != null) {
                        meta.setDisplayName(getMessage().addColor(name));
                    }
                    if (config.isList(section + ".lore")) {
                        var lore = new ArrayList<String>();
                        for (var listedLore : config.getStringList(section + ".lore")) {
                            lore.add(getMessage().addColor(listedLore));
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
        config.addDefault("starter.items.sword.type", "stone_sword");
        config.addDefault("starter.items.sword.amount", 1);
        config.addDefault("starter.items.sword.name", "&6Stone Sword");
        config.addDefault("starter.items.sword.lore", lore);
        config.addDefault("starter.items.sword.enchantments.unbreaking", 1);
        config.addDefault("starter.items.pickaxe.type", "stone_pickaxe");
        config.addDefault("starter.items.pickaxe.amount", 1);
        config.addDefault("starter.items.pickaxe.name", "&6Stone Pickaxe");
        config.addDefault("starter.items.pickaxe.lore", lore);
        config.addDefault("starter.items.pickaxe.enchantments.unbreaking", 1);
        config.addDefault("starter.items.axe.type", "stone_axe");
        config.addDefault("starter.items.axe.amount", 1);
        config.addDefault("starter.items.axe.name", "&6Stone Axe");
        config.addDefault("starter.items.axe.lore", lore);
        config.addDefault("starter.items.axe.enchantments.unbreaking", 1);
        config.addDefault("starter.items.shovel.type", "stone_shovel");
        config.addDefault("starter.items.shovel.amount", 1);
        config.addDefault("starter.items.shovel.name", "&6Stone Shovel");
        config.addDefault("starter.items.shovel.lore", lore);
        config.addDefault("starter.items.shovel.enchantments.unbreaking", 1);
        config.addDefault("starter.items.steak.type", "cooked_beef");
        config.addDefault("starter.items.steak.amount", 16);
        config.addDefault("food.price", 25.0);
        config.addDefault("food.cooldown", 1800);
        config.addDefault("food.items.steak.type", "cooked_beef");
        config.addDefault("food.items.steak.amount", 16);
        config.options().copyDefaults(true);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else setup();
    }
}