package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
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
    private final File file = new File(getInstance().getDataFolder(), "warps.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public Set<String> getListed() {
        return config.getKeys(false);
    }
    public boolean isListed(String warpName) {
        return getListed().contains(warpName);
    }
    public Location getLocation(String warpName) {
        var worldName = config.getString(warpName + ".world");
        if (worldName != null) {
            var world = getInstance().getServer().getWorld(worldName);
            if (world != null) {
                var x = config.getDouble(warpName + ".x");
                var y = config.getDouble(warpName + ".y");
                var z = config.getDouble(warpName + ".z");
                var yaw = config.getLong(warpName + ".yaw");
                var pitch = config.getLong(warpName + ".pitch");
                return new Location(world, x, y, z, yaw, pitch);
            } else return null;
        } else return null;
    }
    public void setLocation(String warpName, Location location) {
        if (location == null) {
            config.set(warpName, null);
            save();
        } else {
            var world = location.getWorld();
            if (world != null) {
                var worldName = location.getWorld().getName();
                var x = location.getX();
                var y = location.getY();
                var z = location.getZ();
                var yaw = location.getYaw();
                var pitch = location.getPitch();
                config.set(warpName + ".world", worldName);
                config.set(warpName + ".x", x);
                config.set(warpName + ".y", y);
                config.set(warpName + ".z", z);
                config.set(warpName + ".yaw", yaw);
                config.set(warpName + ".pitch", pitch);
                save();
            }
        }
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