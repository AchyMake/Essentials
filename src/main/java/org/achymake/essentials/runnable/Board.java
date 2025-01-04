package org.achymake.essentials.runnable;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.ScoreboardHandler;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

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
        return getScoreboardHandler().getScoreboardManager();
    }
    private Scoreboard getMainScoreboard() {
        return getScoreboardManager().getMainScoreboard();
    }
    private Scoreboard getNewScoreboard() {
        return getScoreboardManager().getNewScoreboard();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private @NotNull String getWorldName() {
        return getPlayer().getWorld().getName();
    }
    private @NotNull String getName() {
        return getPlayer().getName();
    }
    private String getTitle() {
        if (!getScoreboardHandler().hasTitle(getWorldName())) {
            return getMessage().addPlaceholder(getPlayer(), getScoreboardHandler().getTitle());
        } else return getMessage().addPlaceholder(getPlayer(), getScoreboardHandler().getTitle(getWorldName()));
    }
    private List<String> getLines() {
        var listed = new ArrayList<String>();
        if (getScoreboardHandler().isLine(getWorldName())) {
            for (var line : getScoreboardHandler().getLines(getWorldName())) {
                listed.add(getMessage().addPlaceholder(getPlayer(), line));
            }
        } else if (getScoreboardHandler().isLine()) {
            for (var line : getScoreboardHandler().getLines()) {
                listed.add(getMessage().addPlaceholder(getPlayer(), line));
            }
        }
        return listed;
    }
    private void update() {
        var scoreboard = getMainScoreboard();
        var sidebar = scoreboard.getObjective(getName() + "-board");
        if (sidebar != null) {
            if (getTitle() != null) {
                if (!getTitle().equals(sidebar.getDisplayName())) {
                    sidebar.setDisplayName(getTitle());
                }
                if (!getLines().isEmpty()) {
                    for (int i = 0; i < getLines().size(); i++) {
                        var team = scoreboard.getTeam(String.valueOf(i));
                        if (team != null) {
                            if (!getLines().get(i).equals(team.getPrefix())) {
                                team.setPrefix(getLines().get(i));
                            }
                        }
                    }
                }
            }
        } else create();
    }
    private void create() {
        if (getTitle() != null) {
            var scoreboard = getNewScoreboard();
            var sidebar = scoreboard.registerNewObjective(getName() + "-board", "yummy", getTitle());
            sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
            sidebar.numberFormat(NumberFormat.blank());
            if (!getLines().isEmpty()) {
                for (int i = 0; i < getLines().size(); i++) {
                    var team = scoreboard.registerNewTeam(String.valueOf(i));
                    team.addEntry(String.valueOf(i));
                    team.setPrefix(getLines().get(i));
                    sidebar.getScore(getLines().get(i)).setScore(i);
                }
            }
            setScoreboard(scoreboard);
        }
    }
    private void setScoreboard(Scoreboard scoreboard) {
        getPlayer().setScoreboard(scoreboard);
    }
    @Override
    public void run() {
        update();
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}