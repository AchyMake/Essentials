package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.runnable.Board;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScoreboardHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "scoreboard.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public ScoreboardManager getScoreboardManager() {
        return Bukkit.getScoreboardManager();
    }
    public FileConfiguration getConfig() {
        return config;
    }
    private boolean isEnable() {
        return config.getBoolean("enable");
    }
    private long getTick() {
        return config.getLong("tick");
    }
    private long getTick(String worldName) {
        return config.getLong("worlds." + worldName + ".tick");
    }
    private String getTitle() {
        return config.getString("title");
    }
    private String getTitle(String worldName) {
        return config.getString("worlds." + worldName + ".title");
    }
    private boolean isList() {
        return config.isList("lines");
    }
    private boolean isList(String worldName) {
        return config.isList("worlds." + worldName + ".lines");
    }
    public void apply(Player player) {
        if (isEnable()) {
            var world = player.getWorld().getName();
            if (getTitle(world) != null && isList(world)) {
                var taskID = getScheduler().runTimer(new Board(player), 0, getTick(world)).getTaskId();
                getUserdata().addTaskID(player, "board", taskID);
            } else if (getTitle() != null && isList()) {
                var taskID = getScheduler().runTimer(new Board(player), 0, getTick()).getTaskId();
                getUserdata().addTaskID(player, "board", taskID);
            }
        }
    }
    public void disable(Player player) {
        if (getUserdata().hasTaskID(player, "board")) {
            getUserdata().removeTask(player, "board");
            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
    }
    public boolean hasBoard(Player player) {
        return getUserdata().hasTaskID(player, "board");
    }
    private void setup() {
        var lines = new ArrayList<String>();
        lines.add("&ename&f: %essentials_display_name%");
        lines.add("&erank&f: %vault_prefix%%vault_rank%%vault_suffix%");
        lines.add("&ecoins&f:&a €%vault_eco_balance_formatted%");
        config.set("enable", false);
        config.set("tick", 20);
        config.set("title", "&6&lStats");
        config.set("lines", lines);
        config.set("worlds.world.tick", 20);
        config.set("worlds.world.title", "&6&lStats");
        config.set("worlds.world.lines", lines);
        config.set("worlds.test.tick", 20);
        config.set("worlds.test.title", "&6&lStats");
        config.set("worlds.test.lines", lines);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else setup();
    }
}