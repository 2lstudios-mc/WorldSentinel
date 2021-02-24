// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel;

import org.bukkit.plugin.PluginManager;
import org.bukkit.Server;
import dev._2lstudios.worldsentinel.listener.WorldSentinelListenerManager;
import org.bukkit.command.CommandExecutor;
import dev._2lstudios.worldsentinel.commands.RegionCommand;
import dev._2lstudios.worldsentinel.tasks.WorldSentinelTaskTimer;
import dev._2lstudios.worldsentinel.configurations.MainConfiguration;
import org.bukkit.plugin.Plugin;
import dev._2lstudios.worldsentinel.utils.ConfigurationUtil;
import dev._2lstudios.worldsentinel.region.RegionPlayerManager;
import dev._2lstudios.worldsentinel.region.RegionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldSentinel extends JavaPlugin {
    private static WorldSentinel instance;
    private RegionManager regionManager;
    private RegionPlayerManager regionPlayerManager;

    private static void setInstance(final WorldSentinel instance) {
        WorldSentinel.instance = instance;
    }

    public static WorldSentinel getInstance() {
        return WorldSentinel.instance;
    }

    public RegionManager getRegionManager() {
        return this.regionManager;
    }

    public RegionPlayerManager getRegionPlayerManager() {
        return this.regionPlayerManager;
    }

    public void onEnable() {
        final Server server = this.getServer();
        final PluginManager pluginManager = server.getPluginManager();
        final ConfigurationUtil configurationUtil = new ConfigurationUtil((Plugin) this);
        configurationUtil.createConfiguration("%datafolder%/config.yml");
        final MainConfiguration mainConfiguration = new MainConfiguration(configurationUtil);
        this.regionManager = new RegionManager((Plugin) this, configurationUtil);
        this.regionPlayerManager = new RegionPlayerManager();
        setInstance(this);
        server.getScheduler().runTaskTimerAsynchronously((Plugin) this,
                (Runnable) new WorldSentinelTaskTimer(server, mainConfiguration, this.regionManager), 20L, 20L);
        final RegionCommand regionCommand = new RegionCommand(server, this.regionManager);
        this.getCommand("rg").setExecutor((CommandExecutor) regionCommand);
        this.getCommand("region").setExecutor((CommandExecutor) regionCommand);
        WorldSentinelListenerManager.register(this, pluginManager, this.regionManager, this.regionPlayerManager,
                mainConfiguration);
    }
}
