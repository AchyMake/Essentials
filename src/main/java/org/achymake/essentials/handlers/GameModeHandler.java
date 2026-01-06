package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameModeHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    /**
     * set game mode
     * @param player target
     * @param gameMode string
     * @return true if mode is gamemode else false
     * @since many moons ago
     */
    public boolean setGameMode(Player player, String gameMode) {
        if (gameMode.equalsIgnoreCase("adventure")) {
            player.setGameMode(get(gameMode));
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.adventure")));
            return true;
        } else if (gameMode.equalsIgnoreCase("creative")) {
            player.setGameMode(get(gameMode));
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.creative")));
            return true;
        } else if (gameMode.equalsIgnoreCase("spectator")) {
            player.setGameMode(get(gameMode));
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.spectator")));
            return true;
        } else if (gameMode.equalsIgnoreCase("survival")) {
            player.setGameMode(get(gameMode));
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.survival")));
            return true;
        } else return false;
    }
    public GameMode get(String gameMode) {
        if (gameMode.equalsIgnoreCase("adventure")) {
            return GameMode.ADVENTURE;
        } else if (gameMode.equalsIgnoreCase("creative")) {
            return GameMode.CREATIVE;
        } else if (gameMode.equalsIgnoreCase("spectator")) {
            return GameMode.SPECTATOR;
        } else if (gameMode.equalsIgnoreCase("survival")) {
            return GameMode.SURVIVAL;
        } else return GameMode.SURVIVAL;
    }
}