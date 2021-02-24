// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBurnEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class BlockBurnListener implements Listener {
    private final RegionManager regionManager;

    BlockBurnListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(final BlockBurnEvent event) {
        final Block block = event.getBlock();
        final Region region = this.regionManager.getRegionInside(block.getLocation().add(0.5, -0.5, 0.5));
        if (region != null && !region.getFlags().getBoolean("breaking")) {
            event.setCancelled(true);
        }
    }
}
