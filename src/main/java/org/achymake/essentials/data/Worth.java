package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Worth {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "worth.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    /**
     * get listed
     * @return set string
     * @since many moons ago
     */
    public Set<String> getListed() {
        return config.getKeys(false);
    }
    /**
     * if item is listed
     * @param material material
     * @return true if material contains listed else false
     * @since many moons ago
     */
    public boolean isListed(Material material) {
        return getListed().contains(material.toString());
    }
    /**
     * get item worth
     * @param material material
     * @return double
     * @since many moons ago
     */
    public double get(Material material) {
        return config.getDouble(material.toString());
    }
    /**
     * set item worth
     * @param material material
     * @param value double
     * @since many moons ago
     */
    public void setWorth(Material material, double value) {
        if (value > 0) {
            config.set(material.toString(), value);
        } else config.set(material.toString(), null);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    /**
     * setup worth.yml
     * @since many moons ago
     */
    private void setup() {
        config.options().copyDefaults(true);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    /**
     * reload worth.yml if exists else setup
     * @since many moons ago
     */
    public void reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else setup();
    }
}