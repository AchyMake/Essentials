package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Warps {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "warps.yml");
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
     * is warp listed
     * @param warpName string
     * @return true if warpName is listed else false
     * @since many moons ago
     */
    public boolean isListed(String warpName) {
        return getListed().contains(warpName);
    }
    /**
     * get location
     * @param warpName string
     * @return location if warpName exists else null
     * @since many moons ago
     */
    public Location getLocation(String warpName) {
        if (config.isConfigurationSection(warpName)) {
            if (config.isString(warpName + ".world")) {
                var world = getWorldHandler().get(config.getString(warpName + ".world"));
                if (world != null) {
                    var x = config.getDouble(warpName + ".x");
                    var y = config.getDouble(warpName + ".y");
                    var z = config.getDouble(warpName + ".z");
                    var yaw = config.getLong(warpName + ".yaw");
                    var pitch = config.getLong(warpName + ".pitch");
                    return new Location(world, x, y, z, yaw, pitch);
                } else return null;
            } else return null;
        } else return null;
    }
    /**
     * set location or remove
     * @param warpName string
     * @param location location or null to remove
     * @since many moons ago
     */
    public void setLocation(String warpName, Location location) {
        if (location != null) {
            config.set(warpName + ".world", location.getWorld().getName());
            config.set(warpName + ".x", location.getX());
            config.set(warpName + ".y", location.getY());
            config.set(warpName + ".z", location.getZ());
            config.set(warpName + ".yaw", location.getYaw());
            config.set(warpName + ".pitch", location.getPitch());
            try {
                config.save(file);
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
            }
        } else {
            config.set(warpName, null);
            try {
                config.save(file);
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
            }
        }
    }
    /**
     * setup
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
     * reload if exists else setup
     * @since many moons ago
     */
    public void reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else setup();
    }
}