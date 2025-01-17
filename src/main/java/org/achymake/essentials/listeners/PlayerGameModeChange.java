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
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerGameModeChange() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        var mode = event.getNewGameMode().toString().toLowerCase();
        event.getPlayer().sendMessage(getMessage().get("events.gamemode.change", getMessage().get("gamemode." + mode)));
        getMessage().sendAll(getMessage().get("events.gamemode.notify", event.getPlayer().getName(), getMessage().get("gamemode." + mode)), "essentials.command.gamemode.notify");
    }
}