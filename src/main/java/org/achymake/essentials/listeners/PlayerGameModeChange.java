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
        event.getPlayer().sendMessage(getMessage().get("events.gamemode.change", getMessage().toTitleCase(event.getNewGameMode().name())));
        getMessage().sendAll(getMessage().get("events.gamemode.notify", event.getPlayer().getName(), getMessage().toTitleCase(event.getNewGameMode().name())), "essentials.event.gamemode_change.notify");
    }
}