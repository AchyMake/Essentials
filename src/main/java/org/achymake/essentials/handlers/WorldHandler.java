package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class WorldHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private RandomHandler getRandomHandler() {
        return getInstance().getRandomHandler();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    /**
     * get world
     * @param worldName string
     * @return world if worldName exists else null
     * @since many moons ago
     */
    public World get(String worldName) {
        return getInstance().getServer().getWorld(worldName);
    }
    /**
     * get listed
     * @return list world
     * @since many moons ago
     */
    public List<World> getListed() {
        return new ArrayList<>(getInstance().getServer().getWorlds());
    }
    /**
     * set morning
     * @param world world
     * @since many moons ago
     */
    public void setMorning(World world) {
        world.setTime(0);
    }
    /**
     * set day
     * @param world world
     * @since many moons ago
     */
    public void setDay(World world) {
        world.setTime(1000);
    }
    /**
     * set noon
     * @param world world
     * @since many moons ago
     */
    public void setNoon(World world) {
        world.setTime(6000);
    }
    /**
     * set night
     * @param world world
     * @since many moons ago
     */
    public void setNight(World world) {
        world.setTime(13000);
    }
    /**
     * set midnight
     * @param world world
     * @since many moons ago
     */
    public void setMidnight(World world) {
        world.setTime(18000);
    }
    /**
     * set time
     * @param world world
     * @param value long
     * @since many moons ago
     */
    public void setTime(World world, long value) {
        world.setTime(value);
    }
    /**
     * add time
     * @param world world
     * @param value long
     * @since many moons ago
     */
    public void addTime(World world, long value) {
        world.setTime(world.getTime() + value);
    }
    /**
     * remove time
     * @param world world
     * @param value long
     * @since many moons ago
     */
    public void removeTime(World world, long value) {
        world.setTime(world.getTime() - value);
    }
    /**
     * drop itemStack
     * @param location location
     * @param itemStack itemStack
     * @since many moons ago
     */
    public Item dropItem(Location location, ItemStack itemStack) {
        return location.getWorld().dropItem(location, itemStack);
    }
    /**
     * update spawner
     * @param blockPlaced block
     * @param heldItem itemStack
     * @since many moons ago
     */
    public void updateSpawner(Block blockPlaced, ItemStack heldItem) {
        var container = getMaterials().getData(heldItem.getItemMeta());
        if (container.has(getInstance().getKey("entity_type"), PersistentDataType.STRING)) {
            var creatureSpawner = (CreatureSpawner) blockPlaced.getState();
            creatureSpawner.setSpawnedType(EntityType.valueOf(container.get(getInstance().getKey("entity_type"), PersistentDataType.STRING)));
            creatureSpawner.update();
        }
    }
    /**
     * drop spawner
     * @param block block
     * @return item
     * @since many moons ago
     */
    public Item dropSpawner(Block block) {
        var creatureSpawner = (CreatureSpawner) block.getState();
        var itemStack = getMaterials().getItemStack("spawner", 1);
        if (creatureSpawner.getSpawnedType() != null) {
            return dropItem(block.getLocation().add(0.5, 0.3, 0.5), getMaterials().getSpawner(creatureSpawner.getSpawnedType().toString(), 1));
        } else return dropItem(block.getLocation().add(0.5, 0.3, 0.5), itemStack);
    }
    /**
     * gets the highest random block
     * @param world world
     * @param spread integer
     * @return block
     * @since many moons ago
     */
    public Block highestRandomBlock(World world, int spread) {
        return world.getHighestBlockAt(getRandomHandler().nextInt(0, spread), getRandomHandler().nextInt(0, spread));
    }
    public int getTeleportDelay() {
        return getConfig().getInt("teleport.delay");
    }
    /**
     * teleport player
     * @param player target
     * @param location location
     * @param name string
     * @param seconds integer
     * @since many moons ago
     */
    public void teleport(Player player, Location location, String name, int seconds) {
        if (!getUserdata().hasTaskID(player, "teleport")) {
            if (!location.getChunk().isLoaded()) {
                location.getChunk().load();
            }
            if (seconds > 0) {
                getMessage().sendActionBar(player, getMessage().get("events.teleport.post", String.valueOf(seconds)));
                var taskID = getInstance().getScheduleHandler().runLater(new Runnable() {
                    @Override
                    public void run() {
                        getMessage().sendActionBar(player, getMessage().get("events.teleport.success", name));
                        player.teleport(location);
                        getUserdata().removeTask(player, "teleport");
                    }
                }, seconds * 20L).getTaskId();
                getUserdata().addTaskID(player, "teleport", taskID);
            } else {
                getMessage().sendActionBar(player, getMessage().get("events.teleport.success", name));
                player.teleport(location);
            }
        } else player.sendMessage(getMessage().get("events.teleport.has-task"));
    }
    /**
     * teleport player random using main config settings
     * @param player target
     * @since many moons ago
     */
    public void randomTeleport(Player player) {
        getMessage().sendActionBar(player, getMessage().get("commands.rtp.post-teleport"));
        var block = highestRandomBlock(get(getConfig().getString("commands.rtp.world")), getConfig().getInt("commands.rtp.spread"));
        var taskID = getScheduler().runLater(new Runnable() {
            @Override
            public void run() {
                if (block.isLiquid()) {
                    getMessage().sendActionBar(player, getMessage().get("commands.rtp.liquid"));
                    randomTeleport(player);
                } else {
                    if (!block.getChunk().isLoaded()) {
                        block.getChunk().load();
                    }
                    getMessage().sendActionBar(player, getMessage().get("commands.rtp.teleport"));
                    player.teleport(block.getLocation().add(0.5,1,0.5));
                    getUserdata().removeTask(player, "rtp");
                }
            }
        }, 3).getTaskId();
        getUserdata().addTaskID(player, "rtp", taskID);
    }
    public void summonLightning(Location location) {
        if (location == null)return;
        if (!location.getChunk().isLoaded())return;
        location.getWorld().strikeLightning(location);
    }
    public boolean isAir(Block block) {
        return block == null || block.getType().equals(getMaterials().get("air"));
    }
}