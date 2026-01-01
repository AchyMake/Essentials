package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                var taskID = getInstance().getScheduleHandler().runLater(() -> {
                    getMessage().sendActionBar(player, getMessage().get("events.teleport.success", name));
                    player.teleport(location);
                    getUserdata().removeTask(player, "teleport");
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
        var taskID = getScheduler().runLater(() -> {
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
        }, 3).getTaskId();
        getUserdata().addTaskID(player, "rtp", taskID);
    }
    public void summonLightning(Location location) {
        var world = location.getWorld();
        if (world == null)return;
        if (!location.getChunk().isLoaded())return;
        world.strikeLightning(location);
    }
    public void spawnParticle(Location location, String particleType, int count, double offsetX, double offsetY, double offsetZ) {
        var world = location.getWorld();
        if (world == null)return;
        world.spawnParticle(Particle.valueOf(particleType), location, count, offsetX, offsetY, offsetZ, 0.0);
    }
    public void playSound(Location location, String soundType, double volume, double pitch) {
        var world = location.getWorld();
        if (world == null)return;
        world.playSound(location, Sound.valueOf(soundType), (float) volume, (float) pitch);
    }
    public void playSound(Entity entity, String soundType, double volume, double pitch) {
        var world = entity.getWorld();
        world.playSound(entity, Sound.valueOf(soundType), (float) volume, (float) pitch);
    }
}