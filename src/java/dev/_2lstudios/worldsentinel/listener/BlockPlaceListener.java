package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.block.BlockState;
import org.bukkit.Chunk;
import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.event.Listener;

class BlockPlaceListener implements Listener {
    private final RegionManager regionManager;
    private final MainConfiguration mainConfiguration;
    private final Material mobSpawnerMaterial;
    private final String cancelMessage;

    BlockPlaceListener(final RegionManager regionManager, final MainConfiguration mainConfiguration) {
        this.regionManager = regionManager;
        this.mainConfiguration = mainConfiguration;
        this.cancelMessage = ChatColor.translateAlternateColorCodes('&',
                "&cNo puedes colocar mas %type% en este chunk! (%limit%)");
        this.mobSpawnerMaterial = Material.getMaterial("MOB_SPAWNER");
    }

    private void cancel(final Cancellable event, final Player player, final String type, final int count,
            final int limit) {
        player.sendMessage(
                this.cancelMessage.replace("%type%", type).replace("%limit%", String.valueOf(count) + "/" + limit));
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPermission("worldsentinel.admin")) {
            final String playerName = player.getName();
            final Block block = event.getBlock();
            final Location location = block.getLocation().add(0.5, -0.5, 0.5);
            final Region region = this.regionManager.getRegionInside(location);
            if (region != null && !player.hasPermission("worldsentinel.admin")
                    && !region.getFlags().getBoolean("placing")
                    && !region.getFlags().getCollection("owners").contains(playerName)
                    && !region.getFlags().getCollection("members").contains(playerName)) {
                event.setCancelled(true);
            } else {
                final Material type = block.getType();
                if (type == Material.HOPPER || type == Material.FURNACE || type == Material.BEACON
                        || type == this.mobSpawnerMaterial || type == Material.CHEST || type == Material.ANVIL
                        || type == Material.BREWING_STAND) {
                    final Chunk chunk = location.getChunk();
                    final BlockState[] tileEntities = chunk.getTileEntities();
                    final int tileEntityLimit = this.mainConfiguration.getTileEntityLimit();
                    final int spawnerLimit = this.mainConfiguration.getMobSpawnerLimit();
                    final int beaconLimit = this.mainConfiguration.getBeaconLimit();
                    final int hopperLimit = this.mainConfiguration.getHopperLimit();
                    final int tileEntityCount = tileEntities.length;
                    int spawnersCount = 0;
                    int beaconCount = 0;
                    int hopperCount = 0;
                    if (tileEntityCount >= tileEntityLimit) {
                        this.cancel((Cancellable) event, player, "Entidades de Tile", tileEntityCount, tileEntityLimit);
                        return;
                    }
                    BlockState[] array;
                    for (int length = (array = tileEntities).length, i = 0; i < length; ++i) {
                        final BlockState blockState = array[i];
                        final Material blockStateType = blockState.getType();
                        if (blockStateType == this.mobSpawnerMaterial) {
                            ++spawnersCount;
                        } else if (blockStateType == Material.BEACON) {
                            ++beaconCount;
                        } else if (blockStateType == Material.HOPPER) {
                            ++hopperCount;
                        }
                    }
                    if (type == this.mobSpawnerMaterial && spawnersCount >= spawnerLimit) {
                        this.cancel((Cancellable) event, player, "Spawners", spawnersCount, spawnerLimit);
                    } else if (type == Material.BEACON && beaconCount >= beaconLimit) {
                        this.cancel((Cancellable) event, player, "Faros", beaconCount, beaconLimit);
                    } else if (type == Material.HOPPER && hopperCount >= hopperLimit) {
                        this.cancel((Cancellable) event, player, "Tolvas", hopperCount, hopperLimit);
                    }
                    player.sendMessage("Tiles: " + tileEntityCount);
                }
            }
        }
    }
}
