package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.bukkit.entity.Player;

public record Vanish(Player getPlayer) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    @Override
    public void run() {
        getMessage().sendActionBar(getPlayer(), getMessage().get("events.vanish.enable"));
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}