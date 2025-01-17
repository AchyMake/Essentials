package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.bukkit.plugin.PluginManager;

public class BlockReceiveGame implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockReceiveGame() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockReceiveGame(BlockReceiveGameEvent event) {
        if (!event.getBlock().getType().equals(getMaterials().get("sculk_sensor")))return;
        if (!getConfig().getBoolean("physics.disable-sculk-sensor")) {
            if (event.getEntity() instanceof Player player) {
                if (!isDisabled(player))return;
                event.setCancelled(true);
            }
        } else event.setCancelled(true);
    }
    private boolean isDisabled(Player player) {
        return getUserdata().isDisabled(player) || getUserdata().isVanished(player);
    }
}