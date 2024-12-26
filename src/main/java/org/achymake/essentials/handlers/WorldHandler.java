package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public World get(String worldName) {
        return getInstance().getServer().getWorld(worldName);
    }
    public List<String> getListed() {
        var listed = new ArrayList<String>();
        getInstance().getServer().getWorlds().forEach(world -> listed.add(world.getName()));
        return listed;
    }
    public boolean setMorning(World world) {
        if (world != null) {
            world.setTime(0);
            return true;
        } else return false;
    }
    public boolean setDay(World world) {
        if (world != null) {
            world.setTime(1000);
            return true;
        } else return false;
    }
    public boolean setNoon(World world) {
        if (world != null) {
            world.setTime(6000);
            return true;
        } else return false;
    }
    public boolean setNight(World world) {
        if (world != null) {
            world.setTime(13000);
            return true;
        } else return false;
    }
    public boolean setMidnight(World world) {
        if (world != null) {
            world.setTime(18000);
            return true;
        } else return false;
    }
    public boolean setTime(World world, long value) {
        if (world != null) {
            world.setTime(value);
            return true;
        } else return false;
    }
    public boolean addTime(World world, long value) {
        if (world != null) {
            world.setTime(world.getTime() + value);
            return true;
        } else return false;
    }
    public boolean removeTime(World world, long value) {
        if (world != null) {
            world.setTime(world.getTime() - value);
            return true;
        } else return false;
    }
    public Block highestRandomBlock(World world, int spread) {
        var random = new Random();
        return world.getHighestBlockAt(random.nextInt(0, spread), random.nextInt(0, spread));
    }
    public void teleport(Player player, Location location, String name, int timer) {
        if (!getUserdata().hasTaskID(player, "teleport")) {
            if (!location.getChunk().isLoaded()) {
                location.getChunk().load();
            }
            if (timer > 0) {
                getMessage().sendActionBar(player, getMessage().get("events.teleport.post", String.valueOf(timer)));
                var taskID = getInstance().getScheduleHandler().runLater(new Runnable() {
                    @Override
                    public void run() {
                        getMessage().sendActionBar(player, getMessage().get("events.teleport.success", name));
                        player.teleport(location);
                        getUserdata().removeTask(player, "teleport");
                    }
                }, timer * 20L).getTaskId();
                getUserdata().addTaskID(player, "teleport", taskID);
            } else {
                getMessage().sendActionBar(player, getMessage().get("events.teleport.success", name));
                player.teleport(location);
            }
        } else player.sendMessage(getMessage().get("events.teleport.has-task"));
    }
    public void randomTeleport(Player player) {
        if (player != null) {
            getMessage().sendActionBar(player, getMessage().get("commands.rtp.post-teleport"));
            getScheduler().runLater(new Runnable() {
                @Override
                public void run() {
                    var block = highestRandomBlock(get(getConfig().getString("commands.rtp.world")), getConfig().getInt("commands.rtp.spread"));
                    if (block.isLiquid()) {
                        getMessage().sendActionBar(player, getMessage().get("commands.rtp.liquid"));
                        randomTeleport(player);
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
}