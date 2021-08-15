package dev._2lstudios.worldsentinel.tasks;

import dev._2lstudios.worldsentinel.region.Region;
import org.bukkit.Location;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.World;
import dev._2lstudios.worldsentinel.region.RegionManager;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import org.bukkit.Server;

public class WorldSentinelTaskTimer implements Runnable {
    private final Server server;
    private final MainConfiguration mainConfiguration;
    private final RegionManager regionManager;

    public WorldSentinelTaskTimer(final Server server, final MainConfiguration mainConfiguration,
            final RegionManager regionManager) {
        this.server = server;
        this.mainConfiguration = mainConfiguration;
        this.regionManager = regionManager;
    }

    @Override
    public void run() {
        if (!this.mainConfiguration.isTimeAllowed()) {
            for (final World world : this.server.getWorlds()) {
                if (world.getTime() != 6000L) {
                    world.setTime(6000L);
                }
            }
        }
        for (final Player player : this.server.getOnlinePlayers()) {
            if (!player.isDead()) {
                final Location location = player.getLocation();
                final Region region = this.regionManager.getRegionInside(location);
                if (region == null) {
                    continue;
                }
                final boolean regeneration = region.getFlags().getBoolean("regeneration");
                if (!regeneration) {
                    continue;
                }
                final double maxHealth = player.getMaxHealth();
                if (player.getHealth() < maxHealth) {
                    player.setHealth(maxHealth);
                }
                if (player.getFoodLevel() < 20) {
                    player.setFoodLevel(20);
                }
                if (player.getSaturation() >= 4.0f) {
                    continue;
                }
                player.setSaturation(4.0f);
            }
        }
    }
}
