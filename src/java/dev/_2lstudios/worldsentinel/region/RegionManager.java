package dev._2lstudios.worldsentinel.region;

import org.bukkit.World;
import org.bukkit.util.Vector;
import java.util.stream.Collectors;
import org.bukkit.Location;
import java.util.Collections;
import java.util.Iterator;
import org.bukkit.scheduler.BukkitScheduler;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import dev._2lstudios.worldsentinel.utils.ConfigurationUtil;

public class RegionManager {
    private final ConfigurationUtil configurationUtil;
    private final Plugin plugin;
    private final Server server;
    private final Map<String, Region> regionsMap;
    private final Map<String, Map<String, Collection<String>>> worldChunkMap;
    private final Collection<String> changedRegions;
    private final Collection<String> noChunkRegions;

    public RegionManager(final Plugin plugin, final ConfigurationUtil configurationUtil) {
        this.regionsMap = new ConcurrentHashMap<String, Region>();
        this.worldChunkMap = new ConcurrentHashMap<String, Map<String, Collection<String>>>();
        this.changedRegions = ConcurrentHashMap.newKeySet();
        this.noChunkRegions = ConcurrentHashMap.newKeySet();
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.configurationUtil = configurationUtil;
        final BukkitScheduler scheduler = this.server.getScheduler();
        final File[] regionsFiles = new File(plugin.getDataFolder() + "/regions/").listFiles();

        scheduler.runTaskAsynchronously(plugin, () -> {
            int i = 0;

            if (regionsFiles != null && regionsFiles.length > 0) {
                for (i = 0; i < regionsFiles.length; ++i) {
                    final File file = regionsFiles[i];
                    final String name = file.getName().split("[.]")[0];
                    this.createRegion(name).loadSync();
                }
            }
            return;
        });

        scheduler.runTaskTimerAsynchronously(plugin, () -> {
            if (!this.changedRegions.isEmpty()) {
                Iterator<String> changedRegionsInterator = this.changedRegions.iterator();
                while (changedRegionsInterator.hasNext()) {
                    String regionName = changedRegionsInterator.next();
                    Region region = this.getRegion(regionName);
                    if (region != null) {
                        region.saveSync();
                    }
                    changedRegionsInterator.remove();
                }
            }
        }, 100L, 100L);
    }

    public Region createRegion(final String regionName) {
        if (this.regionsMap.containsKey(regionName)) {
            return null;
        }
        final Region region = new Region(regionName, this.configurationUtil, this, this.plugin);
        this.regionsMap.put(regionName, region);
        return region;
    }

    public Region createRandomRegion(final String prefix) {
        Region region;
        String regionName;
        for (region = null; region == null; region = this.createRegion(regionName)) {
            regionName = String.valueOf(prefix) + (int) (Math.random() * 1000000.0);
        }
        return region;
    }

    public Map<String, Collection<String>> getChunkMap(final String worldName) {
        return this.worldChunkMap.getOrDefault(worldName, Collections.emptyMap());
    }

    public Collection<String> getRegions(final String worldName, final String chunkId) {
        return this.getChunkMap(worldName).getOrDefault(chunkId, Collections.emptySet());
    }

    public Collection<String> getNoChunkRegions(final Location location) {
        return this.noChunkRegions.stream().filter(regionName -> this.getRegion(regionName).isLocationInside(location))
                .collect(Collectors.toSet());
    }

    public Region getRegion(final String regionName) {
        return this.regionsMap.getOrDefault(regionName, null);
    }

    public String getChunkId(final int x, final int z) {
        return String.valueOf(x) + "," + z;
    }

    public Region getHighestPriority(final Collection<String> regions) {
        Region insideRegion = null;

        for (final String regionName : regions) {
            final Region region = this.getRegion(regionName);
            final int priority = region.getFlags().getInteger("priority");

            if (insideRegion == null || priority >= insideRegion.getFlags().getInteger("priority")) {
                insideRegion = region;
            }
        }

        return insideRegion;
    }

    public Region getRegionInside(final Location location) {
        final String worldName = location.getWorld().getName();
        final int[] chunk = this.getChunkCoords(location);
        final String chunkId = this.getChunkId(chunk[0], chunk[1]);
        final Collection<String> regions = this.getRegions(worldName, chunkId);
        final Collection<String> filteredRegions = regions.stream()
                .filter(regionName -> this.getRegion(regionName).isLocationInside(location))
                .collect(Collectors.toSet());

        filteredRegions.addAll(
                this.noChunkRegions.stream().filter(regionName -> this.getRegion(regionName).isLocationInside(location))
                        .collect(Collectors.toSet()));

        return getHighestPriority(filteredRegions);
    }

    public Collection<Region> getRegions() {
        return this.regionsMap.values();
    }

