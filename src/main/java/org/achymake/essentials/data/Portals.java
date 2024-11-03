package org.achymake.essentials.data;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Portals {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private File getFile(String idName) {
        return new File(getInstance().getDataFolder(), "portals/" + idName + ".yml");
    }
    public boolean exists(String idName) {
        return getFile(idName).exists();
    }
    public FileConfiguration getConfig(String idName) {
        return YamlConfiguration.loadConfiguration(getFile(idName));
    }
    public List<String> getTypes() {
        var portalTypes = new ArrayList<String>();
        portalTypes.add("end_portal");
        portalTypes.add("nether_portal");
        portalTypes.add("end_gateway");
        portalTypes.sort(String::compareToIgnoreCase);
        return portalTypes;
    }
    public boolean delPortal(String idName) {
        var file = getFile(idName);
        if (file.exists()) {
            file.delete();
            return true;
        } else return false;
    }
    public void setPortalType(String idName, String blockType) {
        var file = getFile(idName);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("portal-type", blockType.toUpperCase());
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void setPrimary(ItemStack itemStack, Block block) {
        var meta = itemStack.getItemMeta();
        getMaterials().getData(meta).set(getMaterials().getKey("primary.world"), PersistentDataType.STRING, block.getWorld().getName());
        getMaterials().getData(meta).set(getMaterials().getKey("primary.x"), PersistentDataType.INTEGER, block.getX());
        getMaterials().getData(meta).set(getMaterials().getKey("primary.y"), PersistentDataType.INTEGER, block.getY());
        getMaterials().getData(meta).set(getMaterials().getKey("primary.z"), PersistentDataType.INTEGER, block.getZ());
        itemStack.setItemMeta(meta);
    }
    public void setPrimary(String idName, Location primary) {
        var file = getFile(idName);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("primary.world", primary.getWorld().getName());
        config.set("primary.x", primary.getBlockX());
        config.set("primary.y", primary.getBlockY());
        config.set("primary.z", primary.getBlockZ());
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean hasPrimary(ItemMeta itemMeta) {
        return getMaterials().getData(itemMeta).has(getMaterials().getKey("primary.world"), PersistentDataType.STRING)
                && getMaterials().getData(itemMeta).has(getMaterials().getKey("primary.x"), PersistentDataType.INTEGER)
                && getMaterials().getData(itemMeta).has(getMaterials().getKey("primary.y"), PersistentDataType.INTEGER)
                && getMaterials().getData(itemMeta).has(getMaterials().getKey("primary.z"), PersistentDataType.INTEGER);
    }
    public Location getPrimary(ItemStack itemStack) {
        var meta = itemStack.getItemMeta();
        if (hasPrimary(meta)) {
            var world = getInstance().getWorlds().get(getMaterials().getData(meta).get(getMaterials().getKey("primary.world"), PersistentDataType.STRING));
            var x = getMaterials().getData(meta).get(getMaterials().getKey("primary.x"), PersistentDataType.INTEGER);
            var y = getMaterials().getData(meta).get(getMaterials().getKey("primary.y"), PersistentDataType.INTEGER);
            var z = getMaterials().getData(meta).get(getMaterials().getKey("primary.z"), PersistentDataType.INTEGER);
            return new Location(world, x, y, z);
        } else return null;
    }
    public void setSecondary(ItemStack itemStack, Block block) {
        var itemMeta = itemStack.getItemMeta();
        getMaterials().getData(itemMeta).set(getMaterials().getKey("secondary.world"), PersistentDataType.STRING, block.getWorld().getName());
        getMaterials().getData(itemMeta).set(getMaterials().getKey("secondary.x"), PersistentDataType.INTEGER, block.getX());
        getMaterials().getData(itemMeta).set(getMaterials().getKey("secondary.y"), PersistentDataType.INTEGER, block.getY());
        getMaterials().getData(itemMeta).set(getMaterials().getKey("secondary.z"), PersistentDataType.INTEGER, block.getZ());
        itemStack.setItemMeta(itemMeta);
    }
    public void setSecondary(String portalName, Location secondary) {
        var file = getFile(portalName);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("secondary.world", secondary.getWorld().getName());
        config.set("secondary.x", secondary.getBlockX());
        config.set("secondary.y", secondary.getBlockY());
        config.set("secondary.z", secondary.getBlockZ());
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean hasSecondary(ItemStack itemStack) {
        return getMaterials().getData(itemStack.getItemMeta()).has(getMaterials().getKey("secondary.world"), PersistentDataType.STRING)
                && getMaterials().getData(itemStack.getItemMeta()).has(getMaterials().getKey("secondary.x"), PersistentDataType.INTEGER)
                && getMaterials().getData(itemStack.getItemMeta()).has(getMaterials().getKey("secondary.y"), PersistentDataType.INTEGER)
                && getMaterials().getData(itemStack.getItemMeta()).has(getMaterials().getKey("secondary.z"), PersistentDataType.INTEGER);
    }
    public Location getSecondary(ItemStack itemStack) {
        if (hasSecondary(itemStack)) {
            var world = getInstance().getWorlds().get(getMaterials().getData(itemStack.getItemMeta()).get(getMaterials().getKey("secondary.world"), PersistentDataType.STRING));
            var x = getMaterials().getData(itemStack.getItemMeta()).get(getMaterials().getKey("secondary.x"), PersistentDataType.INTEGER);
            var y = getMaterials().getData(itemStack.getItemMeta()).get(getMaterials().getKey("secondary.y"), PersistentDataType.INTEGER);
            var z = getMaterials().getData(itemStack.getItemMeta()).get(getMaterials().getKey("secondary.z"), PersistentDataType.INTEGER);
            return new Location(world, x, y, z);
        } else return null;
    }
    public boolean hasWand(ItemStack itemStack) {
        if (!getMaterials().isAir(itemStack)) {
            return itemStack.getItemMeta().getPersistentDataContainer().has(getMaterials().getKey("wand"), PersistentDataType.BOOLEAN);
        } else return false;
    }
    public void getWand(Player player) {
        var itemStack = getMaterials().getItemStack("stone_axe", 1);
        var itemMeta = itemStack.getItemMeta();
        getMaterials().getData(itemMeta).set(getMaterials().getKey("wand"), PersistentDataType.BOOLEAN, true);
        itemMeta.setDisplayName(getInstance().getMessage().addColor("&6Portals"));
        itemStack.setItemMeta(itemMeta);
        getMaterials().giveItemStack(player, itemStack);
    }
    public boolean createPortal(String idName, String portalType, String warpName, Location primary, Location secondary) {
        if (!exists(idName)) {
            var file = getFile(idName);
            var config = YamlConfiguration.loadConfiguration(file);
            config.set("portal-type", portalType.toUpperCase());
            config.set("warp", warpName);
            config.set("primary.world", primary.getWorld().getName());
            config.set("primary.x", primary.getBlockX());
            config.set("primary.y", primary.getBlockY());
            config.set("primary.z", primary.getBlockZ());
            config.set("secondary.world", secondary.getWorld().getName());
            config.set("secondary.x", secondary.getBlockX());
            config.set("secondary.y", secondary.getBlockY());
            config.set("secondary.z", secondary.getBlockZ());
            try {
                config.save(file);
                return true;
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return false;
    }
    public String getIDName(Location location) {
        for (var fileName : getPortals().keySet()) {
            var centered = getPortals().get(fileName);
            var primary = centered[0];
            var secondary = centered[1];
            var x = location.getBlockX();
            var y = location.getBlockY();
            var z = location.getBlockZ();
            if (x >= primary.getX() && x <= secondary.getX() && y >= primary.getY() && y <= secondary.getY() && z >= primary.getZ() && z <= secondary.getZ()) {
                return fileName;
            }
        }
        return null;
    }
    public String getPortalType(Location location) {
        return getConfig(getIDName(location)).getString("portal-type");
    }
    public String getWarp(Location location) {
        return getConfig(getIDName(location)).getString("warp");
    }
    public boolean isWithin(Location location) {
        if (!getPortals().isEmpty()) {
            for (var fileName : getPortals().keySet()) {
                var centered = getPortals().get(fileName);
                var primary = centered[0];
                var secondary = centered[1];
                var x = location.getBlockX();
                var y = location.getBlockY();
                var z = location.getBlockZ();
                return x >= primary.getX() && x <= secondary.getX() && y >= primary.getY() && y <= secondary.getY() && z >= primary.getZ() && z <= secondary.getZ();
            }
        }
        return false;
    }
    public Map<String, Location[]> getPortals() {
        var folder = new File(getInstance().getDataFolder(), "portals");
        if (folder.exists() && folder.isDirectory()) {
            var portals = new HashMap<String, Location[]>();
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    var config = YamlConfiguration.loadConfiguration(file);
                    var fileName = file.getName().replace(".yml", "");
                    var primaryWorld = getInstance().getWorlds().get(config.getString("primary.world"));
                    var locationX1 = config.getInt("primary.x");
                    var locationY1 = config.getInt("primary.y");
                    var locationZ1 = config.getInt("primary.z");
                    var locationWorld2 = getInstance().getWorlds().get(config.getString("secondary.world"));
                    var locationX2 = config.getInt("secondary.x");
                    var locationY2 = config.getInt("secondary.y");
                    var locationZ2 = config.getInt("secondary.z");
                    var primaryLocation = new Location(primaryWorld, locationX1, locationY1, locationZ1);
                    var secondaryLocation = new Location(locationWorld2, locationX2, locationY2, locationZ2);
                    int x1 = primaryLocation.getBlockX(), x2 = secondaryLocation.getBlockX(), y1 = primaryLocation.getBlockY(), y2 = secondaryLocation.getBlockY(), z1 = primaryLocation.getBlockZ(), z2 = secondaryLocation.getBlockZ();
                    var primary = primaryLocation.clone();
                    var secondary = secondaryLocation.clone();
                    primary.setX(Math.min(x1, x2));
                    primary.setY(Math.min(y1, y2));
                    primary.setZ(Math.min(z1, z2));
                    secondary.setX(Math.max(x1, x2));
                    secondary.setY(Math.max(y1, y2));
                    secondary.setZ(Math.max(z1, z2));
                    portals.put(fileName, new Location[] {primary, secondary});
                }
            }
            return portals;
        } else return new HashMap<>();
    }
    public void reload() {
        var portals = new File(getInstance().getDataFolder(), "portals");
        if (portals.exists() && portals.isDirectory()) {
            for (var file : portals.listFiles()) {
                if (file.exists() && file.isFile()) {
                    var config = YamlConfiguration.loadConfiguration(file);
                    try {
                        config.load(file);
                    } catch (IOException | InvalidConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
