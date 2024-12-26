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
    private FileConfiguration getConfig() {
        return getTablistHandler().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    @Override
    public void run() {
        if (getConfig().getBoolean("enable")) {
            var world = getPlayer().getWorld().getName();
            if (getConfig().isList("worlds." + world + ".header.lines")) {
                getPlayer().setPlayerListHeader(getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("worlds." + world + ".header.lines"))));
            } else if (getConfig().isList("header.lines")) {
                getPlayer().setPlayerListHeader(getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("header.lines"))));
            }
            if (getConfig().isString("worlds." + world + ".name")) {
                getPlayer().setPlayerListName(getMessage().addPlaceholder(getPlayer(), getConfig().getString(world + ".name")));
            } else if (getConfig().isString("name")) {
                getPlayer().setPlayerListName(getMessage().addPlaceholder(getPlayer(), getConfig().getString("name")));
            }
            if (getConfig().isList("worlds." + world + ".footer.lines")) {
                getPlayer().setPlayerListFooter(getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("worlds." + world + ".footer.lines"))));
            } else if (getConfig().isList("footer.lines")) {
                getPlayer().setPlayerListFooter(getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("footer.lines"))));
            }
        }
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}