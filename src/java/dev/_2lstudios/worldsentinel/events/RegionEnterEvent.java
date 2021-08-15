package dev._2lstudios.worldsentinel.events;

import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class RegionEnterEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS;
    private final Player player;
    private Region oldRegion;
    private Region newRegion;
    private boolean cancelled;

    static {
        HANDLERS = new HandlerList();
    }

    public RegionEnterEvent(final Player player, final Region oldRegion, final Region newRegion) {
        this.player = player;
        this.oldRegion = oldRegion;
        this.newRegion = newRegion;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Region getOldRegion() {
        return this.oldRegion;
    }

    public Region getNewRegion() {
        return this.newRegion;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return RegionEnterEvent.HANDLERS;
    }
}
