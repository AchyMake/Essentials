package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.bukkit.entity.Player;

public record Vanish(Player getPlayer) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private boolean isVanish(Player player) {
        return getInstance().getVanishHandler().isVanish(player);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private String enable() {
        return getMessage().get("enable");
    }
    private String disable() {
        return getMessage().get("disable");
    }
    @Override
    public void run() {
        if (isVanish(getPlayer())) {
            getMessage().sendActionBar(getPlayer(), getMessage().get("events.vanish", enable()));
        } else getMessage().sendActionBar(getPlayer(), getMessage().get("events.vanish", disable()));
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}