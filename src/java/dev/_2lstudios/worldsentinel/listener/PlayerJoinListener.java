// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import dev._2lstudios.worldsentinel.region.RegionPlayerManager;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener {
    private final RegionPlayerManager regionPlayerManager;

    public PlayerJoinListener(final RegionPlayerManager regionPlayerManager) {
        this.regionPlayerManager = regionPlayerManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.regionPlayerManager.addPlayer(event.getPlayer());
    }
}
