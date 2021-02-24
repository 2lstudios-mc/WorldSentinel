// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Collection;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class PlayerCommandPreProcessListener implements Listener {
    private final RegionManager regionManager;

    PlayerCommandPreProcessListener(final RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerCommandPreProcess(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final Location location = player.getLocation();
        final Region region = this.regionManager.getRegionInside(location);
        if (region == null) {
            return;
        }
        final String message = event.getMessage().toLowerCase();
        final Collection<String> blockedCommands = region.getFlags().getCollection("blocked_commands");
        if (blockedCommands != null && blockedCommands
                .contains(message.replace("/", "").replaceAll("[\\w]+:", "").toLowerCase().split(" ")[0])) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "No puedes ejecutar ese comando en esta zona!");
        }
    }
}
