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
        if (args.length > 2) {
            final Region region = regionManager.getRegion(args[1]);
            final String flag = args[2];

            if (region == null) {
                player.sendMessage(ChatColor.RED + "The specified Region doesnt exist.");
            } else {
                final RegionFlags flags = region.getFlags();

                if (args.length > 3) {
                    final String value = args[3];

                    if (args.length > 4) {
                        Collection<String> collection = flags.getCollection(flag);

                        if (args[4].equals("-a")) {
                            if (collection == null) {
                                collection = ConcurrentHashMap.newKeySet();
                            }

                            if (!collection.contains(value)) {
                                collection.add(value);
                                flags.set(flag, collection);
                                player.sendMessage(
                                        ChatColor.GREEN + "Added '" + value + "' to the collection '" + flag + "'!");
                            } else {
                                player.sendMessage(ChatColor.RED + "The collection '" + flag + "' already contains '"
                                        + value + "'!");
                            }
                        } else if (args[4].equals("-r")) {
                            if (flags.getFlagNames().contains(flag)) {
                                if (collection.contains(value)) {
                                    collection.remove(value);

                                    if (collection.isEmpty()) {
                                        flags.remove(flag);
                                    }

                                    player.sendMessage(ChatColor.GREEN + "Removed '" + value + "' from the collection '"
                                            + flag + "'!");
                                } else {
                                    player.sendMessage(ChatColor.RED + "The collection '" + flag + "' doesn't contain '"
                                            + value + "'!");
                                }
                            } else {
                                player.sendMessage(
                                        ChatColor.RED + "The region doesn't contain the flag '" + flag + "'!");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Invalid argument '" + args[4] + "'!");
                        }
                    } else {
                        final Integer number = parseInteger(value, null);
                        final Boolean bool = parseBoolean(value, null);

                        if (flag.startsWith("position")) {
                            flags.set(flag, player.getLocation().toVector());
                        } else if (number != null) {
                            flags.set(flag, (int) number);
                        } else if (bool != null) {
                            flags.set(flag, (boolean) bool);
                        } else {
                            flags.set(flag, value);
                        }

                        player.sendMessage(
                                ChatColor.GREEN + "Setted '" + flag + "' flag to '" + value + "'!");
                    }
                } else {
                    if (flags.getFlagNames().contains(flag)) {
                        flags.remove(flag);
                        player.sendMessage(
                                ChatColor.GREEN + "Removed '" + flag + "' flag from '" + region.getName() + "'!");
                    } else {
                        player.sendMessage(ChatColor.RED + "The region doesn't contain the flag '" + flag + "'!");
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "/rg flag <flag> [value] [-a-r]");
        }
    }
}
