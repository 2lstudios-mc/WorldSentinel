package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Creature;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import org.bukkit.event.Listener;

class EntityChangeBlockListener implements Listener {
    private final MainConfiguration mainConfiguration;

    EntityChangeBlockListener(final MainConfiguration mainConfiguration) {
        this.mainConfiguration = mainConfiguration;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Creature && !this.mainConfiguration.isEntityChangeBlockAllowed()) {
            event.setCancelled(true);
        }
    }
}
