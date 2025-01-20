package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import org.achymake.essentials.handlers.MaterialHandler;
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
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Userdata {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private EconomyHandler getEconomy() {
        return getInstance().getEconomyHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    /**
     * gets userdata/uuid.yml
     * @param offlinePlayer or player
     * @return file
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public File getFile(OfflinePlayer offlinePlayer) {
        return new File(getInstance().getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
    }
    /**
     * if file exists
     * @param offlinePlayer or player
     * @return true if file exists else false
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean exists(OfflinePlayer offlinePlayer) {
        return getFile(offlinePlayer).exists();
    }
    /**
     * config of userdata/uuid.yml
     * @param offlinePlayer or player
     * @return config
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(getFile(offlinePlayer));
    }
    /**
     * sets string
     * @param offlinePlayer or player
     * @param path path
     * @param value string
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setString(OfflinePlayer offlinePlayer, String path, String value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * sets list string
     * @param offlinePlayer or player
     * @param path path
     * @param value list string
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setStringList(OfflinePlayer offlinePlayer, String path, List<String> value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * sets double
     * @param offlinePlayer or player
     * @param path path
     * @param value double
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setDouble(OfflinePlayer offlinePlayer, String path, double value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * sets int
     * @param offlinePlayer or player
     * @param path path
     * @param value int
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setInt(OfflinePlayer offlinePlayer, String path, int value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * sets float
     * @param offlinePlayer or player
     * @param path path
     * @param value float
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setFloat(OfflinePlayer offlinePlayer, String path, float value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * sets long
     * @param offlinePlayer or player
     * @param path path
     * @param value long
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setLong(OfflinePlayer offlinePlayer, String path, long value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * sets boolean
     * @param offlinePlayer or player
     * @param path path
     * @param value boolean
     * @since many moons ago
     * @see File
     * @see FileConfiguration
     */
    public boolean setBoolean(OfflinePlayer offlinePlayer, String path, boolean value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * has joined
     * @param offlinePlayer or player
     * @return true if player has joined else false
     * @since many moons ago
     */
    public boolean hasJoined(OfflinePlayer offlinePlayer) {
        if (exists(offlinePlayer)) {
            return isLocation(offlinePlayer, "quit");
        } else return false;
    }
    /**
     * get display name
     * @param offlinePlayer or player
     * @return display name if null else name
     * @since many moons ago
     */
    public String getDisplayName(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("display-name")) {
            return config.getString("display-name");
        } else return offlinePlayer.getName();
    }
    /**
     * get account
     * @param offlinePlayer or player
     * @return double
     * @since many moons ago
     */
    public double getAccount(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getDouble("account");
    }
    /**
     * has bank
     * @param offlinePlayer or player
     * @return true if offlinePlayer has bank else false
     * @since many moons ago
     */
    public boolean hasBank(OfflinePlayer offlinePlayer) {
        return !getBank(offlinePlayer).isEmpty();
    }
    /**
     * get bank name
     * @param offlinePlayer or player
     * @return string
     * @since many moons ago
     */
    public String getBank(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("bank")) {
            return config.getString("bank");
        } else return "";
    }
    /**
     * get bank rank
     * @param offlinePlayer
     * or player
     * @return string
     * @since many moons ago
     */
    public String getBankRank(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("bank-rank")) {
            return config.getString("bank-rank");
        } else return "default";
    }
    /**
     * gets boolean for 'settings.jailed' and 'settings.frozen'
     * @param offlinePlayer or player
     * @return true if player is frozen or jailed else false
     * @since many moons ago
     */
    public boolean isDisabled(OfflinePlayer offlinePlayer) {
        return isFrozen(offlinePlayer) || isJailed(offlinePlayer);
    }
    /**
     * gets boolean for 'settings.pvp'
     * @param offlinePlayer or player
     * @return true if player has pvp enabled else false
     * @since many moons ago
     */
    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.pvp");
    }
    /**
     * gets boolean for 'settings.frozen'
     * @param offlinePlayer or player
     * @return true if player is frozen else false
     * @since many moons ago
     */
    public boolean isFrozen(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.frozen");
    }
    /**
     * gets boolean for 'settings.jailed'
     * @param offlinePlayer or player
     * @return true if player is jailed else false
     * @since many moons ago
     */
    public boolean isJailed(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.jailed");
    }
    /**
     * gets boolean for 'settings.muted'
     * @param offlinePlayer or player
     * @return true if player is muted else false
     * @since many moons ago
     */
    public boolean isMuted(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.muted");
    }
    /**
     * gets boolean for 'settings.banned'
     * @param offlinePlayer or player
     * @return true if player is banned else false
     * @since many moons ago
     */
    public boolean isBanned(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.banned");
    }
    /**
     * gets string for 'settings.ban-reason'
     * @param offlinePlayer or player
     * @return string
     * @since many moons ago
     */
    public String getBanReason(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("settings.ban-reason")) {
            return config.getString("settings.ban-reason");
        } else return "None";
    }
    /**
     * gets long for 'settings.ban-expire'
     * @param offlinePlayer or player
     * @return long
     * @since many moons ago
     */
    public long getBanExpire(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getLong("settings.ban-expire");
    }
    /**
     * gets boolean for 'settings.board'
     * @param offlinePlayer or player
     * @return true if offlinePlayer has board enabled else false
     * @since many moons ago
     */
    public boolean hasBoard(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.board");
    }
    /**
     * gets boolean for 'settings.vanished'
     * @param offlinePlayer or player
     * @return true if target is vanished else false
     * @since many moons ago
     */
    public boolean isVanished(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getBoolean("settings.vanished");
    }
    /**
     * gets offlinePlayer for 'last-whisper'
     * @param offlinePlayer or player
     * @return offlinePlayer else null
     * @since many moons ago
     */
    public OfflinePlayer getLastWhisper(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("last-whisper")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("last-whisper")));
        } else return null;
    }
    /**
     * gets offlinePlayer for 'bank-invite.sent'
     * @param offlinePlayer or player
     * @return offlinePlayer else null if none
     * @since many moons ago
     */
    public OfflinePlayer getBankSent(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("bank-invite.sent")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("bank-invite.sent")));
        } else return null;
    }
    /**
     * gets offlinePlayer for 'bank-invite.from'
     * @param offlinePlayer or player
     * @return offlinePlayer else null if none
     * @since many moons ago
     */
    public OfflinePlayer getBankFrom(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("bank-invite.from")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("bank-invite.from")));
        } else return null;
    }
    /**
     * gets offlinePlayer for 'tpa.sent'
     * @param offlinePlayer or player
     * @return offlinePlayer else null if none
     * @since many moons ago
     */
    public OfflinePlayer getTpaSent(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("tpa.sent")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("tpa.sent")));
        } else return null;
    }
    /**
     * gets offlinePlayer for 'tpa.from'
     * @param offlinePlayer or player
     * @return offlinePlayer else null if none
     * @since many moons ago
     */
    public OfflinePlayer getTpaFrom(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("tpa.from")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("tpa.from")));
        } else return null;
    }
    /**
     * gets offlinePlayer for 'tpahere.sent'
     * @param offlinePlayer or player
     * @return offlinePlayer else null if none
     * @since many moons ago
     */
    public OfflinePlayer getTpaHereSent(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("tpahere.sent")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("tpahere.sent")));
        } else return null;
    }
    /**
     * gets offlinePlayer for 'tpahere.from'
     * @param offlinePlayer or player
     * @return offlinePlayer else null if none
     * @since many moons ago
     */
    public OfflinePlayer getTpaHereFrom(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        if (config.isString("tpahere.from")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("tpahere.from")));
        } else return null;
    }
    /**
     * @param offlinePlayer or player
     * @param homeName string
     * @return true if homeName exists else false
     * @since many moons ago
     */
    public boolean isHome(OfflinePlayer offlinePlayer, String homeName) {
        return getHomes(offlinePlayer).contains(homeName);
    }
    /**
     * gets set string of homes
     * @param offlinePlayer or player
     * @return set string
     * @since many moons ago
     */
    public Set<String> getHomes(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getConfigurationSection("homes").getKeys(false);
    }
    /**
     * get player max home
     * @return integer
     * @since many moons ago
     */
    public int getMaxHomes(Player player) {
        if (!player.isOp()) {
            var listed = new ArrayList<Integer>();
            for (var value : getConfig().getConfigurationSection("homes").getKeys(false)) {
                if (player.hasPermission("essentials.command.sethome.multiple." + value)) {
                    listed.add(getConfig().getInt("homes." + value));
                }
            }
            listed.sort(Integer::compareTo);
            if (!listed.isEmpty()) {
                return listed.getLast();
            } else return getConfig().getInt("homes.default");
        } else return getConfig().getInt("homes.op");
    }
    /**
     * get home
     * @param offlinePlayer or player
     * @param homeName string
     * @return location if homeName exists else null
     * @since many moons ago
     */
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
    /**
     * set home
     * @param player target
     * @param homeName string
     * @return true if homeName exist else false,
     * true if max home is above homes size else false
     * @since many moons ago
     */
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
    /**
     * is location set
     * @param offlinePlayer or player
     * @param locationName string
     * @return true if locationName exists else false
     * @since many moons ago
     */
    public boolean isLocation(OfflinePlayer offlinePlayer, String locationName) {
        return getLocations(offlinePlayer).contains(locationName);
    }
    /**
     * get locations name
     * @param offlinePlayer or player
     * @return set string
     * @since many moons ago
     */
    public Set<String> getLocations(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getConfigurationSection("locations").getKeys(false);
    }
    /**
     * get location
     * @param offlinePlayer or player
     * @param locationName string
     * @return location if locationName exists else null
     * @since many moons ago
     */
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
    /**
     * set location
     * @param offlinePlayer or player
     * @param location location
     * @param locationName string
     * @since many moons ago
     */
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
    /**
     * add task id
     * @param offlinePlayer or player
     * @param taskName string
     * @param value integer
     * @since many moons ago
     */
    public void addTaskID(OfflinePlayer offlinePlayer, String taskName, int value) {
        setInt(offlinePlayer, "tasks." + taskName, value);
    }
    /**
     * has task id
     * @param offlinePlayer or player
     * @param taskName string
     * @return true if target has task else false
     * @since many moons ago
     */
    public boolean hasTaskID(OfflinePlayer offlinePlayer, String taskName) {
        return getConfig(offlinePlayer).isInt("tasks." + taskName);
    }
    /**
     * get task id
     * @param offlinePlayer or player
     * @param taskName string
     * @return integer
     * @since many moons ago
     */
    public int getTaskID(OfflinePlayer offlinePlayer, String taskName) {
        return getConfig(offlinePlayer).getInt("tasks." + taskName);
    }
    /**
     * remove task which will likely cancel first if its scheduled
     * @param offlinePlayer or player
     * @param taskName string
     * @since many moons ago
     */
    public void removeTask(OfflinePlayer offlinePlayer, String taskName) {
        if (getScheduler().isQueued(getTaskID(offlinePlayer, taskName))) {
            getScheduler().cancel(getTaskID(offlinePlayer, taskName));
        }
        setString(offlinePlayer, "tasks." + taskName, null);
    }
    /**
     * this will cancel then remove all target tasks
     * @param offlinePlayer or player
     * @since many moons ago
     */
    public void disableTasks(OfflinePlayer offlinePlayer) {
        getConfig(offlinePlayer).getConfigurationSection("tasks").getKeys(false).forEach(s -> removeTask(offlinePlayer, s));
    }
    /**
     * gets main config chat format
     * @param player target
     * @since many moons ago
     */
    public String getChat(Player player) {
        if (!player.isOp()) {
            for (var value : getConfig().getConfigurationSection("chat.format").getKeys(false)) {
                if (player.hasPermission("essentials.event.chat." + value)) {
                    return getConfig().getString("chat.format." + value);
                }
            }
            return getConfig().getString("chat.format.default");
        } else return getConfig().getString("chat.format.op");
    }
    /**
     * get default fly speed
     * @since many moons ago
     */
    public float getDefaultFlySpeed() {
        return 0.1F;
    }
    /**
     * get default walk speed
     * @since many moons ago
     */
    public float getDefaultWalkSpeed() {
        return 0.2F;
    }
    /**
     * sets fly speed
     * @param player target
     * @param amount float
     * @since many moons ago
     */
    public void setFlySpeed(Player player, float amount) {
        if (amount > 0) {
            player.setFlySpeed(getDefaultFlySpeed() * amount);
        } else player.setFlySpeed(getDefaultFlySpeed());
    }
    /**
     * sets walk speed
     * @param player target
     * @param amount float
     * @since many moons ago
     */
    public void setWalkSpeed(Player player, float amount) {
        if (amount > 0) {
            player.setWalkSpeed(getDefaultWalkSpeed() * amount);
        } else player.setWalkSpeed(getDefaultWalkSpeed());
    }
    /**
     * set game mode
     * @param player target
     * @param mode string
     * @return true if mode is gamemode else false
     * @since many moons ago
     */
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
    public boolean setHelmet(Player player, ItemStack itemStack) {
        if (player.getInventory().getHelmet() == null) {
            player.getInventory().setHelmet(getMaterials().getItemStack(itemStack, 1));
            return true;
        } else return false;
    }
    /**
     * set morning for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setMorning(Player player) {
        setTime(player, 0);
    }
    /**
     * set day for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setDay(Player player) {
        setTime(player, 1000);
    }
    /**
     * set noon for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setNoon(Player player) {
        setTime(player, 6000);
    }
    /**
     * set night for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setNight(Player player) {
        setTime(player, 13000);
    }
    /**
     * set midnight for target which is not server time relative
     * @param player target
     * @since many moons ago
     */
    public void setMidnight(Player player) {
        setTime(player, 18000);
    }
    /**
     * set target time which is not server time relative
     * @param player target
     * @param value long
     * @since many moons ago
     */
    public void setTime(Player player, long value) {
        player.setPlayerTime(value, false);
    }
    /**
     * add time which is not server time relative
     * @param player target
     * @param value long
     * @since many moons ago
     */
    public void addTime(Player player, long value) {
        setTime(player, player.getPlayerTime() + value);
    }
    /**
     * remove target time which is not server time relative
     * @param player target
     * @param value long
     * @since many moons ago
     */
    public void removeTime(Player player, long value) {
        setTime(player, player.getPlayerTime() - value);
    }
    /**
     * reset target time, this will reset to server time relative
     * @param player target
     * @since many moons ago
     */
    public void resetTime(Player player) {
        player.resetPlayerTime();
    }
    /**
     * set weather for target
     * @param player target
     * @param weatherType string
     * @since many moons ago
     */
    public void setWeather(Player player, String weatherType) {
        if (weatherType.equalsIgnoreCase("clear")) {
            player.setPlayerWeather(WeatherType.CLEAR);
        } else if (weatherType.equalsIgnoreCase("rain")) {
            player.setPlayerWeather(WeatherType.DOWNFALL);
        } else if (weatherType.equalsIgnoreCase("reset")) {
            player.resetPlayerWeather();
        }
    }
    /**
     * reload userdata folder
     * @since many moons ago
     */
    /**
     * sets file up for target
     * @param offlinePlayer or player
     * @since many moons ago
     */
    private boolean setup(OfflinePlayer offlinePlayer) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("name", offlinePlayer.getName());
        config.set("display-name", offlinePlayer.getName());
        config.set("account", getEconomy().getStartingBalance());
        config.set("bank", "");
        config.set("bank-rank", "default");
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
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * reloads target file else setup if file does not exist
     * @param offlinePlayer or player
     * @since many moons ago
     */
    public boolean reload(OfflinePlayer offlinePlayer) {
        if (exists(offlinePlayer)) {
            var file = getFile(offlinePlayer);
            var config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
                return true;
            } catch (IOException | InvalidConfigurationException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return setup(offlinePlayer);
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
    /**
     * get userdata uuid
     * @return list uuid
     * @since many moons ago
     */
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
    /**
     * get offlinePlayers from userdata folder
     * @return list offlinePlayer
     * @since many moons ago
     */
    public List<OfflinePlayer> getOfflinePlayers() {
        var listed = new ArrayList<OfflinePlayer>();
        if (!getUUIDs().isEmpty()) {
            for (var uuid : getUUIDs()) {
                listed.add(getInstance().getOfflinePlayer(uuid));
            }
        }
        return listed;
    }
    public void disable() {
        getInstance().getOnlinePlayers().forEach(player -> {
            setLocation(player, player.getLocation(), "quit");
        });
    }
}