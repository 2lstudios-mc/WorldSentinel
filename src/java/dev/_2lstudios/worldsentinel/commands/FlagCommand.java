// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.commands;

import java.util.Collection;
import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.region.RegionManager;

class FlagCommand {
    private int parseInt(final String integer) {
        try {
            Integer.parseInt(integer);
        } catch (NumberFormatException ex) {
        }
        return 0;
    }

    FlagCommand(final RegionManager regionManager, final String[] args, final Player player) {
        if (args.length <= 4) {
            player.sendMessage(ChatColor.RED + "/rg flag <set/add/remove> <region> <flag> <value>");
        } else {
            final Region region = regionManager.getRegion(args[2]);
            final String args2 = args[3];
            final String args3 = args[4];
            if (region == null) {
                player.sendMessage(ChatColor.RED + "The specified Region doesnt exist.");
            } else {
                final RegionFlags flags = region.getFlags();
                if (args[1].equals("set")) {
                    if (flags.getVectors().containsKey(args2)) {
                        flags.setVector(args2, player.getLocation().toVector().normalize());
                    } else if (flags.getStrings().containsKey(args2)) {
                        flags.setString(args2, args3);
                    } else if (flags.getIntegers().containsKey(args2)) {
                        flags.setInteger(args2, this.parseInt(args3));
                    } else if (flags.getBooleans().containsKey(args2)) {
                        flags.setBoolean(args2, args3.equals("true"));
                    } else {
                        player.sendMessage(ChatColor.RED + "The region doesn't contain the flag '" + args2 + "'!");
                    }
                } else if (args[1].equals("add")) {
                    if (flags.getCollections().containsKey(args2)) {
                        final Collection<String> collection = flags.getCollection(args2);
                        if (!collection.contains(args3)) {
                            collection.add(args3);
                            player.sendMessage(
                                    ChatColor.GREEN + "Added '" + args3 + "' to the collection '" + args2 + "'!");
                        } else {
                            player.sendMessage(
                                    ChatColor.RED + "The collection '" + args2 + "' already contains '" + args3 + "'!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "The region doesn't contain the flag '" + args2 + "'!");
                    }
                } else if (args[1].equals("remove")) {
                    if (flags.getCollections().containsKey(args2)) {
                        final Collection<String> collection = flags.getCollection(args2);
                        if (collection.contains(args3)) {
                            collection.remove(args3);
                            player.sendMessage(
                                    ChatColor.GREEN + "Removed '" + args3 + "' from the collection '" + args2 + "'!");
                        } else {
                            player.sendMessage(
                                    ChatColor.RED + "The collection '" + args2 + "' doesn't contain '" + args3 + "'!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "The region doesn't contain the flag '" + args2 + "'!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "/rg flag <set/add/remove> <flag> <value>");
                }
            }
        }
    }
}
