package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
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
        var gamemode = event.getNewGameMode();
        getMessage().send(player, "&6Your gamemode has changed to&f " + getMessage().toTitleCase(gamemode.name()));
        getMessage().sendAll(player.getName() + "&6 has changed gamemode to&f " + getMessage().toTitleCase(gamemode.name()), "essentials.event.gamemode_change.notify");
    }
}