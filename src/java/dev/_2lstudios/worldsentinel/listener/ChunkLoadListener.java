// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.world.ChunkLoadEvent;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import org.bukkit.event.Listener;

class ChunkLoadListener implements Listener {
    private final MainConfiguration mainConfiguration;

    ChunkLoadListener(final MainConfiguration mainConfiguration) {
        this.mainConfiguration = mainConfiguration;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (this.mainConfiguration.isWolfsRemove()) {
            Entity[] entities;
            for (int length = (entities = event.getChunk().getEntities()).length, i = 0; i < length; ++i) {
                final Entity entity = entities[i];
                if (entity instanceof Wolf) {
                    entity.remove();
                }
            }
        }
    }
}
