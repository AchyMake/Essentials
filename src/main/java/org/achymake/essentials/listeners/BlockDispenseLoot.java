package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseLootEvent;
import org.bukkit.plugin.PluginManager;

public class BlockDispenseLoot implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockDispenseLoot() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDispenseLoot(BlockDispenseLootEvent event) {
        if (!getConfig().getBoolean("physics.disable-redstone")) {
            var player = event.getPlayer();
            if (player == null)return;
            if (!isDisabled(player))return;
            event.setCancelled(true);
        } else event.setCancelled(true);
    }
    private boolean isDisabled(Player player) {
        return getUserdata().isDisabled(player) || getUserdata().isVanished(player);
    }
}