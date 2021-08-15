package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.event.block.BlockFromToEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class BlockFromToListener implements Listener {
    private final RegionManager regionManager;

    BlockFromToListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFromTo(final BlockFromToEvent event) {
        final Region region = this.regionManager.getRegionInside(event.getBlock().getLocation().add(0.5, -0.5, 0.5));
        final Region region2 = this.regionManager.getRegionInside(event.getToBlock().getLocation().add(0.5, -0.5, 0.5));
        if (region != region2) {
            event.setCancelled(true);
        }
    }
}
