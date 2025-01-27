package org.achymake.essentials.providers;

import org.achymake.essentials.Essentials;
import org.achymake.vaultextra.services.EntityHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.ServicePriority;

public class EntityProvider implements EntityHandler {
    private final Essentials ess;
    public EntityProvider(Essentials ess) {
        this.ess = ess;
    }
    public void register() {
        ess.getServer().getServicesManager().register(EntityHandler.class, this, ess, ServicePriority.Normal);
    }
    @Override
    public boolean isEnable() {
        return ess.isEnabled();
    }
    @Override
    public String getName() {
        return ess.name();
    }
    @Override
    public boolean isFriendly(Entity entity) {
        return ess.getEntityHandler().isFriendly(entity.getType());
    }
    @Override
    public boolean isHostile(Entity entity) {
        return ess.getEntityHandler().isHostile(entity.getType());
    }
    @Override
    public boolean isTamed(Entity entity) {
        return ess.getEntityHandler().isTamed(entity);
    }
    @Override
    public OfflinePlayer getOwner(Entity entity) {
        return ess.getEntityHandler().getOwner(entity);
    }
    @Override
    public AttributeInstance getAttribute(Entity entity, Attribute attribute) {
        return ess.getEntityHandler().getAttribute(entity, attribute);
    }
    @Override
    public void setAttribute(Entity entity, Attribute attribute, double value) {
        ess.getEntityHandler().setAttribute(entity, attribute, value);
    }
}