// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class EntityDamageListener implements Listener {
    private final RegionManager regionManager;

    EntityDamageListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityDamage(final EntityDamageEvent event) {
        final EntityDamageEvent.DamageCause damageCause = event.getCause();
        final Entity entity = event.getEntity();
        final Location location = entity.getLocation();
        final Region region = this.regionManager.getRegionInside(location);
        if (region == null) {
            return;
        }
        final RegionFlags flags = region.getFlags();
        final boolean fallDamage = flags.getBoolean("fall_damage");
        final boolean playerDamage = flags.getBoolean("player_damage");
        final boolean entityDamage = flags.getBoolean("entity_damage");
        if (!fallDamage && damageCause == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        } else if (entity instanceof Player) {
            if (!playerDamage) {
                event.setCancelled(true);
            }
        } else if (!entityDamage) {
            event.setCancelled(true);
        }
    }
}
