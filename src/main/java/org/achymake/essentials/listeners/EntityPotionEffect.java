package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;

public class EntityPotionEffect implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityPotionEffect() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_DRINK) || event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_SPLASH)) {
                if (!player.hasPermission("essentials.event.potion.multiply"))return;
                var oldEffect = event.getOldEffect();
                if (oldEffect == null)return;
                var newEffect = event.getNewEffect();
                if (newEffect == null)return;
                if (newEffect.getType() != oldEffect.getType())return;
                if (newEffect.getAmplifier() != oldEffect.getAmplifier())return;
                event.setCancelled(true);
                player.addPotionEffect(new PotionEffect(newEffect.getType(),oldEffect.getDuration() + newEffect.getDuration(),newEffect.getAmplifier()));
            }
        }
    }
}