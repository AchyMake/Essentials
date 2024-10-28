package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.World;

public class WorldHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    public World getWorld(String worldName) {
        return getInstance().getServer().getWorld(worldName);
    }
}