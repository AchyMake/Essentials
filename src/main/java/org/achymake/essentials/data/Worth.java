package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Worth {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "worth.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public Set<String> getListed() {
        return config.getKeys(false);
    }
    public boolean isListed(Material material) {
        return getListed().contains(material.toString());
    }
    public double get(Material material) {
        return config.getDouble(material.toString());
    }
    public void setWorth(ItemStack itemStack, double value) {
        if (value > 0) {
            config.set(itemStack.getType().toString(), value);
        } else config.set(itemStack.getType().toString(), null);
        save();
    }
    private void setup() {
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