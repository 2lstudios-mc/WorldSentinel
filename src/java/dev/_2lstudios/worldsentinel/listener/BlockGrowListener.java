// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.event.block.BlockGrowEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class BlockGrowListener implements Listener {
    private final RegionManager regionManager;

    BlockGrowListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockGrow(final BlockGrowEvent event) {
        final Location location = event.getBlock().getLocation().add(0.5, -0.5, 0.5);
        final Region region = this.regionManager.getRegionInside(location);
        if (region != null) {
            final RegionFlags flags = region.getFlags();
            if (!flags.getBoolean("growing")) {
                event.setCancelled(true);
            }
        }
    }
}
