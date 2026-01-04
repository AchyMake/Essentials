package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerLevelChange implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerLevelChange() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        var player = event.getPlayer();
        if (!player.hasPermission("essentials.event.level_change"))return;
        var level = player.getLevel();
        var oldLevel = event.getOldLevel();
        if (level > oldLevel) {
            if (getConfig().getBoolean("levels.level-up.particle.enable")) {
                var particleType = getConfig().getString("levels.level-up.particle.type");
                if (particleType != null) {
                    var count = getConfig().getInt("levels.level-up.particle.count");
                    var offsetX = getConfig().getDouble("levels.level-up.particle.offsetX");
                    var offsetY = getConfig().getDouble("levels.level-up.particle.offsetY");
                    var offsetZ = getConfig().getDouble("levels.level-up.particle.offsetZ");
                    getWorldHandler().spawnParticle(player.getLocation(), particleType, count, offsetX, offsetY, offsetZ);
                }
            }
            if (getConfig().getBoolean("levels.level-up.sound.enable")) {
                var soundType = getConfig().getString("levels.level-up.sound.type");
                if (soundType != null) {
                    var volume = getConfig().getDouble("levels.level-up.sound.volume");
                    var pitch = getConfig().getDouble("levels.level-up.sound.pitch");
                    getWorldHandler().playSound(player.getLocation(), soundType, volume, pitch);
                }
            }
        } else if (oldLevel > level) {
            if (getConfig().getBoolean("levels.level-down.particle.enable")) {
                var particleType = getConfig().getString("levels.level-down.particle.type");
                if (particleType != null) {
                    var count =  getConfig().getInt("levels.level-down.particle.count");
                    var offsetX = getConfig().getDouble("levels.level-down.particle.offsetX");
                    var offsetY = getConfig().getDouble("levels.level-down.particle.offsetY");
                    var offsetZ = getConfig().getDouble("levels.level-down.particle.offsetZ");
                    var location = player.getLocation().add(0.0, 0.7, 0.0);
                    getWorldHandler().spawnParticle(location, particleType, count, offsetX, offsetY, offsetZ);
                }
            }
            if (getConfig().getBoolean("levels.level-down.sound.enable")) {
                var soundType = getConfig().getString("levels.level-down.sound.type");
                if (soundType != null) {
                    var volume = getConfig().getDouble("levels.level-down.sound.volume");
                    var pitch = getConfig().getDouble("levels.level-down.sound.pitch");
                    getWorldHandler().playSound(player.getLocation(), soundType, volume, pitch);
                }
            }
        }
    }
}