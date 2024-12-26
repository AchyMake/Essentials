package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.runnable.Board;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScoreboardHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "scoreboard.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public FileConfiguration getConfig() {
        return config;
    }
    public int getInt(String path) {
        return config.getInt(path);
    }
    public boolean isList(String path) {
        return config.isList(path);
    }
    public boolean isSection(String section) {
        return config.isConfigurationSection(section);
    }
    public boolean isEnable() {
        return config.getBoolean("enable");
    }
    public boolean hasBoard(Player player) {
        return getInstance().getUserdata().hasTaskID(player, "board");
    }
    public void apply(Player player) {
        if (isEnable()) {
            var world = player.getWorld().getName();
            if (isSection("worlds." + world)) {
                if (isList("worlds." + world + ".lines")) {
                    var tick = getInt("worlds." + world + ".tick");
                    var taskID = getScheduler().runTimer(new Board(player), tick, 3).getTaskId();
                    getInstance().getUserdata().addTaskID(player, "board", taskID);
                }
            } else if (isList("lines")) {
                var tick = getInt("tick");
                var taskID = getScheduler().runTimer(new Board(player), tick, 3).getTaskId();
                getInstance().getUserdata().addTaskID(player, "board", taskID);
            }
        }
    }
    public void disable(Player player) {
        if (getInstance().getUserdata().hasTaskID(player, "board")) {
            getInstance().getUserdata().removeTask(player, "board");
            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
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