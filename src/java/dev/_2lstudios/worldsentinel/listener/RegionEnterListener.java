// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Collection;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.events.RegionEnterEvent;
import org.bukkit.event.Listener;

public class RegionEnterListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onRegionEnter(final RegionEnterEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("worldsentinel.bypass")) {
            return;
        }
        final String playerName = player.getName();
        final Region newRegion = event.getNewRegion();
        final RegionFlags flags = newRegion.getFlags();
        final Collection<String> owners = flags.getCollection("owners");
        final Collection<String> members = flags.getCollection("members");
        final boolean movement = flags.getBoolean("movement");
        if (!movement && !owners.contains(playerName) && !members.contains(playerName)) {
            event.setCancelled(true);
        }
    }
}
