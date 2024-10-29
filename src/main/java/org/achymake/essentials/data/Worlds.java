package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.WorldInfo;

import java.io.File;
import java.util.Random;

public class Worlds {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private WorldCreator getCreator(String worldName) {
        return new WorldCreator(worldName);
    }
    public World get(String worldName) {
        return getInstance().getServer().getWorld(worldName);
    }
    public File getFolder(String worldName) {
        return new File(getInstance().getServer().getWorldContainer(), worldName);
    }
    public void setup() {
        var worlds = new File(getInstance().getDataFolder(), "worlds");
        if (worlds.exists() && worlds.isDirectory()) {
            for (var files : worlds.listFiles()) {
                var worldName = files.getName().replace(".yml", "");
                if (get(worldName) == null) {
                    if (getFolder(worldName).exists()) {
                        var config = YamlConfiguration.loadConfiguration(files);
                        var info = create(worldName, World.Environment.valueOf(config.getString("environment")), config.getLong("seed"));
                        getInstance().sendInfo(info.getName() + " has been created with the following:");
                        getInstance().sendInfo("environment: " + info.getEnvironment().name());
                        getInstance().sendInfo("seed: " + info.getSeed());
                    } else {
                        files.delete();
                        getInstance().sendWarning(worldName + " does not exist " + files.getName() + " has been deleted");
                    }
                }
            }
        }
    }
    public WorldInfo create(String worldName, World.Environment environment) {
        var creator = getCreator(worldName);
        creator.environment(environment);
        return creator.createWorld();
    }
    public WorldInfo create(String worldName, World.Environment environment, long seed) {
        var creator = getCreator(worldName);
        creator.environment(environment);
        creator.seed(seed);
        return creator.createWorld();
    }
    public WorldInfo createRandom(String worldName, World.Environment environment) {
        var creator = getCreator(worldName);
        creator.environment(environment);
        creator.seed(new Random().nextLong());
        return creator.createWorld();
    }
}