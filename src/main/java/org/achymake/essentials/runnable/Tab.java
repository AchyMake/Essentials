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
    private String getWorldName() {
        return getPlayer().getWorld().getName();
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
        if (!getTablistHandler().hasHeaderLines(getWorldName())) {
            if (!getTablistHandler().hasHeaderLines()) {
                return null;
            } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getHeaderLines()));
        } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getHeaderLines(getWorldName())));
    }
    private String getName() {
        if (!getTablistHandler().hasName(getWorldName())) {
            if (!getTablistHandler().hasName()) {
                return getPlayer().getName();
            } else return getMessage().addPlaceholder(getPlayer(), getTablistHandler().getName());
        } else return getMessage().addPlaceholder(getPlayer(), getTablistHandler().getName(getWorldName()));
    }
    private String getFooter() {
        if (!getTablistHandler().hasFooterLines(getWorldName())) {
            if (!getTablistHandler().hasFooterLines()) {
                return null;
            } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getFooterLines()));
        } else return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getTablistHandler().getFooterLines(getWorldName())));
    }
    @Override
    public void run() {
        if (getPlayerListHeader() != null) {
            if (getHeader() != null) {
                if (!getPlayerListHeader().equals(getHeader())) {
                    getPlayer().setPlayerListHeader(getHeader());
                }
            }
        } else if (getHeader() != null) {
            getPlayer().setPlayerListHeader(getHeader());
        }
        if (!getPlayerListName().equals(getName())) {
            getPlayer().setPlayerListName(getName());
        }
        if (getPlayerListFooter() != null) {
            if (getFooter() != null) {
                if (!getPlayerListFooter().equals(getFooter())) {
                    getPlayer().setPlayerListFooter(getFooter());
                }
            }
        } else if (getFooter() != null) {
            getPlayer().setPlayerListFooter(getFooter());
        }
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}