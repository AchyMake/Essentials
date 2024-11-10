package org.achymake.essentials.data;

import me.clip.placeholderapi.PlaceholderAPI;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record Userdata(OfflinePlayer getOfflinePlayer) {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getMain() {
        return getInstance().getConfig();
    }
    private Worlds getWorlds() {
        return getInstance().getWorlds();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private File getFile() {
        return new File(getInstance().getDataFolder(), "userdata/" + getUUID() + ".yml");
    }
    public boolean exists() {
        return getFile().exists();
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public void setString(String path, String value) {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setStringList(String path, List<String> value) {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setDouble(String path, double value) {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setInt(String path, int value) {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setFloat(String path, float value) {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setLong(String path, long value) {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setBoolean(String path, boolean value) {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public String getName() {
        return getOfflinePlayer().getName();
    }
    public String getDisplayName() {
        return getConfig().getString("display-name");
    }
    public double getAccount() {
        return getConfig().getDouble("account");
    }
    public double getBankAccount() {
        return getConfig().getDouble("bank.account");
    }
    public List<String> getBankMembers() {
        return getConfig().getStringList("bank.members");
    }
    public boolean isAutoPick() {
        return getConfig().getBoolean("settings.auto-pick");
    }
    public boolean isBanned() {
        return getConfig().getBoolean("settings.banned");
    }
    public String getBanReason() {
        var reason = getConfig().getString("settings.ban-reason");
        if (reason == null || reason.equalsIgnoreCase("")) {
            return "None";
        } else return getConfig().getString("settings.ban-reason");
    }
    public long getBanExpire() {
        return getConfig().getLong("settings.ban-expire");
    }
    public boolean isFrozen() {
        return getConfig().getBoolean("settings.frozen");
    }
    public boolean isJailed() {
        return getConfig().getBoolean("settings.jailed");
    }
    public boolean isDisabled() {
        return isFrozen() || isJailed();
    }
    public boolean isMuted() {
        return getConfig().getBoolean("settings.muted");
    }
    public boolean isPVP() {
        return getConfig().getBoolean("settings.pvp");
    }
    public boolean isVanished() {
        return getConfig().getBoolean("settings.vanished");
    }
    public OfflinePlayer getLastWhisper() {
        if (getConfig().isString("last-whisper")) {
            return getInstance().getOfflinePlayer(UUID.fromString(getConfig().getString("last-whisper")));
        } else return null;
    }
    public OfflinePlayer getTpaSent() {
        if (getConfig().isString("tpa.sent")) {
            return getInstance().getOfflinePlayer(UUID.fromString(getConfig().getString("tpa.sent")));
        } else return null;
    }
    public OfflinePlayer getTpaFrom() {
        if (getConfig().isString("tpa.from")) {
            return getInstance().getOfflinePlayer(UUID.fromString(getConfig().getString("tpa.from")));
        } else return null;
    }
    public OfflinePlayer getTpaHereSent() {
        if (getConfig().isString("tpahere.sent")) {
            return getInstance().getOfflinePlayer(UUID.fromString(getConfig().getString("tpahere.sent")));
        } else return null;
    }
    public OfflinePlayer getTpaHereFrom() {
        if (getConfig().isString("tpahere.from")) {
            return getInstance().getOfflinePlayer(UUID.fromString(getConfig().getString("tpahere.from")));
        } else return null;
    }
    public Set<String> getHomes() {
        return getConfig().getConfigurationSection("homes").getKeys(false);
    }
    public boolean isHome(String homeName) {
        return getHomes().contains(homeName);
    }
    public Location getHome(String homeName) {
        if (isHome(homeName)) {
            var world = getWorlds().get(getConfig().getString("homes." + homeName + ".world"));
            if (world != null) {
                var x = getConfig().getDouble("homes." + homeName + ".x");
                var y = getConfig().getDouble("homes." + homeName + ".y");
                var z = getConfig().getDouble("homes." + homeName + ".z");
                var yaw = getConfig().getLong("homes." + homeName + ".yaw");
                var pitch = getConfig().getLong("homes." + homeName + ".pitch");
                return new Location(world, x, y, z, yaw, pitch);
            } else return null;
        } else return null;
    }
    public boolean setHome(String homeName) {
        var player = getPlayer();
        if (player != null) {
            var location = player.getLocation();
            if (isHome(homeName)) {
                var file = getFile();
                var config = YamlConfiguration.loadConfiguration(file);
                config.set("homes." + homeName + ".world", location.getWorld().getName());
                config.set("homes." + homeName + ".x", location.getX());
                config.set("homes." + homeName + ".y", location.getY());
                config.set("homes." + homeName + ".z", location.getZ());
                config.set("homes." + homeName + ".yaw", location.getYaw());
                config.set("homes." + homeName + ".pitch", location.getPitch());
                try {
                    config.save(file);
                    return true;
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                    return false;
                }
            } else if (getMaxHomes() > getHomes().size()) {
                var file = getFile();
                var config = YamlConfiguration.loadConfiguration(file);
                config.set("homes." + homeName + ".world", location.getWorld().getName());
                config.set("homes." + homeName + ".x", location.getX());
                config.set("homes." + homeName + ".y", location.getY());
                config.set("homes." + homeName + ".z", location.getZ());
                config.set("homes." + homeName + ".yaw", location.getYaw());
                config.set("homes." + homeName + ".pitch", location.getPitch());
                try {
                    config.save(file);
                    return true;
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                    return false;
                }
            } else return false;
        } else return false;
    }
    private static int maxHomes;
    public int getMaxHomes() {
        var player = getPlayer();
        if (player != null) {
            if (player.isOp()) {
                maxHomes = 9999;
            } else {
                for (String rank : getMain().getConfigurationSection("homes").getKeys(false)) {
                    if (player.hasPermission("essentials.command.sethome.multiple." + rank)) {
                        maxHomes = getMain().getInt("homes." + rank);
                    }
                }
            }
        }
        return maxHomes;
    }
    public Set<String> getLocations() {
        return getConfig().getConfigurationSection("locations").getKeys(false);
    }
    public boolean isLocation(String locationName) {
        return getLocations().contains(locationName);
    }
    public void setLocation(Location location, String locationName) {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("locations." + locationName + ".world", location.getWorld().getName());
        config.set("locations." + locationName + ".x", location.getX());
        config.set("locations." + locationName + ".y", location.getY());
        config.set("locations." + locationName + ".z", location.getZ());
        config.set("locations." + locationName + ".yaw", location.getYaw());
        config.set("locations." + locationName + ".pitch", location.getPitch());
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public Location getLocation(String locationName) {
        if (isLocation(locationName)) {
            var world = getWorlds().get(getConfig().getString("locations." + locationName + ".world"));
            if (world != null) {
                var x = getConfig().getDouble("locations." + locationName + ".x");
                var y = getConfig().getDouble("locations." + locationName + ".y");
                var z = getConfig().getDouble("locations." + locationName + ".z");
                var yaw = getConfig().getLong("locations." + locationName + ".yaw");
                var pitch = getConfig().getLong("locations." + locationName + ".pitch");
                return new Location(world, x, y, z, yaw, pitch);
            } else return null;
        } else return null;
    }
    public void addTaskID(String task, int value) {
        setInt("tasks." + task, value);
    }
    public boolean hasTaskID(String task) {
        return getConfig().isInt("tasks." + task);
    }
    public int getTaskID(String task) {
        return getConfig().getInt("tasks." + task);
    }
    public void disableTask(String task) {
        getScheduler().cancel(getTaskID(task));
        setString("tasks." + task, null);
    }
    public void disableTasks() {
        getConfig().getConfigurationSection("tasks").getKeys(false).forEach(this::disableTask);
    }
    public boolean hasJoined() {
        if (exists()) {
            return isLocation("quit");
        } else return false;
    }
    private void setup() {
        var file = getFile();
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("name", getName());
        config.set("display-name", getName());
        config.set("account", getMain().getDouble("economy.starting-balance"));
        config.set("bank.account", getMain().getDouble("economy.bank.starting-balance"));
        config.createSection("bank.members");
        config.set("settings.auto-pick", false);
        config.set("settings.banned", false);
        config.set("settings.ban-reason", "");
        config.set("settings.ban-expire", 0);
        config.set("settings.frozen", false);
        config.set("settings.jailed", false);
        config.set("settings.muted", false);
        config.set("settings.pvp", true);
        config.set("settings.vanished", false);
        config.createSection("tpa");
        config.createSection("tpahere");
        config.createSection("homes");
        config.createSection("locations");
        config.createSection("tasks");
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void reload() {
        if (exists()) {
            var file = getFile();
            var config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getInstance().sendWarning(e.getMessage());
            }
            if (!getName().equals(config.getString("name"))) {
                config.set("name", getName());
                try {
                    config.save(file);
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                }
            }
        } else setup();
    }
    public UUID getUUID() {
        return getOfflinePlayer().getUniqueId();
    }
    public Player getPlayer() {
        return getOfflinePlayer().getPlayer();
    }
    public void teleport(Location location, String name, int timer) {
        var player = getPlayer();
        if (player != null) {
            if (!hasTaskID("teleport")) {
                if (!location.getChunk().isLoaded()) {
                    location.getChunk().load();
                }
                if (timer > 0) {
                    getMessage().sendActionBar(player, getMessage().get("events.teleport.post", String.valueOf(timer)));
                }
                var taskID = getInstance().getScheduleHandler().runLater(new Runnable() {
                    @Override
                    public void run() {
                        setString("tasks.teleport", null);
                        getMessage().sendActionBar(player, getMessage().get("events.teleport.success", name));
                        player.teleport(location);
                    }
                }, timer * 20L).getTaskId();
                addTaskID("teleport", taskID);
            } else player.sendMessage(getMessage().get("events.teleport.has-task"));
        }
    }
    @Override
    public OfflinePlayer getOfflinePlayer() {
        return getOfflinePlayer;
    }
    public float getDefaultFlySpeed() {
        return 0.1F;
    }
    public void setFlySpeed(float amount) {
        var player = getPlayer();
        if (player != null) {
            if (amount > 0) {
                player.setFlySpeed(getDefaultFlySpeed() * amount);
            } else player.setFlySpeed(getDefaultFlySpeed());
        }
    }
    public float getDefaultWalkSpeed() {
        return 0.2F;
    }
    public void setWalkSpeed(float amount) {
        var player = getPlayer();
        if (player != null) {
            if (amount > 0) {
                player.setWalkSpeed(getDefaultWalkSpeed() * amount);
            } else player.setWalkSpeed(getDefaultWalkSpeed());
        }
    }
    public boolean setGameMode(String mode) {
        var player = getPlayer();
        if (player != null) {
            if (mode.equalsIgnoreCase("adventure")) {
                player.setGameMode(GameMode.ADVENTURE);
                getMessage().sendActionBar(player, getMessage().get("commands.gamemode.adventure"));
                return true;
            } else if (mode.equalsIgnoreCase("creative")) {
                player.setGameMode(GameMode.CREATIVE);
                getMessage().sendActionBar(player, getMessage().get("commands.gamemode.creative"));
                return true;
            } else if (mode.equalsIgnoreCase("spectator")) {
                player.setGameMode(GameMode.SPECTATOR);
                getMessage().sendActionBar(player, getMessage().get("commands.gamemode.spectator"));
                return true;
            } else if (mode.equalsIgnoreCase("survival")) {
                player.setGameMode(GameMode.SURVIVAL);
                getMessage().sendActionBar(player, getMessage().get("commands.gamemode.survival"));
                return true;
            } else return false;
        } else return false;
    }
    public void randomTeleport() {
        var player = getPlayer();
        if (player != null) {
            getMessage().sendActionBar(player, getMessage().get("commands.rtp.post-teleport"));
            getScheduler().runLater(new Runnable() {
                @Override
                public void run() {
                    var block = getWorlds().highestRandomBlock(getMain().getString("commands.rtp.world"), getMain().getInt("commands.rtp.spread"));
                    if (block.isLiquid()) {
                        getMessage().sendActionBar(player, getMessage().get("commands.rtp.liquid"));
                        randomTeleport();
                    } else {
                        if (!block.getChunk().isLoaded()) {
                            block.getChunk().load();
                        }
                        getMessage().sendActionBar(player, getMessage().get("commands.rtp.teleport"));
                        player.teleport(block.getLocation().add(0.5,1,0.5));
                    }
                }
            }, 0);
        }
    }
    public String prefix() {
        var player = getPlayer();
        if (player != null) {
            if (PlaceholderAPI.isRegistered("vault")) {
                return getMessage().addColor(PlaceholderAPI.setPlaceholders(player, "%vault_prefix%"));
            } else return "";
        } else return "";
    }
    public String suffix() {
        var player = getPlayer();
        if (player != null) {
            if (PlaceholderAPI.isRegistered("vault")) {
                return getMessage().addColor(PlaceholderAPI.setPlaceholders(player, "%vault_suffix%"));
            } else return "";
        } else return "";
    }
}