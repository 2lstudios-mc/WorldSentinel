package dev._2lstudios.worldsentinel.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev._2lstudios.worldsentinel.region.Region;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.RegionManager;

class InfoCommand {
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

            for (final String key : flags.getFlagNames()) {
                final Object value = flags.get(key);

                append(information, key, value);
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
