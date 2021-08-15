package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventHandler;
import java.util.Collection;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerTeleportEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class PlayerTeleportListener implements Listener {
    private final RegionManager regionManager;

    PlayerTeleportListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        final Location location = event.getTo();
        final Region region = this.regionManager.getRegionInside(location);
        if (region == null) {
            return;
        }
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        final RegionFlags flags = region.getFlags();
        final Collection<String> owners = flags.getCollection("owners");
        final Collection<String> members = flags.getCollection("members");
        if (!owners.contains(playerName) && !members.contains(playerName)) {
            final boolean movement = flags.getBoolean("movement");
            final boolean enderpearl = flags.getBoolean("enderpearl");
            if (!movement || (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && !enderpearl)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "No puedes teletransportarte a esa zona!");
            }
        }
    }
}
