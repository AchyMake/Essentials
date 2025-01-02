package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.achymake.essentials.handlers.WorldHandler;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Userdata {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getMain() {
        return getInstance().getConfig();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public File getFile(OfflinePlayer offlinePlayer) {
        return new File(getInstance().getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
    }
    public boolean exists(OfflinePlayer offlinePlayer) {
        return getFile(offlinePlayer).exists();
    }
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(getFile(offlinePlayer));
    }
    public void setString(OfflinePlayer offlinePlayer, String path, String value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setStringList(OfflinePlayer offlinePlayer, String path, List<String> value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setDouble(OfflinePlayer offlinePlayer, String path, double value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setInt(OfflinePlayer offlinePlayer, String path, int value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setFloat(OfflinePlayer offlinePlayer, String path, float value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setLong(OfflinePlayer offlinePlayer, String path, long value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setBoolean(OfflinePlayer offlinePlayer, String path, boolean value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public boolean hasJoined(OfflinePlayer offlinePlayer) {
        if (exists(offlinePlayer)) {
            return isLocation(offlinePlayer, "quit");
        } else return false;
    }
    public String getDisplayName(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getString("display-name");
    }
    public double getAccount(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getDouble("account");
    }
    public boolean hasBank(OfflinePlayer offlinePlayer) {
        return !getBank(offlinePlayer).isEmpty();
    }
    public String getBank(OfflinePlayer offlinePlayer) {
        if (getConfig(offlinePlayer).isString("bank")) {
            return getConfig(offlinePlayer).getString("bank");
        } else return "";
    }
    public String getBankRank(OfflinePlayer offlinePlayer) {
        if (getConfig(offlinePlayer).isString("bank-rank")) {
            return getConfig(offlinePlayer).getString("bank-rank");
        } else return "default";
    }
    public boolean isDisabled(OfflinePlayer offlinePlayer) {
        return isFrozen(offlinePlayer) || isJailed(offlinePlayer);
    }
    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.pvp");
    }
    public boolean isFrozen(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.frozen");
    }
    public boolean isJailed(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.jailed");
    }
    public boolean isMuted(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.muted");
    }
    public boolean isBanned(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.banned");
    }
    public String getBanReason(OfflinePlayer offlinePlayer) {
        if (getConfig(offlinePlayer).isString("settings.ban-reason")) {
            return getConfig(offlinePlayer).getString("settings.ban-reason");
        } else return "None";
    }
    public long getBanExpire(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getLong("settings.ban-expire");
    }
    public boolean hasBoard(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.board");
    }
    public boolean isVanished(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.vanished");
    }
    public OfflinePlayer getLastWhisper(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("last-whisper")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("last-whisper")));
        } else return null;
    }
    public OfflinePlayer getBankSent(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("bank-invite.sent")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("bank-invite.sent")));
        } else return null;
    }
    public OfflinePlayer getBankFrom(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("bank-invite.from")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("bank-invite.from")));
        } else return null;
    }
    public OfflinePlayer getTpaSent(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("tpa.sent")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("tpa.sent")));
        } else return null;
    }
    public OfflinePlayer getTpaFrom(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("tpa.from")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("tpa.from")));
        } else return null;
    }
    public OfflinePlayer getTpaHereSent(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("tpahere.sent")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("tpahere.sent")));
        } else return null;
    }
    public OfflinePlayer getTpaHereFrom(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("tpahere.from")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("tpahere.from")));
        } else return null;
    }
    public boolean isHome(OfflinePlayer offlinePlayer, String homeName) {
        return getHomes(offlinePlayer).contains(homeName);
    }
    public Set<String> getHomes(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getConfigurationSection("homes").getKeys(false);
    }
    public int getMaxHomes(Player player) {
        if (!player.isOp()) {
            for (var value : getMain().getConfigurationSection("homes").getKeys(false)) {
                if (player.hasPermission("essentials.command.sethome.multiple." + value)) {
                    return getMain().getInt("homes." + value);
                }
            }
            return getMain().getInt("homes.default");
        } else return getMain().getInt("homes.op");
    }
    public Location getHome(OfflinePlayer offlinePlayer, String homeName) {
        if (isHome(offlinePlayer, homeName)) {
            var config = getConfig(offlinePlayer);
            var world = getWorldHandler().get(config.getString("homes." + homeName + ".world"));
            if (world != null) {
                var x = config.getDouble("homes." + homeName + ".x");
                var y = config.getDouble("homes." + homeName + ".y");
                var z = config.getDouble("homes." + homeName + ".z");
                var yaw = config.getLong("homes." + homeName + ".yaw");
                var pitch = config.getLong("homes." + homeName + ".pitch");
                return new Location(world, x, y, z, yaw, pitch);
            } else return null;
        } else return null;
    }
    public boolean setHome(Player player, String homeName) {
        var location = player.getLocation();
        if (isHome(player, homeName)) {
            var file = getFile(player);
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
        } else if (getMaxHomes(player) > getHomes(player).size()) {
            var file = getFile(player);
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
    }
    public boolean isLocation(OfflinePlayer offlinePlayer, String locationName) {
        return getLocations(offlinePlayer).contains(locationName);
    }
    public Set<String> getLocations(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getConfigurationSection("locations").getKeys(false);
    }
    public Location getLocation(OfflinePlayer offlinePlayer, String locationName) {
        if (isLocation(offlinePlayer, locationName)) {
            var config = getConfig(offlinePlayer);
            var world = getWorldHandler().get(config.getString("locations." + locationName + ".world"));
            if (world != null) {
                var x = config.getDouble("locations." + locationName + ".x");
                var y = config.getDouble("locations." + locationName + ".y");
                var z = config.getDouble("locations." + locationName + ".z");
                var yaw = config.getLong("locations." + locationName + ".yaw");
                var pitch = config.getLong("locations." + locationName + ".pitch");
                return new Location(world, x, y, z, yaw, pitch);
            } else return null;
        } else return null;
    }
    public void setLocation(OfflinePlayer offlinePlayer, Location location, String locationName) {
        var file = getFile(offlinePlayer);
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
    public void addTaskID(OfflinePlayer offlinePlayer, String task, int value) {
        setInt(offlinePlayer, "tasks." + task, value);
    }
    public boolean hasTaskID(OfflinePlayer offlinePlayer, String task) {
        return getConfig(offlinePlayer).isInt("tasks." + task);
    }
    public int getTaskID(OfflinePlayer offlinePlayer, String task) {
        return getConfig(offlinePlayer).getInt("tasks." + task);
    }
    public void removeTask(OfflinePlayer offlinePlayer, String task) {
        if (getScheduler().isQueued(getTaskID(offlinePlayer, task))) {
            getScheduler().cancel(getTaskID(offlinePlayer, task));
        }
        setString(offlinePlayer, "tasks." + task, null);
    }
    public void disableTasks(OfflinePlayer offlinePlayer) {
        getConfig(offlinePlayer).getConfigurationSection("tasks").getKeys(false).forEach(s -> removeTask(offlinePlayer, s));
    }
    private void setup(OfflinePlayer offlinePlayer) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("name", offlinePlayer.getName());
        config.set("display-name", offlinePlayer.getName());
        config.set("account", getEconomyHandler().getStartingBalance());
        config.set("bank", "");
        config.set("settings.pvp", !isPVP(offlinePlayer));
        config.set("settings.muted", isMuted(offlinePlayer));
        config.set("settings.frozen", isFrozen(offlinePlayer));
        config.set("settings.jailed", isJailed(offlinePlayer));
        config.set("settings.banned", isBanned(offlinePlayer));
        config.set("settings.ban-expire", 0);
        config.set("settings.board", !hasBoard(offlinePlayer));
        config.set("settings.vanished", isVanished(offlinePlayer));
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
    public void reload(OfflinePlayer offlinePlayer) {
        if (exists(offlinePlayer)) {
            var file = getFile(offlinePlayer);
            var config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getInstance().sendWarning(e.getMessage());
            }
            if (!offlinePlayer.getName().equals(config.getString("name"))) {
                config.set("name", offlinePlayer.getName());
                try {
                    config.save(file);
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                }
            }
            if (isBanned(offlinePlayer)) {
                config.set("settings.banned", false);
                config.set("settings.ban-expire", 0);
                config.set("settings.ban-reason", null);
                try {
                    config.save(file);
                } catch (IOException e) {
                    getInstance().sendWarning(e.getMessage());
                }
            }
        } else setup(offlinePlayer);
    }
    public String getChat(Player player) {
        if (!player.isOp()) {
            for (var value : getMain().getConfigurationSection("chat.format").getKeys(false)) {
                if (player.hasPermission("essentials.event.chat." + value)) {
                    return getMain().getString("chat.format." + value);
                }
            }
            return getMain().getString("chat.format.default");
        } else return getMain().getString("chat.format.op");
    }
    public float getDefaultFlySpeed() {
        return 0.1F;
    }
    public float getDefaultWalkSpeed() {
        return 0.2F;
    }
    public void setFlySpeed(Player player, float amount) {
        if (amount > 0) {
            player.setFlySpeed(getDefaultFlySpeed() * amount);
        } else player.setFlySpeed(getDefaultFlySpeed());
    }
    public void setWalkSpeed(Player player, float amount) {
        if (amount > 0) {
            player.setWalkSpeed(getDefaultWalkSpeed() * amount);
        } else player.setWalkSpeed(getDefaultWalkSpeed());
    }
    public boolean setGameMode(Player player, String mode) {
        if (mode.equalsIgnoreCase("adventure")) {
            player.setGameMode(GameMode.ADVENTURE);
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.adventure")));
            return true;
        } else if (mode.equalsIgnoreCase("creative")) {
            player.setGameMode(GameMode.CREATIVE);
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.creative")));
            return true;
        } else if (mode.equalsIgnoreCase("spectator")) {
            player.setGameMode(GameMode.SPECTATOR);
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.spectator")));
            return true;
        } else if (mode.equalsIgnoreCase("survival")) {
            player.setGameMode(GameMode.SURVIVAL);
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.survival")));
            return true;
        } else return false;
    }
    public void setMorning(Player player) {
        setTime(player, 0);
    }
    public void setDay(Player player) {
        setTime(player, 1000);
    }
    public void setNoon(Player player) {
        setTime(player, 6000);
    }
    public void setNight(Player player) {
        setTime(player, 13000);
    }
    public void setMidnight(Player player) {
        setTime(player, 18000);
    }
    public void setTime(Player player, long value) {
        player.setPlayerTime(value, false);
    }
    public void addTime(Player player, long value) {
        setTime(player, player.getPlayerTime() + value);
    }
    public void removeTime(Player player, long value) {
        setTime(player, player.getPlayerTime() - value);
    }
    public void resetTime(Player player) {
        player.resetPlayerTime();
    }
    public void setWeather(Player player, String weatherType) {
        if (weatherType.equalsIgnoreCase("clear")) {
            player.setPlayerWeather(WeatherType.CLEAR);
        } else if (weatherType.equalsIgnoreCase("rain")) {
            player.setPlayerWeather(WeatherType.DOWNFALL);
        } else if (weatherType.equalsIgnoreCase("reset")) {
            player.resetPlayerWeather();
        }
    }
    public void reload() {
        var folder = new File(getInstance().getDataFolder(), "userdata");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    var config = YamlConfiguration.loadConfiguration(file);
                    try {
                        config.load(file);
                    } catch (IOException | InvalidConfigurationException e) {
                        getInstance().sendWarning(e.getMessage());
                    }
                }
            }
        }
    }
    public List<UUID> getUUIDs() {
        var listed = new ArrayList<UUID>();
        var folder = new File(getInstance().getDataFolder(), "userdata");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    listed.add(UUID.fromString(file.getName().replace(".yml", "")));
                }
            }
        }
        return listed;
    }
    public List<OfflinePlayer> getOfflinePlayers() {
        var listed = new ArrayList<OfflinePlayer>();
        if (!getUUIDs().isEmpty()) {
            for (var uuid : getUUIDs()) {
                listed.add(getInstance().getOfflinePlayer(uuid));
            }
        }
        return listed;
    }
}