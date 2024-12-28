package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.TablistHandler;
import org.bukkit.configuration.file.FileConfiguration;
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
    private FileConfiguration getConfig() {
        return getTablistHandler().getConfig();
    }
    private String getName() {
        if (getConfig().isString("name")) {
            return getMessage().addPlaceholder(getPlayer(), getConfig().getString("name"));
        } else return getPlayer().getName();
    }
    private String getHeader() {
        return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("header.lines")));
    }
    private String getFooter() {
        return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("footer.lines")));
    }
    private String getNameWorld() {
        if (getConfig().isString("worlds." + getPlayer().getWorld().getName() + ".name")) {
            return getMessage().addPlaceholder(getPlayer(), getConfig().getString("worlds." + getPlayer().getWorld().getName() + ".name"));
        } else return getPlayer().getName();
    }
    private String getHeaderWorld() {
        return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("worlds." + getPlayer().getWorld().getName() + ".header.lines")));
    }
    private String getFooterWorld() {
        return getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("worlds." + getPlayer().getWorld().getName() + ".footer.lines")));
    }
    private boolean isEnable() {
        return getConfig().getBoolean("enable");
    }
    private void setHeader() {
        if (getHeaderWorld() != null) {
            if (!getHeaderWorld().equals(getPlayer().getPlayerListHeader())) {
                getPlayer().setPlayerListHeader(getHeaderWorld());
            }
        } else if (getHeader() != null) {
            if (!getHeader().equals(getPlayer().getPlayerListHeader())) {
                getPlayer().setPlayerListHeader(getHeader());
            }
        }
    }
    private void setName() {
        if (getNameWorld() != null) {
            if (!getNameWorld().equals(getPlayer().getPlayerListName())) {
                getPlayer().setPlayerListName(getNameWorld());
            }
        } else if (getName() != null) {
            if (!getName().equals(getPlayer().getPlayerListName())) {
                getPlayer().setPlayerListName(getName());
            }
        }
    }
    private void setFooter() {
        if (getFooterWorld() != null) {
            if (!getFooterWorld().equals(getPlayer().getPlayerListFooter())) {
                getPlayer().setPlayerListFooter(getFooterWorld());
            }
        } else if (getFooter() != null) {
            if (!getFooter().equals(getPlayer().getPlayerListFooter())) {
                getPlayer().setPlayerListFooter(getFooter());
            }
        }
    }
    @Override
    public void run() {
        if (isEnable()) {
            setHeader();
            setName();
            setFooter();
        }
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}