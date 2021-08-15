package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Collection;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class PlayerBucketEmptyListener implements Listener {
    private final RegionManager regionManager;

    PlayerBucketEmptyListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(final PlayerBucketEmptyEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("worldsentinel.admin")) {
            return;
        }
        final Location location = event.getBlockClicked().getLocation().add(0.5, -0.5, 0.5);
        final Region region = this.regionManager.getRegionInside(location);
        if (region == null) {
            return;
        }
        final RegionFlags flags = region.getFlags();
        final boolean placing = flags.getBoolean("placing");
        final Collection<String> owners = flags.getCollection("owners");
        final Collection<String> members = flags.getCollection("members");
        if (!placing && !owners.contains(player.getName()) && !members.contains(player.getName())) {
            event.setCancelled(true);
        }
    }
}
