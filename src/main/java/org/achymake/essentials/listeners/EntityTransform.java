package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.plugin.PluginManager;

public class EntityTransform implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityTransform() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTransform(EntityTransformEvent event) {
        var transformReason = event.getTransformReason();
        var before = event.getEntity();
        var after = event.getTransformedEntity();
        if (transformReason.equals(EntityTransformEvent.TransformReason.FROZEN)) {
            if (before instanceof Skeleton skeleton && after instanceof Stray stray) {
                getEntityHandler().copyStats(skeleton, stray);
            } else if (before instanceof Stray stray && after instanceof Skeleton skeleton) {
                getEntityHandler().copyStats(stray, skeleton);
            }
        } else if (transformReason.equals(EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED)) {
            if (before instanceof Piglin piglin && after instanceof PigZombie pigZombie) {
                getEntityHandler().copyStats(piglin, pigZombie);
            }
        } else if (transformReason.equals(EntityTransformEvent.TransformReason.SHEARED)) {
            if (before instanceof Sheep sheep && after instanceof Sheep newSheep) {
                getEntityHandler().copyStats(sheep, newSheep);
            }
        } else if (transformReason.equals(EntityTransformEvent.TransformReason.DROWNED)) {
            if (before instanceof Drowned drowned && after instanceof Zombie zombie) {
                getEntityHandler().copyStats(drowned, zombie);
            } else if (before instanceof Zombie zombie && after instanceof Drowned drowned) {
                getEntityHandler().copyStats(zombie, drowned);
            }
        } else if (transformReason.equals(EntityTransformEvent.TransformReason.INFECTION)) {
            if (before instanceof Villager villager && after instanceof ZombieVillager zombieVillager) {
                getEntityHandler().copyStats(villager, zombieVillager);
            } else if (before instanceof Villager villager && after instanceof Zombie zombie) {
                getEntityHandler().copyStats(villager, zombie);
            }
        } else if (transformReason.equals(EntityTransformEvent.TransformReason.CURED)) {
            if (before instanceof ZombieVillager zombieVillager && after instanceof Villager villager) {
                getEntityHandler().copyStats(zombieVillager, villager);
            }
        }
    }
}