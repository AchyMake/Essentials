package org.achymake.essentials.runnable;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

public record Board(Player getPlayer) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ScoreboardHandler getScoreboardHandler() {
        return getInstance().getScoreboardHandler();
    }
    private ScoreboardManager getScoreboardManager() {
        return Bukkit.getScoreboardManager();
    }
    private Scoreboard getScoreboard() {
        return getPlayer.getScoreboard();
    }
    private Objective getObjective(String objective) {
        return getScoreboardManager().getMainScoreboard().getObjective(objective);
    }
    private Scoreboard getNewScoreboard() {
        return getScoreboardManager().getNewScoreboard();
    }
    private FileConfiguration getConfig() {
        return getScoreboardHandler().getConfig();
    }
    private boolean isEnable() {
        return getConfig().getBoolean("enable");
    }
    private String getTitle() {
        return getMessage().addPlaceholder(getPlayer, getConfig().getString("title"));
    }
    private String getTitle(String worldName) {
        return getMessage().addPlaceholder(getPlayer, getConfig().getString("worlds." + worldName + ".title"));
    }
    private List<String> getLines() {
        var listed = new ArrayList<String>();
        var lines = getConfig().getStringList("lines").reversed();
        if (!lines.isEmpty()) {
            for (var line : lines) {
                listed.add(getMessage().addPlaceholder(getPlayer, line));
            }
        }
        return listed;
    }
    private List<String> getLines(String worldName) {
        var listed = new ArrayList<String>();
        var lines = getConfig().getStringList("worlds." + worldName + ".lines").reversed();
        if (!lines.isEmpty()) {
            for (var line : lines) {
                listed.add(getMessage().addPlaceholder(getPlayer, line));
            }
        }
        return listed;
    }
    private String getWorldName() {
        return getPlayer.getWorld().getName();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private void update() {
        var scoreboard = getScoreboard();
        var sidebar = getObjective("board");
        if (sidebar != null) {
            if (getTitle(getWorldName()) != null) {
                if (!sidebar.getDisplayName().equals(getTitle(getWorldName()))) {
                    sidebar.setDisplayName(getTitle(getWorldName()));
                }
                if (!getLines(getWorldName()).isEmpty()) {
                    for (int i = 0; i < getLines(getWorldName()).size(); i++) {
                        var team = scoreboard.getTeam(String.valueOf(i));
                        if (team != null) {
                            if (!team.getPrefix().equals(getLines(getWorldName()).get(i))) {
                                team.setPrefix(getLines(getWorldName()).get(i));
                            }
                        }
                    }
                }
            } else if (getTitle() != null) {
                if (!sidebar.getDisplayName().equals(getTitle())) {
                    sidebar.setDisplayName(getTitle());
                }
                if (!getLines().isEmpty()) {
                    for (int i = 0; i < getLines().size(); i++) {
                        var team = scoreboard.getTeam(String.valueOf(i));
                        if (team != null) {
                            if (!team.getPrefix().equals(getLines().get(i))) {
                                team.setPrefix(getLines().get(i));
                            }
                        }
                    }
                }
            }
        } else create();
    }
    private void create() {
        if (getTitle(getWorldName()) != null) {
            var scoreboard = getNewScoreboard();
            var sidebar = scoreboard.registerNewObjective("board", "yummy", getTitle(getWorldName()));
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
            sidebar.numberFormat(NumberFormat.blank());
            if (!getLines(getWorldName()).isEmpty()) {
                for (int i = 0; i < getLines(getWorldName()).size(); i++) {
                    var team = scoreboard.getTeam(String.valueOf(i));
                    if (team != null) {
                        team.addEntry(String.valueOf(i));
                        team.setPrefix(getLines(getWorldName()).get(i));
                    } else {
                        var newTeam = scoreboard.registerNewTeam(String.valueOf(i));
                        newTeam.addEntry(String.valueOf(i));
                        newTeam.setPrefix(getLines(getWorldName()).get(i));
                    }
                    sidebar.getScore(getLines(getWorldName()).get(i)).setScore(i);
                }
            }
            getPlayer.setScoreboard(scoreboard);
        } else if (getTitle() != null) {
            var scoreboard = getNewScoreboard();
            var sidebar = scoreboard.registerNewObjective("board", "yummy", getTitle());
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
            sidebar.numberFormat(NumberFormat.blank());
            if (!getLines().isEmpty()) {
                for (int i = 0; i < getLines().size(); i++) {
                    var team = scoreboard.getTeam(String.valueOf(i));
                    if (team != null) {
                        team.addEntry(String.valueOf(i));
                        team.setPrefix(getLines().get(i));
                    } else {
                        var newTeam = scoreboard.registerNewTeam(String.valueOf(i));
                        newTeam.addEntry(String.valueOf(i));
                        newTeam.setPrefix(getLines().get(i));
                    }
                    sidebar.getScore(getLines().get(i)).setScore(i);
                }
            }
            getPlayer.setScoreboard(scoreboard);
        }
    }
    @Override
    public void run() {
        if (isEnable()) {
            update();
        } else getPlayer.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}