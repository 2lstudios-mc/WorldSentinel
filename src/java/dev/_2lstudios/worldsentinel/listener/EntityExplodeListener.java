// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityExplodeEvent;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class EntityExplodeListener implements Listener {
    private final RegionManager regionManager;
    private final MainConfiguration mainConfiguration;

    EntityExplodeListener(final RegionManager regionManager, final MainConfiguration mainConfiguration) {
        this.regionManager = regionManager;
        this.mainConfiguration = mainConfiguration;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (!this.mainConfiguration.isExplosionsAllowed()) {
            event.setCancelled(true);
        } else {
            final Location location = event.getEntity().getLocation();
            final Region region = this.regionManager.getRegionInside(location);
            if (region == null) {
                return;
            }
            if (!region.getFlags().getBoolean("explosions")) {
                event.setCancelled(true);
            }
        }
    }
}
