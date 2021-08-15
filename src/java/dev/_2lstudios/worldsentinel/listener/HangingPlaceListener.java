package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingPlaceEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class HangingPlaceListener implements Listener {
    private final RegionManager regionManager;

    HangingPlaceListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHangingPlace(final HangingPlaceEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPermission("worldsentinel.admin")) {
            final String playerName = player.getName();
            final Location location = event.getEntity().getLocation();
            final Region region = this.regionManager.getRegionInside(location);
            if (region != null) {
                final RegionFlags flags = region.getFlags();
                if (!flags.getBoolean("placing") && !flags.getCollection("owners").contains(playerName)
                        && !flags.getCollection("members").contains(playerName)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
