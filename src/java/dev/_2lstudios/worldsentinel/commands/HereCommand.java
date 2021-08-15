package dev._2lstudios.worldsentinel.commands;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev._2lstudios.worldsentinel.region.Region;
import dev._2lstudios.worldsentinel.region.RegionManager;

class HereCommand {
    HereCommand(final RegionManager regionManager, final Player player) {
        final Location location = player.getLocation();
        final String worldName = location.getWorld().getName();
        final Chunk chunk = location.getChunk();
        final Collection<String> regions = new HashSet<String>();
        boolean messageSent = false;
        regions.addAll(regionManager.getRegions(worldName, regionManager.getChunkId(chunk.getX(), chunk.getZ())));
        regions.addAll(regionManager.getNoChunkRegions(location));
        if (regions.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No regions are present in this chunk!");
            return;
        }
        for (final String regionName : regions) {
            final Region region = regionManager.getRegion(regionName);
            if (region.isLocationInside(location)) {
                final String name = region.getFlags().getString("name");
                player.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', "&aYou are inside region &9" + name + "&a!"));
                messageSent = true;
            }
        }
        if (!messageSent) {
            player.sendMessage(ChatColor.RED + "You aren't inside any region present in this chunk!");
        }
    }
}
