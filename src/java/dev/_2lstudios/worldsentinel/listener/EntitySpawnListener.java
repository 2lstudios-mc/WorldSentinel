package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import dev._2lstudios.worldsentinel.utils.Limit;
import java.util.EnumMap;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntitySpawnEvent;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import org.bukkit.event.Listener;

class EntitySpawnListener implements Listener {
    private final MainConfiguration mainConfiguration;

    EntitySpawnListener(final MainConfiguration mainConfiguration) {
        this.mainConfiguration = mainConfiguration;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntitySpawn(final EntitySpawnEvent event) {
        final Entity entity = event.getEntity();
        final EntityType entityType = entity.getType();
        if (entityType != EntityType.ARMOR_STAND && entityType != EntityType.DROPPED_ITEM
                && entityType != EntityType.VILLAGER && entityType != EntityType.WITHER) {
            return;
        }
        final Chunk chunk = event.getLocation().getChunk();
        final Map<EntityType, Limit> limits = new EnumMap<EntityType, Limit>(EntityType.class);
        limits.put(EntityType.ARMOR_STAND, new Limit(this.mainConfiguration.getArmorStandLimit()));
        limits.put(EntityType.DROPPED_ITEM, new Limit(this.mainConfiguration.getDroppedItemLimit()));
        limits.put(EntityType.VILLAGER, new Limit(this.mainConfiguration.getVillagerLimit()));
        limits.put(EntityType.WITHER, new Limit(this.mainConfiguration.getWitherLimit()));
        Entity[] entities;
        for (int length = (entities = chunk.getEntities()).length, i = 0; i < length; ++i) {
            final Entity entity2 = entities[i];
            final EntityType entityType2 = entity2.getType();
            if (entityType == entityType2 && limits.containsKey(entityType2)) {
                final Limit limit = limits.get(entityType2);
                limit.add(1);
                if (limit.reached()) {
                    if (entityType == EntityType.DROPPED_ITEM) {
                        entity2.remove();
                    } else {
                        event.setCancelled(true);
                    }
                    return;
                }
            }
        }
    }
}
