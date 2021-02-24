// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Collection;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketFillEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class PlayerBucketFillListener implements Listener {
    private final RegionManager regionManager;

    PlayerBucketFillListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(final PlayerBucketFillEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("worldsentinel.admin")) {
            return;
        }
        final Location location = event.getBlockClicked().getLocation();
        final Region region = this.regionManager.getRegionInside(location);
        if (region == null) {
            return;
        }
        final RegionFlags flags = region.getFlags();
        final Collection<String> owners = flags.getCollection("owners");
        final Collection<String> members = flags.getCollection("members");
        final boolean breaking = flags.getBoolean("breaking");
        if (!breaking && !owners.contains(player.getName()) && !members.contains(player.getName())) {
            event.setCancelled(true);
        }
    }
}
