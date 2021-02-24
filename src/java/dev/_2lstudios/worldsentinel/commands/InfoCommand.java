// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.commands;

import org.bukkit.Location;
import dev._2lstudios.worldsentinel.region.RegionManager;
import java.util.Iterator;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import org.bukkit.ChatColor;
import org.bukkit.util.Vector;
import java.util.Collection;
import java.util.Map;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.entity.Player;

class InfoCommand {
    private static final String LINE_SEPARATOR = "&8, ";

    private void append(final StringBuilder information, final String key, final Object value) {
        if (value instanceof Boolean) {
            information.append("&9" + key + ": " + ((boolean) value ? "&a" : "&c") + value + "&8, ");
        } else if (value == null) {
            information.append("&9" + key + ": " + "&7" + value + "&8, ");
        } else {
            information.append("&9" + key + ": " + "&a" + value + "&8, ");
        }
    }

    public void showInfo(final Player player, final Region region) {
        if (region != null) {
            final RegionFlags flags = region.getFlags();
            final StringBuilder information = new StringBuilder(
                    "&aInformation of region &b" + region.getName() + "&a:&r\n");
            for (final Map.Entry<String, ?> entry : flags.getCollections().entrySet()) {
                this.append(information, entry.getKey(), entry.getValue());
            }
            for (final Map.Entry<String, ?> entry : flags.getVectors().entrySet()) {
                this.append(information, entry.getKey(), entry.getValue());
            }
            for (final Map.Entry<String, ?> entry : flags.getStrings().entrySet()) {
                this.append(information, entry.getKey(), entry.getValue());
            }
            for (final Map.Entry<String, ?> entry : flags.getIntegers().entrySet()) {
                this.append(information, entry.getKey(), entry.getValue());
            }
            for (final Map.Entry<String, ?> entry : flags.getBooleans().entrySet()) {
                this.append(information, entry.getKey(), entry.getValue());
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', information.toString()));
        } else {
            player.sendMessage(ChatColor.RED + "The region requested wasn't found!");
        }
    }

    InfoCommand(final RegionManager regionManager, final String[] args, final Player player) {
        if (args.length <= 1) {
            final Location location = player.getLocation();
            final Region region = regionManager.getRegionInside(location);
            this.showInfo(player, region);
        } else {
            final Region region2 = regionManager.getRegion(args[1]);
            this.showInfo(player, region2);
        }
    }
}
