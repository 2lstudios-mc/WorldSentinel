// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.event.block.BlockExplodeEvent;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class BlockExplodeListener implements Listener {
    private final RegionManager regionManager;
    private final MainConfiguration mainConfiguration;

    BlockExplodeListener(final RegionManager regionManager, final MainConfiguration mainConfiguration) {
        this.regionManager = regionManager;
        this.mainConfiguration = mainConfiguration;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(final BlockExplodeEvent event) {
        if (!this.mainConfiguration.isExplosionsAllowed()) {
            event.setCancelled(true);
        } else {
            final Location location = event.getBlock().getLocation().add(0.5, -0.5, 0.5);
            final Region region = this.regionManager.getRegionInside(location);
            if (region != null && !region.getFlags().getBoolean("breaking")) {
                event.setCancelled(true);
            }
        }
    }
}
