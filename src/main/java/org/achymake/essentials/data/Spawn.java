package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Spawn {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "spawn.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public Location getLocation() {
        var worldName = config.getString("world");
        if (worldName != null) {
            var world = getInstance().getServer().getWorld(worldName);
            if (world != null) {
                var x = config.getDouble("x");
                var y = config.getDouble("y");
                var z = config.getDouble("z");
                var yaw = config.getLong("yaw");
                var pitch = config.getLong("pitch");
                return new Location(world, x, y, z, yaw, pitch);
            } else return null;
        } else return null;
    }
    public void setLocation(Location location) {
        var world = location.getWorld();
        if (world != null) {
            var worldName = location.getWorld().getName();
            var x = location.getX();
            var y = location.getY();
            var z = location.getZ();
            var yaw = location.getYaw();
            var pitch = location.getPitch();
            config.set("world", worldName);
            config.set("x", x);
            config.set("y", y);
            config.set("z", z);
            config.set("yaw", yaw);
            config.set("pitch", pitch);
            save();
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