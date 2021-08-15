package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import dev._2lstudios.worldsentinel.region.RegionPlayerManager;
import org.bukkit.event.Listener;

public class PlayerQuitListener implements Listener {
    private final RegionPlayerManager regionPlayerManager;

    public PlayerQuitListener(final RegionPlayerManager regionPlayerManager) {
        this.regionPlayerManager = regionPlayerManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerQuitEvent event) {
        this.regionPlayerManager.removePlayer(event.getPlayer());
    }
}
