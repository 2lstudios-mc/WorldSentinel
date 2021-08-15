package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Creature;
import org.bukkit.event.entity.CreatureSpawnEvent;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class CreatureSpawnListener implements Listener {
    private final RegionManager regionManager;
    private final MainConfiguration mainConfiguration;

    CreatureSpawnListener(final RegionManager regionManager, final MainConfiguration mainConfiguration) {
        this.regionManager = regionManager;
        this.mainConfiguration = mainConfiguration;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        final LivingEntity entity = event.getEntity();
        if (!(entity instanceof Creature)) {
            return;
        }
        if (!this.mainConfiguration.isCreaturesSpawningAllowed()) {
            event.setCancelled(true);
        } else {
            final EntityType entityType = entity.getType();
            if (entityType == EntityType.WOLF && !this.mainConfiguration.isWolfsSpawningAllowed()) {
                event.setCancelled(true);
            } else if (entityType == EntityType.BAT && !this.mainConfiguration.isBatSpawningAllowed()) {
                event.setCancelled(true);
            } else if (entityType == EntityType.ENDERMITE && !this.mainConfiguration.isEndermiteSpawningAllowed()) {
                event.setCancelled(true);
            } else {
                final Location location = entity.getLocation();
                final Region region = this.regionManager.getRegionInside(location);
                if (region == null) {
                    return;
                }
                if (!region.getFlags().getBoolean("creatures")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
