package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockIgniteEvent;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class BlockIgniteListener implements Listener {
    private final RegionManager regionManager;
    private final MainConfiguration mainConfiguration;

    BlockIgniteListener(final RegionManager regionManager, final MainConfiguration mainConfiguration) {
        this.regionManager = regionManager;
        this.mainConfiguration = mainConfiguration;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            final Block block = event.getIgnitingBlock();
            if (block == null) {
                return;
            }
            if (this.mainConfiguration.isFirespreadAllowed()) {
                final Region region = this.regionManager.getRegionInside(block.getLocation().add(0.5, -0.5, 0.5));
                if (region != null && !region.getFlags().getBoolean("fire_spread")) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }
}
