package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public record WorldHandler(World getWorld) {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private File getFile() {
        return new File(getInstance().getDataFolder(), "worlds/" + getName() + ".yml");
    }
    public File getFolder() {
        return new File(getInstance().getServer().getWorldContainer(), getName());
    }
    public boolean exists() {
        return getFile().exists();
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public void setSpawn(Location location) {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public Location getSpawn() {
        if (getConfig().isConfigurationSection("spawn")) {
            var x = getConfig().getDouble("spawn.x");
            var y = getConfig().getDouble("spawn.y");
            var z = getConfig().getDouble("spawn.z");
            var yaw = getConfig().getLong("spawn.yaw");
            var pitch = getConfig().getLong("spawn.pitch");
            return new Location(getWorld(), x, y, z, yaw, pitch);
        } else return getWorld().getSpawnLocation().add(0.5, 0.0, 0.5);
    }
    public boolean getPVP() {
        return getWorld().getPVP();
    }
    public void setPVP(boolean value) {
        getWorld().setPVP(value);
    }
    public void setDifficulty(Difficulty difficulty) {
        getWorld().setDifficulty(difficulty);
    }
    private void setup() {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("name", getName());
        config.set("seed", getSeed());
        config.set("environment", getEnvironment().toString());
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void reload() {
        if (exists()) {
            var file = getFile();
            var config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getInstance().sendWarning(e.getMessage());
            }
        } else setup();
    }
    public String getDisplayName() {
        return getConfig().getString("name");
    }
    public String getName() {
        return getWorld().getName();
    }
    public long getSeed() {
        return getWorld().getSeed();
    }
    public World.Environment getEnvironment() {
        return getWorld().getEnvironment();
    }
    public Chunk getChunk(long chunkKey) {
        var x = (int) chunkKey;
        var z = (int) (chunkKey >> 32);
        return getWorld().getChunkAt(x, z);
    }
    public void remove() {
        var file = getFile();
        if (file.exists()) {
            getFile().delete();
        }
        getInstance().getServer().unloadWorld(getWorld(), true);
    }
    @Override
    public World getWorld() {
        return getWorld;
    }
}