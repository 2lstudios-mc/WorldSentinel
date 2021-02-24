// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.commands;

import dev._2lstudios.worldsentinel.region.RegionFlags;
import java.util.Iterator;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.World;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.Server;

class ListCommand {
    ListCommand(final Server server, final RegionManager regionManager, final Player player) {
        final String lineSeparator = ChatColor.DARK_GRAY + ", ";
        final StringBuilder regionsMessage = new StringBuilder();
        for (final World world : server.getWorlds()) {
            regionsMessage.append(ChatColor.GREEN + world.getName() + " regions:\n");
            for (final Region region : regionManager.getRegions()) {
                final RegionFlags flags = region.getFlags();
                final String world2 = flags.getString("world");
                if (world2.equals(world.getName())) {
                    final String regionName = flags.getString("name");
                    regionsMessage.append(ChatColor.BLUE + regionName + lineSeparator);
                }
            }
        }
        regionsMessage.append("\n" + ChatColor.GREEN + "Null regions:\n");
        for (final Region region2 : regionManager.getRegions()) {
            final RegionFlags flags2 = region2.getFlags();
            final String world3 = flags2.getString("world");
            if (world3 == null) {
                final String regionName2 = flags2.getString("name");
                regionsMessage.append(ChatColor.BLUE + regionName2 + lineSeparator);
            }
        }
        player.sendMessage(regionsMessage.toString());
    }
}
