package org.achymake.essentials.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerChangedChunkEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Location from;
    private final Location to;
    private boolean cancelled;
    public PlayerChangedChunkEvent(Player player, Location from, Location to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    public Player getPlayer() {
        return player;
    }
    public Location getFrom() {
        return from;
    }
    public Location getTo() {
        return to;
    }
    public boolean isCancelled() {
        return cancelled;
    }
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}