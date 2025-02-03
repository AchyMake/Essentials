package org.achymake.essentials.providers;

import org.achymake.essentials.Essentials;
import org.achymake.vaultextra.services.WorthService;
import org.bukkit.Material;
import org.bukkit.plugin.ServicePriority;

import java.util.Set;

public class WorthProvider implements WorthService {
    private final Essentials ess;
    public WorthProvider(Essentials ess) {
        this.ess = ess;
    }
    public void register() {
        ess.getServer().getServicesManager().register(WorthService.class, this, ess, ServicePriority.Normal);
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
        return ess.getWorth().getListed();
    }
    @Override
    public boolean isListed(Material material) {
        return ess.getWorth().isListed(material);
    }
    @Override
    public double get(Material material) {
        return ess.getWorth().get(material);
    }
}