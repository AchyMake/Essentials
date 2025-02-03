package org.achymake.essentials.providers;

import org.achymake.essentials.Essentials;
import org.achymake.vaultextra.services.UserService;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;

import java.io.File;
import java.util.Set;

public class UserProvider implements UserService {
    private final Essentials ess;
    public UserProvider(Essentials ess) {
        this.ess = ess;
    }
    public void register() {
        ess.getServer().getServicesManager().register(UserService.class, this, ess, ServicePriority.Normal);
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
    public File getFile(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().getFile(offlinePlayer);
    }
    @Override
    public boolean exists(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().exists(offlinePlayer);
    }
    @Override
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().getConfig(offlinePlayer);
    }
    @Override
    public boolean isHome(OfflinePlayer offlinePlayer, String homeName) {
        return ess.getUserdata().isHome(offlinePlayer, homeName);
    }
    @Override
    public Set<String> getHomes(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().getHomes(offlinePlayer);
    }
    @Override
    public Location getHome(OfflinePlayer offlinePlayer, String homeName) {
        return ess.getUserdata().getHome(offlinePlayer, homeName);
    }
    @Override
    public boolean isLocation(OfflinePlayer offlinePlayer, String locationName) {
        return ess.getUserdata().isLocation(offlinePlayer, locationName);
    }
    @Override
    public Set<String> getLocations(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().getLocations(offlinePlayer);
    }
    @Override
    public Location getLocation(OfflinePlayer offlinePlayer, String locationName) {
        return ess.getUserdata().getLocation(offlinePlayer, locationName);
    }
    @Override
    public boolean isDisabled(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().isDisabled(offlinePlayer);
    }
    @Override
    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().isPVP(offlinePlayer);
    }
    @Override
    public boolean setPVP(OfflinePlayer offlinePlayer, boolean value) {
        return ess.getUserdata().setBoolean(offlinePlayer, "settings.pvp", value);
    }
    @Override
    public boolean isFrozen(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().isFrozen(offlinePlayer);
    }
    @Override
    public boolean isJailed(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().isJailed(offlinePlayer);
    }
    @Override
    public boolean isMuted(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().isMuted(offlinePlayer);
    }
    @Override
    public boolean isVanished(OfflinePlayer offlinePlayer) {
        return ess.getUserdata().isVanished(offlinePlayer);
    }
}