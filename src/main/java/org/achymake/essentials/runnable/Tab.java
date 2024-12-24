package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public record Tab(Player getPlayer, FileConfiguration getConfig) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private boolean isList(String path) {
        return getConfig().isList(path);
    }
    private List<String> getList(String path) {
        return getConfig().getStringList(path);
    }
    private boolean isEnable() {
        return getConfig().getBoolean("enable");
    }
    private String getDisplayName() {
        return getInstance().getUserdata(getPlayer()).getDisplayName();
    }
    private String getPrefix() {
        return getInstance().getUserdata(getPlayer()).getPrefix();
    }
    private String getSuffix() {
        return getInstance().getUserdata(getPlayer()).getSuffix();
    }
    @Override
    public void run() {
        if (isEnable()) {
            var world = getPlayer().getWorld().getName();
            if (isList("worlds." + world + ".header.lines")) {
                var text = getMessage().toString(getList("worlds." + world + ".header.lines"));
                var header = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), text));
                getPlayer().setPlayerListHeader(header);
            } else if (isList("header.lines")) {
                var text = getMessage().toString(getList("header.lines"));
                var header = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), text));
                getPlayer().setPlayerListHeader(header);
            }
            getPlayer().setPlayerListName(getPrefix() + getDisplayName() + getSuffix());
            if (isList("worlds." + world + ".footer.lines")) {
                var text = getMessage().toString(getList("worlds." + world + ".footer.lines"));
                var footer = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), text));
                getPlayer().setPlayerListFooter(footer);
            } else if (isList("footer.lines")) {
                var text = getMessage().toString(getList("footer.lines"));
                var footer = getMessage().addColor(getMessage().addPlaceholder(getPlayer(), text));
                getPlayer().setPlayerListFooter(footer);
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