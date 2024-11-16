package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
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
    public boolean setDifficulty(String difficultyString) {
        if (difficultyString.equalsIgnoreCase("peaceful")) {
            getWorld().setDifficulty(Difficulty.PEACEFUL);
            return true;
        } else if (difficultyString.equalsIgnoreCase("easy")) {
            getWorld().setDifficulty(Difficulty.EASY);
            return true;
        } else if (difficultyString.equalsIgnoreCase("normal")) {
            getWorld().setDifficulty(Difficulty.NORMAL);
            return true;
        } else if (difficultyString.equalsIgnoreCase("hard")) {
            getWorld().setDifficulty(Difficulty.HARD);
            return true;
        } else return false;
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
    public void setMorning() {
        setTime(0);
    }
    public void setDay() {
        setTime(1000);
    }
    public void setNoon() {
        setTime(6000);
    }
    public void setNight() {
        setTime(13000);
    }
    public void setMidnight() {
        setTime(18000);
    }
    public void setTime(long value) {
        getWorld().setTime(value);
    }
    public void addTime(long value) {
        setTime(getWorld().getTime() + value);
    }
    public void removeTime(long value) {
        setTime(getWorld().getTime() - value);
    }
}