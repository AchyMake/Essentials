package org.achymake.essentials.providers;

import org.achymake.essentials.Essentials;
import org.achymake.pvpapi.PVPAPI;
import org.achymake.pvpapi.handlers.PVPHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

public class PVPProvider implements PVPHandler {
    private final Essentials ess;
    public PVPProvider(Essentials ess) {
        this.ess = ess;
    }
    public void register() {
        ess.getServer().getServicesManager().register(PVPHandler.class, this, ess, ServicePriority.Normal);
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
    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().isPVP(offlinePlayer);
    }
    @Override
    public boolean setPVP(OfflinePlayer offlinePlayer, boolean value) {
        return ess.getUserdata().setBoolean(offlinePlayer, "settings.pvp", value);
    }
}