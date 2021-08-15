package dev._2lstudios.worldsentinel.listener;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import dev._2lstudios.worldsentinel.region.Region;
import dev._2lstudios.worldsentinel.region.RegionManager;

class PistonExtendListener implements Listener {
    private final RegionManager regionManager;

    PistonExtendListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPistonExtend(final BlockPistonExtendEvent event) {
        final List<Block> pushedBlocks = (List<Block>) event.getBlocks();
        if (!pushedBlocks.isEmpty()) {
            final Block piston = event.getBlock();
            final Region region = this.regionManager.getRegionInside(piston.getLocation());
            for (final Block block : pushedBlocks) {
                final Region region2 = this.regionManager.getRegionInside(block.getLocation());
                if (region != region2) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
