package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class HangingBreakByEntityListener implements Listener {
    private final RegionManager regionManager;

    HangingBreakByEntityListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event) {
        final Entity remover = event.getRemover();
        if (!remover.hasPermission("worldsentinel.admin")) {
            final String removerName = remover.getName();
            final Location location = event.getEntity().getLocation();
            final Region region = this.regionManager.getRegionInside(location);
            if (region != null) {
                final RegionFlags flags = region.getFlags();
                if (!flags.getBoolean("breaking") && !flags.getCollection("owners").contains(removerName)
                        && !flags.getCollection("members").contains(removerName)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
