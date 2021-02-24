// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventHandler;
import java.util.Collection;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class EntityShootBowListener implements Listener {
    private final RegionManager regionManager;

    EntityShootBowListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityShootBow(final EntityShootBowEvent event) {
        final LivingEntity livingEntity = event.getEntity();
        final Location location = livingEntity.getLocation();
        final Region region = this.regionManager.getRegionInside(location);
        if (region == null) {
            return;
        }
        final RegionFlags flags = region.getFlags();
        final boolean bow = flags.getBoolean("bow");
        final Collection<String> owners = flags.getCollection("owners");
        final Collection<String> members = flags.getCollection("members");
        if (!bow && !owners.contains(livingEntity.getName()) && !members.contains(livingEntity.getName())) {
            event.setCancelled(true);
        }
    }
}
