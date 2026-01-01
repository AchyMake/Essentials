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
     * @param mode string
     * @return true if mode is gamemode else false
     * @since many moons ago
     */
    public boolean setGameMode(Player player, String mode) {
        if (mode.equalsIgnoreCase("adventure")) {
            player.setGameMode(GameMode.ADVENTURE);
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.adventure")));
            return true;
        } else if (mode.equalsIgnoreCase("creative")) {
            player.setGameMode(GameMode.CREATIVE);
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.creative")));
            return true;
        } else if (mode.equalsIgnoreCase("spectator")) {
            player.setGameMode(GameMode.SPECTATOR);
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.spectator")));
            return true;
        } else if (mode.equalsIgnoreCase("survival")) {
            player.setGameMode(GameMode.SURVIVAL);
            getMessage().sendActionBar(player, getMessage().get("gamemode.change", getMessage().get("gamemode.survival")));
            return true;
        } else return false;
    }
}