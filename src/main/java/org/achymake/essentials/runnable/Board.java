package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public record Board(Player getPlayer, FileConfiguration getConfig) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ScoreboardManager getScoreboardManager() {
        return getInstance().getServer().getScoreboardManager();
    }
    private Scoreboard getScoreboard() {
        return getScoreboardManager().getMainScoreboard();
    }
    private Objective getObjective(String objective) {
        return getScoreboardManager().getMainScoreboard().getObjective(objective);
    }
    private Scoreboard getNewScoreboard() {
        return getScoreboardManager().getNewScoreboard();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private String getString(String path) {
        return getConfig().getString(path);
    }
    private int getInt(String path) {
        return getConfig().getInt(path);
    }
    private boolean isList(String path) {
        return getConfig().isList(path);
    }
    private List<String> getList(String path) {
        return getConfig().getStringList(path);
    }
    private boolean isSection(String section) {
        return getConfig().isConfigurationSection(section);
    }
    private boolean isEnable() {
        return getConfig().getBoolean("enable");
    }
    @Override
    public void run() {
        if (isEnable()) {
            var world = getPlayer().getWorld().getName();
            if (isSection("worlds." + world)) {
                if (isList("worlds." + world + ".lines")) {
                    var title = getString("worlds." + world + ".title");
                    if (title != null) {
                        var scoreboardTick = getInt("worlds." + world + ".tick");
                        if (scoreboardTick >= 3) {
                            var lines = getList("worlds." + world + ".lines").reversed();
                            if (!lines.isEmpty()) {
                                var scoreboard = getScoreboard();
                                var objective = getObjective("board");
                                if (objective != null) {
                                    var displayName = objective.getDisplayName();
                                    var test = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), title));
                                    if (!displayName.equals(test)) {
                                        objective.setDisplayName(test);
                                    }
                                    for (int i = 0; i < lines.size(); i++) {
                                        var team = scoreboard.getTeam(String.valueOf(i));
                                        if (team != null) {
                                            var prefix = team.getPrefix();
                                            var test2 = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), lines.get(i)));
                                            if (!prefix.equals(test2)) {
                                                team.setPrefix(test2);
                                            }
                                        }
                                    }
                                    getPlayer().setScoreboard(scoreboard);
                                } else {
                                    var board = getNewScoreboard();
                                    var object = board.registerNewObjective("board", "yummy", getMessage().addColor(getMessage().addPlaceholder(getPlayer(), title)));
                                    object.setDisplaySlot(DisplaySlot.SIDEBAR);
                                    for (int i = 0; i < lines.size(); i++) {
                                        var message = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), lines.get(i)));
                                        var team = board.getTeam(String.valueOf(i));
                                        if (team != null) {
                                            team.addEntry(String.valueOf(i));
                                            team.setPrefix(message);
                                        } else {
                                            var newTeam = board.registerNewTeam(String.valueOf(i));
                                            newTeam.addEntry(String.valueOf(i));
                                            newTeam.setPrefix(message);
                                        }
                                        object.getScore(message).setScore(i);
                                    }
                                    getPlayer().setScoreboard(board);
                                }
                            }
                        }
                    }
                }
            } else if (isList("lines")) {
                var title = getString("title");
                if (title != null) {
                    var scoreboardTick = getInt("tick");
                    if (scoreboardTick >= 3) {
                        var lines = getList("lines").reversed();
                        if (!lines.isEmpty()) {
                            var scoreboard = getScoreboard();
                            var objective = getObjective("board");
                            if (objective != null) {
                                var displayName = objective.getDisplayName();
                                var test = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), title));
                                if (!displayName.equals(test)) {
                                    objective.setDisplayName(test);
                                }
                                for (int i = 0; i < lines.size(); i++) {
                                    var team = scoreboard.getTeam(String.valueOf(i));
                                    if (team != null) {
                                        var prefix = team.getPrefix();
                                        var test2 = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), lines.get(i)));
                                        if (!prefix.equals(test2)) {
                                            team.setPrefix(test2);
                                        }
                                    }
                                }
                                getPlayer().setScoreboard(scoreboard);
                            } else {
                                var board = getNewScoreboard();
                                var object = board.registerNewObjective("board", "yummy", getMessage().addColor(getMessage().addPlaceholder(getPlayer(), title)));
                                object.setDisplaySlot(DisplaySlot.SIDEBAR);
                                for (int i = 0; i < lines.size(); i++) {
                                    var message = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), lines.get(i)));
                                    var team = board.getTeam(String.valueOf(i));
                                    if (team != null) {
                                        team.addEntry(String.valueOf(i));
                                        team.setPrefix(message);
                                    } else {
                                        var newTeam = board.registerNewTeam(String.valueOf(i));
                                        newTeam.addEntry(String.valueOf(i));
                                        newTeam.setPrefix(message);
                                    }
                                    object.getScore(message).setScore(i);
                                }
                                getPlayer().setScoreboard(board);
                            }
                        }
                    }
                }
            }
        } else getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }
    @Override
    public FileConfiguration getConfig() {
        return getConfig;
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}