    public Collection<String> getNearRegions(final Location location, final int distance) {
        final String worldName = location.getWorld().getName();
        final Collection<String> nearRegions = ConcurrentHashMap.newKeySet();
        final int[] chunkCoords = this.getChunkCoords(location);
        final int chunkDistance = distance / 16;
        final int x = chunkCoords[0];
        final int z = chunkCoords[1];
        for (int x2 = -chunkDistance; x2 <= chunkDistance; ++x2) {
            for (int z2 = -chunkDistance; z2 <= chunkDistance; ++z2) {
                final String chunkId = this.getChunkId(x + x2, z + z2);
                final Collection<String> regions = this.getRegions(worldName, chunkId);
                nearRegions.addAll(regions);
            }
        }
        return nearRegions;
    }

    public void setChanged(final Region region) {
        final String regionName = region.getName();
        if (!this.changedRegions.contains(regionName)) {
            this.changedRegions.add(regionName);
        }
    }

    private void removeChunkRegion(final Collection<String> regionChunks, final Region region) {
        if (region.getName().equals("PW401945")) {
            System.out.println("REMOVED REGION FROM NO CHUNK");
        }
        this.noChunkRegions.remove(region.getName());
        if (regionChunks == null || regionChunks.isEmpty()) {
            return;
        }
        final String worldName = region.getFlags().getString("world");
        final Map<String, Collection<String>> chunkMap = this.getChunkMap(worldName);
        if (chunkMap.isEmpty()) {
            return;
        }
        for (final String chunkId : regionChunks) {
            if (chunkMap.containsKey(chunkId)) {
                final Collection<String> regions = this.getRegions(worldName, chunkId);
                regions.remove(region.getName());
                if (!regions.isEmpty()) {
                    continue;
                }
                chunkMap.remove(chunkId);
            }
        }
    }

    public boolean deleteRegion(final String regionName) {
        final Region region = this.getRegion(regionName);
        if (region == null) {
            return false;
        }
        final Collection<String> regionChunks = region.getChunks();
        this.regionsMap.remove(regionName);
        this.changedRegions.remove(region.getName());
        this.removeChunkRegion(regionChunks, region);
        this.configurationUtil.deleteConfigurationSync("%datafolder%/regions/" + regionName + ".yml");
        return true;
    }

    private int floor(final double num) {
        final int floor = (int) num;
        return (floor == num) ? floor : (floor - (int) (Double.doubleToRawLongBits(num) >>> 63));
    }

    private int[] getChunkCoords(final Location location) {
        final double x = location.getX();
        final double z = location.getZ();
        final int chunkX = this.floor(x) >> 4;
        final int chunkZ = this.floor(z) >> 4;
        return new int[] { chunkX, chunkZ };
    }

    public void updateChunks(final Region region) {
        final Collection<String> regionChunks = region.getChunks();
        final RegionFlags regionFlags = region.getFlags();
        final Vector position1 = regionFlags.getVector("position1");
        final Vector position2 = regionFlags.getVector("position2");
        final String worldName = regionFlags.getString("world");

        removeChunkRegion(regionChunks, region);

        if (position1 != null && position2 != null && worldName != null) {
            final World world = this.server.getWorld(worldName);

            if (world != null) {
                final String regionName = region.getName();
                final Collection<String> chunks = ConcurrentHashMap.newKeySet();
                final Location location1 = position1.toLocation(world);
                final Location location2 = position2.toLocation(world);
                final int[] chunk1 = this.getChunkCoords(location1);
                final int[] chunk2 = this.getChunkCoords(location2);
                final int minChunkX = Math.min(chunk1[0], chunk2[0]);
                final int minChunkZ = Math.min(chunk1[1], chunk2[1]);
                final int maxChunkX = Math.max(chunk1[0], chunk2[0]);
                final int maxChunkZ = Math.max(chunk1[1], chunk2[1]);
                for (int x = minChunkX; x <= maxChunkX; ++x) {
                    for (int z = minChunkZ; z <= maxChunkZ; ++z) {
                        final String chunkId = this.getChunkId(x, z);
                        final Map<String, Collection<String>> chunkMap = this.worldChunkMap.getOrDefault(worldName,
                                new ConcurrentHashMap<String, Collection<String>>());
                        final Collection<String> regions = chunkMap.getOrDefault(chunkId, ConcurrentHashMap.newKeySet());
                        regions.add(regionName);
                        chunkMap.put(chunkId, regions);
                        this.worldChunkMap.put(worldName, chunkMap);
                        chunks.add(chunkId);
                    }
                }

                if (region.getName().equals("PW401945")) {
                    System.out.println("REGION IS CHUNK");
                }

                region.setChunks(chunks);
                return;
            }
        }

        region.setChunks(null);

        if (region.getName().equals("PW401945")) {
            System.out.println("REGION IS NO CHUNK");
        }

        this.noChunkRegions.add(region.getName());
    }
}
