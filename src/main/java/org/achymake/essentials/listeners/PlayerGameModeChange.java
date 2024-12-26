package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerGameModeChange implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerGameModeChange() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        var player = event.getPlayer();
        if (event.getNewGameMode().equals(GameMode.ADVENTURE)) {
            var mode = getMessage().get("gamemode.adventure");
            player.sendMessage(getMessage().get("events.gamemode.change", mode));
            getMessage().sendAll(getMessage().get("events.gamemode.notify", player.getName(), mode), "essentials.command.gamemode.notify");
        } else if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
            var mode = getMessage().get("gamemode.creative");
            player.sendMessage(getMessage().get("events.gamemode.change", mode));
            getMessage().sendAll(getMessage().get("events.gamemode.notify", player.getName(), mode), "essentials.command.gamemode.notify");
        } else if (event.getNewGameMode().equals(GameMode.SPECTATOR)) {
            var mode = getMessage().get("gamemode.spectator");
            player.sendMessage(getMessage().get("events.gamemode.change", mode));
            getMessage().sendAll(getMessage().get("events.gamemode.notify", player.getName(), mode), "essentials.command.gamemode.notify");
        } else if (event.getNewGameMode().equals(GameMode.SURVIVAL)) {
            var mode = getMessage().get("gamemode.survival");
            player.sendMessage(getMessage().get("events.gamemode.change", mode));
            getMessage().sendAll(getMessage().get("events.gamemode.notify", player.getName(), mode), "essentials.command.gamemode.notify");
        }
    }
}