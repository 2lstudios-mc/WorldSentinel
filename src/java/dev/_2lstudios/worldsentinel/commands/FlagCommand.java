package dev._2lstudios.worldsentinel.commands;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import dev._2lstudios.worldsentinel.region.RegionFlags;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev._2lstudios.worldsentinel.region.RegionManager;

class FlagCommand {
    private Integer parseInteger(final String string, final Integer def) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            // Ignored
        }

        return def;
    }

    private Boolean parseBoolean(final String string, final Boolean def) {
        try {
            return Boolean.parseBoolean(string);
        } catch (NumberFormatException ex) {
            // Ignored
        }

        return def;
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
                    final Integer number = parseInteger(args2, null);
                    final Boolean bool = parseBoolean(args2, null);

                    if (args2.startsWith("position")) {

                    } else if (number != null) {
                        flags.set(args2, (int) number);
                    } else if (bool != null) {
                        flags.set(args2, (boolean) bool);
                    } else {
                        flags.set(args2, args3);
                    }
                } else if (args[1].equals("add")) {
                    Collection<String> collection = flags.getCollection(args2);

                    if (collection == null) {
                        collection = ConcurrentHashMap.newKeySet();
                    }

                    if (!collection.contains(args3)) {
                        collection.add(args3);
                        flags.set(args2, collection);
                        player.sendMessage(
                                ChatColor.GREEN + "Added '" + args3 + "' to the collection '" + args2 + "'!");
                    } else {
                        player.sendMessage(
                                ChatColor.RED + "The collection '" + args2 + "' already contains '" + args3 + "'!");
                    }
                } else if (args[1].equals("remove")) {
                    if (flags.getFlagNames().contains(args2)) {
                        final Collection<String> collection = flags.getCollection(args2);
                        if (collection.contains(args3)) {
                            collection.remove(args3);

                            if (collection.isEmpty()) {
                                flags.remove(args2);
                            }

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
