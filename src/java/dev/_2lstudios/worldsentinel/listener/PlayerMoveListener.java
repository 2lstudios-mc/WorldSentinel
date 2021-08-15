package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.Region;
import dev._2lstudios.worldsentinel.region.RegionPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import dev._2lstudios.worldsentinel.events.RegionEnterEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import dev._2lstudios.worldsentinel.region.RegionPlayerManager;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;

class PlayerMoveListener implements Listener {
    private final PluginManager pluginManager;
    private final RegionManager regionManager;
    private final RegionPlayerManager regionPlayerManager;

    PlayerMoveListener(final Plugin plugin, final RegionManager regionManager,
            final RegionPlayerManager regionPlayerManager) {
        this.pluginManager = plugin.getServer().getPluginManager();
        this.regionManager = regionManager;
        this.regionPlayerManager = regionPlayerManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final RegionPlayer regionPlayer = this.regionPlayerManager.getPlayer(player);
        final Region oldRegion = regionPlayer.getRegion();
        final Region newRegion = this.regionManager.getRegionInside(event.getTo());
        if (oldRegion != newRegion) {
            final RegionEnterEvent regionEnterEvent = new RegionEnterEvent(player, oldRegion, newRegion);
            this.pluginManager.callEvent((Event) regionEnterEvent);
            if (!regionEnterEvent.isCancelled()) {
                regionPlayer.setRegion(newRegion);
            } else {
                event.setCancelled(true);
            }
        }
    }
}
