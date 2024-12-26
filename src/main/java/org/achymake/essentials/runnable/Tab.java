package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public record Tab(Player getPlayer, FileConfiguration getConfig) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
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
            getPlayer().setPlayerListName(getMessage().addPlaceholder(getPlayer(), getInstance().getConfig().getString("tablist.format")));
            if (getConfig().isList("worlds." + world + ".footer.lines")) {
                getPlayer().setPlayerListFooter(getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("worlds." + world + ".footer.lines"))));
            } else if (getConfig().isList("footer.lines")) {
                getPlayer().setPlayerListFooter(getMessage().addPlaceholder(getPlayer(), getMessage().toString(getConfig().getStringList("footer.lines"))));
            }
        }
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