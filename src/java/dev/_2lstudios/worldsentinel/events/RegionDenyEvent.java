// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.events;

import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class RegionDenyEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS;
    private final Event event;
    private Region region;
    private boolean cancelled;

    static {
        HANDLERS = new HandlerList();
    }

    public RegionDenyEvent(final Event event, final Region region) {
        this.event = event;
        this.region = region;
        this.cancelled = false;
    }

    public Event getEvent() {
        return this.event;
    }

    public Region getRegion() {
        return this.region;
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
        return RegionDenyEvent.HANDLERS;
    }
}
