package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.TablistHandler;
import org.bukkit.entity.Player;

public record Tab(Player getPlayer) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private TablistHandler getTablistHandler() {
        return getInstance().getTablistHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private String getPlayerListHeader() {
        return getPlayer().getPlayerListHeader();
    }
    private String getPlayerListName() {
        return getPlayer().getPlayerListName();
    }
    private String getPlayerListFooter() {
        return getPlayer().getPlayerListFooter();
    }
    private String getHeader() {
        var world = getPlayer().getWorld();
        if (!getTablistHandler().hasHeaderLines(world.getName())) {
            if (!getTablistHandler().hasHeaderLines()) {
                return null;
            } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getHeaderLines()));
        } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getHeaderLines(world.getName())));
    }
    private String getName() {
        var world = getPlayer().getWorld();
        if (!getTablistHandler().hasName(world.getName())) {
            if (!getTablistHandler().hasName()) {
                return getPlayer().getName();
            } else return getMessage().addPlaceholder(getPlayer(), getTablistHandler().getName());
        } else return getMessage().addPlaceholder(getPlayer(), getTablistHandler().getName(world.getName()));
    }
    private String getFooter() {
        var world = getPlayer().getWorld();
        if (!getTablistHandler().hasFooterLines(world.getName())) {
            if (!getTablistHandler().hasFooterLines()) {
                return null;
            } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getFooterLines()));
        } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getFooterLines(world.getName())));
    }
    @Override
    public void run() {
        if (getPlayerListHeader() != null) {
            if (getHeader() != null) {
                if (!getPlayerListHeader().equals(getHeader())) {
                    getPlayer().setPlayerListHeader(getHeader());
                }
            }
        } else getPlayer().setPlayerListHeader(getHeader());
        if (!getPlayerListName().equals(getName())) {
            getPlayer().setPlayerListName(getName());
        }
        getPlayer().setPlayerListOrder(getTablistHandler().getWeightedPlayers().get(getPlayer()));
        if (getPlayerListFooter() != null) {
            if (getFooter() != null) {
                if (!getPlayerListFooter().equals(getFooter())) {
                    getPlayer().setPlayerListFooter(getFooter());
                }
            }
        } else getPlayer().setPlayerListFooter(getFooter());
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}