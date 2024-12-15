package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerLevelChange() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        var player = event.getPlayer();
        if (!player.hasPermission("essentials.event.level_change"))return;
        var level = player.getLevel();
        var oldLevel = event.getOldLevel();
        if (level > oldLevel) {
            if (getConfig().getBoolean("levels.level-up.particle.enable")) {
                var particle = Particle.valueOf(getConfig().getString("levels.level-up.particle.type"));
                var count = getConfig().getInt("levels.level-up.particle.count");
                var offsetX = getConfig().getDouble("levels.level-up.particle.offsetX");
                var offsetY = getConfig().getDouble("levels.level-up.particle.offsetY");
                var offsetZ = getConfig().getDouble("levels.level-up.particle.offsetZ");
                player.getWorld().spawnParticle(particle, player.getLocation(), count, offsetX, offsetY, offsetZ, 0.0);
            }
            if (getConfig().getBoolean("levels.level-up.sound.enable")) {
                var sound = Sound.valueOf(getConfig().getString("levels.level-up.sound.type"));
                var volume = (float) getConfig().getDouble("levels.level-up.sound.volume");
                var pitch = (float) getConfig().getDouble("levels.level-up.sound.pitch");
                player.getWorld().playSound(player, sound, volume, pitch);
            }
        } else if (oldLevel > level) {
            if (getConfig().getBoolean("levels.level-down.particle.enable")) {
                var particle = Particle.valueOf(getConfig().getString("levels.level-down.particle.type"));
                var count =  getConfig().getInt("levels.level-down.particle.count");
                var offsetX = getConfig().getDouble("levels.level-down.particle.offsetX");
                var offsetY = getConfig().getDouble("levels.level-down.particle.offsetY");
                var offsetZ = getConfig().getDouble("levels.level-down.particle.offsetZ");
                player.getWorld().spawnParticle(particle, player.getLocation(), count, offsetX, offsetY, offsetZ, 0.0);
            }
            if (getConfig().getBoolean("levels.level-down.sound.enable")) {
                var sound = Sound.valueOf(getConfig().getString("levels.level-down.sound.type"));
                var volume = (float) getConfig().getDouble("levels.level-down.sound.volume");
                var pitch = (float) getConfig().getDouble("levels.level-down.sound.pitch");
                player.getWorld().playSound(player, sound, volume, pitch);
            }
        }
    }
}