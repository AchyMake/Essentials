package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.generator.WorldInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Worlds {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private WorldCreator getCreator(String worldName) {
        return new WorldCreator(worldName);
    }
    public List<World> getListed() {
        return new ArrayList<World>(getInstance().getServer().getWorlds());
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
            for (var file : worlds.listFiles()) {
                var worldName = file.getName().replace(".yml", "");
                if (get(worldName) == null) {
                    if (getFolder(worldName).exists()) {
                        var info = add(worldName);
                        getInstance().sendInfo(info.getName() + " has been created with the following:");
                        getInstance().sendInfo("environment: " + info.getEnvironment().name());
                        getInstance().sendInfo("seed: " + info.getSeed());
                    } else {
                        file.delete();
                        getInstance().sendWarning(worldName + " does not exist " + file.getName() + " has been deleted");
                    }
                }
            }
        }
    }
    public WorldInfo add(String worldName) {
        var creator = getCreator(worldName);
        return creator.createWorld();
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
    public Block highestRandomBlock(String worldName, int spread) {
        var random = new Random();
        return get(worldName).getHighestBlockAt(random.nextInt(0, spread), random.nextInt(0, spread));
    }
}