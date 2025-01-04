package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.PluginManager;

public class EntityShootBow implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityShootBow() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!getEntityHandler().isCreatureSpawnDisabled(event.getProjectile().getType()))return;
        if (event.getEntity() instanceof Player player) {
            var consumable = event.getConsumable();
            if (consumable == null)return;
            player.getInventory().addItem(consumable);
        }
    }
}