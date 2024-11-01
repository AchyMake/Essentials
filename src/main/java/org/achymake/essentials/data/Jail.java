package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Jail {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "jail.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public File getFile() {
        return file;
    }
    public FileConfiguration getConfig() {
        return config;
    }
    public Location getLocation() {
        var world = getInstance().getWorlds().get(config.getString("world"));
        if (world != null) {
            var x = config.getDouble("x");
            var y = config.getDouble("y");
            var z = config.getDouble("z");
            var yaw = config.getLong("yaw");
            var pitch = config.getLong("pitch");
            return new Location(world, x, y, z, yaw, pitch);
        } else return null;
    }
    public void setLocation(Location location) {
        var world = location.getWorld();
        if (world != null) {
            config.set("world", location.getWorld().getName());
            config.set("x", location.getX());
            config.set("y", location.getY());
            config.set("z", location.getZ());
            config.set("yaw", location.getYaw());
            config.set("pitch", location.getPitch());
            try {
                config.save(file);
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
            }
        }
    }
    private void setup() {
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