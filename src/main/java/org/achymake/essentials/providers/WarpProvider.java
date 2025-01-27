package org.achymake.essentials.providers;

import org.achymake.essentials.Essentials;
import org.achymake.vaultextra.services.WarpHandler;
import org.bukkit.Location;
import org.bukkit.plugin.ServicePriority;

import java.util.Set;

public class WarpProvider implements WarpHandler {
    private final Essentials ess;
    public WarpProvider(Essentials ess) {
        this.ess = ess;
    }
    public void register() {
        ess.getServer().getServicesManager().register(WarpHandler.class, this, ess, ServicePriority.Normal);
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
    public Set<String> getListed() {
        return ess.getWarps().getListed();
    }
    @Override
    public boolean isListed(String warpName) {
        return ess.getWarps().isListed(warpName);
    }
    @Override
    public Location get(String warpName) {
        return ess.getWarps().getLocation(warpName);
    }
}