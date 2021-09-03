package dev._2lstudios.worldsentinel.region;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import dev._2lstudios.worldsentinel.utils.ConfigurationUtil;
import dev._2lstudios.worldsentinel.utils.RegionUtil;

public class Region {
    private final BukkitScheduler scheduler;
    private final Plugin plugin;
    private final RegionManager regionManager;
    private final RegionFlags flags;
    private final RegionFlagsManager flagsManager;
    private Collection<String> chunks;
    private boolean loading;

    Region(final String name, final ConfigurationUtil configurationUtil, final RegionManager regionManager,
            final Plugin plugin) {
        this.chunks = null;
        this.loading = true;
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        this.regionManager = regionManager;
        this.flags = new RegionFlags(this);
        this.flagsManager = new RegionFlagsManager(configurationUtil, this.flags);
        this.flags.setString("name", name);
        this.setLoading(false);
    }

    void loadAsync() {
        if (this.plugin.getServer().isPrimaryThread()) {
            this.scheduler.runTaskAsynchronously(this.plugin, () -> this.loadSync());
        } else {
            this.loadSync();
        }
    }

    void loadSync() {
        this.setLoading(true);
        this.flagsManager.load();
        this.setLoading(false);
    }

    void saveAsync() {
        if (this.plugin.getServer().isPrimaryThread()) {
            this.scheduler.runTaskAsynchronously(this.plugin, () -> this.saveSync());
        } else {
            this.saveSync();
        }
    }

    void saveSync() {
        this.flagsManager.save();
    }

    public void updateChunks() {
        this.regionManager.updateChunks(this);
    }

    public void setChanged() {
        if (!this.isLoading()) {
            this.regionManager.setChanged(this);
        }
    }

    public Collection<String> getChunks() {
        return this.chunks;
    }

    public void setChunks(final Collection<String> chunks) {
        this.chunks = chunks;
    }

    public Collection<Region> getRegionsInside() {
        final Collection<Region> regionsInside = new HashSet<Region>();
        if (this.chunks != null && !this.chunks.isEmpty()) {
            final Collection<Region> checkedRegions = new HashSet<Region>();
            final String worldName = this.flags.getString("world");
            final Vector regionPos1 = this.flags.getVector("position1");
            final Vector regionPos2 = this.flags.getVector("position2");
            for (final String chunkId : this.chunks) {
                final Collection<String> regions = this.regionManager.getRegions(worldName, chunkId);
                for (final String region1Name : regions) {
                    final Region region1 = this.regionManager.getRegion(region1Name);
                    final RegionFlags flags1 = region1.getFlags();
                    final Vector region1Pos1 = flags1.getVector("position1");
                    final Vector region1Pos2 = flags1.getVector("position2");
                    if (!checkedRegions.contains(region1)) {
                        if (RegionUtil.isAABBInAABB(regionPos1, regionPos2, region1Pos1, region1Pos2)) {
                            regionsInside.add(region1);
                        }
                        checkedRegions.add(region1);
                    }
                }
            }
        }
        return regionsInside;
    }

    public boolean isVectorInside(final Vector coords, final String worldName) {
        final String world = this.flags.getString("world");
        if (world != null && world.equals(worldName)) {
            final Vector pos1 = this.flags.getVector("position1");
            final Vector pos2 = this.flags.getVector("position2");
            return RegionUtil.isPointInAABB(pos1, pos2, coords);
        }
        return false;
    }

    public boolean isLocationInside(final Location loc) {
        return isVectorInside(loc.toVector(), loc.getWorld().getName());
    }

    public String getName() {
        return this.flags.getString("name");
    }

    public RegionFlags getFlags() {
        return this.flags;
    }

    public boolean isLoading() {
        return this.loading;
    }

    public void setLoading(final boolean loading) {
        this.loading = loading;
    }
}
