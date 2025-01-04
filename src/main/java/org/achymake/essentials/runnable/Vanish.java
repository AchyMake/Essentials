package org.achymake.essentials.runnable;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.VanishHandler;
import org.bukkit.entity.Player;

public record Vanish(Player getPlayer) implements Runnable {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private boolean isVanish() {
        return getVanishHandler().isVanish(getPlayer());
    }
    private void enable() {
        getMessage().sendActionBar(getPlayer(), getMessage().get("events.vanish", getMessage().get("enable")));
    }
    private void disable() {
        getMessage().sendActionBar(getPlayer(), getMessage().get("events.vanish", getMessage().get("disable")));
    }
    @Override
    public void run() {
        if (isVanish()) {
            enable();
        } else disable();
    }
    @Override
    public Player getPlayer() {
        return getPlayer;
    }
}