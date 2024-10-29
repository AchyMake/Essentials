package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.PluginManager;

public class EntityTargetLivingEntity implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private EntityHandler getEntityHandler(Entity getEntity) {
        return getInstance().getEntityHandler(getEntity);
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityTargetLivingEntity() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() == null)return;
        var entity = event.getEntity();
        var target = event.getTarget();
        if (entity instanceof Player)return;
        if (target instanceof Player player) {
            if (!getUserdata(player).isVanished())return;
            event.setCancelled(true);
        } else {
            var entityHandler = getEntityHandler(entity);
            if (!entityHandler.exists())return;
            if (!entityHandler.disableTarget(target))return;
            event.setCancelled(true);
        }
    }
}