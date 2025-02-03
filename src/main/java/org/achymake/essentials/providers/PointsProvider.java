package org.achymake.essentials.providers;

import org.achymake.essentials.Essentials;
import org.achymake.vaultextra.services.PointsService;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

public class PointsProvider implements PointsService {
    private final Essentials ess;
    public PointsProvider(Essentials ess) {
        this.ess = ess;
    }
    public void register() {
        ess.getServer().getServicesManager().register(PointsService.class, this, ess, ServicePriority.Normal);
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
    public int get(OfflinePlayer offlinePlayer) {
        return ess.getPointsHandler().get(offlinePlayer);
    }
    @Override
    public boolean has(OfflinePlayer offlinePlayer, int amount) {
        return get(offlinePlayer) >= amount;
    }
    @Override
    public boolean setPoints(OfflinePlayer offlinePlayer, int amount) {
        return ess.getPointsHandler().set(offlinePlayer, amount);
    }
    @Override
    public boolean addPoints(OfflinePlayer offlinePlayer, int amount) {
        return ess.getPointsHandler().add(offlinePlayer, amount);
    }
    @Override
    public boolean removePoints(OfflinePlayer offlinePlayer, int amount) {
        return ess.getPointsHandler().remove(offlinePlayer, amount);
    }
}