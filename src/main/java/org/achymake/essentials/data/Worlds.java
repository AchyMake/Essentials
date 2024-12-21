package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Worlds {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    public List<String> getListed() {
        var listed = new ArrayList<String>();
        getInstance().getServer().getWorlds().forEach(world -> listed.add(world.getName()));
        return listed;
    }
    public World get(String worldName) {
        return getInstance().getServer().getWorld(worldName);
    }
    public Block highestRandomBlock(String worldName, int spread) {
        var random = new Random();
        return get(worldName).getHighestBlockAt(random.nextInt(0, spread), random.nextInt(0, spread));
    }
}