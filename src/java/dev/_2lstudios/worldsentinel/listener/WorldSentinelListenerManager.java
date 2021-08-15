package dev._2lstudios.worldsentinel.listener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.HandlerList;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import dev._2lstudios.worldsentinel.region.RegionPlayerManager;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.plugin.PluginManager;
import dev._2lstudios.worldsentinel.WorldSentinel;

public class WorldSentinelListenerManager {
    private WorldSentinelListenerManager() {
    }

    public static void register(final WorldSentinel plugin, final PluginManager pluginManager,
            final RegionManager regionManager, final RegionPlayerManager regionPlayerManager,
            final MainConfiguration mainConfiguration) {
        HandlerList.unregisterAll((Plugin) plugin);
        pluginManager.registerEvents((Listener) new BlockBreakListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new BlockBurnListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new BlockExplodeListener(regionManager, mainConfiguration),
                (Plugin) plugin);
        pluginManager.registerEvents((Listener) new BlockFromToListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new BlockGrowListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new BlockIgniteListener(regionManager, mainConfiguration),
                (Plugin) plugin);
        pluginManager.registerEvents((Listener) new BlockPlaceListener(regionManager, mainConfiguration),
                (Plugin) plugin);
        pluginManager.registerEvents((Listener) new ChunkLoadListener(mainConfiguration), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new CreatureSpawnListener(regionManager, mainConfiguration),
                (Plugin) plugin);
        pluginManager.registerEvents((Listener) new EntityChangeBlockListener(mainConfiguration), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new EntityDamageByEntityListener(regionManager, mainConfiguration),
                (Plugin) plugin);
        pluginManager.registerEvents((Listener) new EntityDamageListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new EntityExplodeListener(regionManager, mainConfiguration),
                (Plugin) plugin);
        pluginManager.registerEvents((Listener) new EntityShootBowListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new EntitySpawnListener(mainConfiguration), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new HangingBreakByEntityListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new HangingPlaceListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new PistonExtendListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new PistonRetractListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new PlayerBucketEmptyListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new PlayerBucketFillListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new PlayerCommandPreProcessListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new PlayerInteractListener(pluginManager, regionManager,
                regionPlayerManager, mainConfiguration), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new PlayerJoinListener(regionPlayerManager), (Plugin) plugin);
        pluginManager.registerEvents(
                (Listener) new PlayerMoveListener((Plugin) plugin, regionManager, regionPlayerManager),
                (Plugin) plugin);
        pluginManager.registerEvents((Listener) new PlayerQuitListener(regionPlayerManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new PlayerTeleportListener(regionManager), (Plugin) plugin);
        pluginManager.registerEvents((Listener) new WeatherChangeListener(mainConfiguration), (Plugin) plugin);
    }
}
