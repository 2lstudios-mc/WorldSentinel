// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.configurations;

import org.bukkit.configuration.file.YamlConfiguration;
import dev._2lstudios.worldsentinel.utils.ConfigurationUtil;

public class MainConfiguration {
    private final ConfigurationUtil configurationUtil;
    private YamlConfiguration yamlConfiguration;

    public MainConfiguration(final ConfigurationUtil configurationUtil) {
        this.configurationUtil = configurationUtil;
        this.reload();
    }

    private boolean get(final String key, final boolean def) {
        if (!this.yamlConfiguration.isSet(key)) {
            this.set(key, def);
            return def;
        }
        return this.yamlConfiguration.getBoolean(key);
    }

    private int get(final String key, final int def) {
        if (!this.yamlConfiguration.isSet(key)) {
            this.set(key, def);
            return def;
        }
        return this.yamlConfiguration.getInt(key);
    }

    private void set(final String key, final Object value) {
        this.yamlConfiguration.set(key, value);
        this.configurationUtil.saveConfigurationAsync(this.yamlConfiguration, "%datafolder%/config.yml");
    }

    private void reload() {
        this.yamlConfiguration = this.configurationUtil.getConfiguration("%datafolder%/config.yml");
    }

    public boolean isFirespreadAllowed() {
        return this.get("global_settings.world.firespread", true);
    }

    public boolean isExplosionsAllowed() {
        return this.get("global_settings.world.explosions", true);
    }

    public boolean isWeatherAllowed() {
        return this.get("global_settings.world.weather", true);
    }

    public boolean isTimeAllowed() {
        return this.get("global_settings.world.time", true);
    }

    public boolean isCreaturesSpawningAllowed() {
        return this.get("global_settings.creatures.spawning", true);
    }

    public boolean isEntityChangeBlockAllowed() {
        return this.get("global_settings.creatures.changeblock", false);
    }

    public boolean isCreaturesDamageAllowed() {
        return this.get("global_settings.creatures.damage", true);
    }

    public boolean isWolfsSpawningAllowed() {
        return this.get("global_settings.wolfs.spawning", false);
    }

    public boolean isWolfsRemove() {
        return this.get("global_settings.wolfs.remove", true);
    }

    public boolean isEndermiteSpawningAllowed() {
        return this.get("global_settings.endermite.spawning", false);
    }

    public boolean isPlayersNoStrength() {
        return this.get("global_settings.players.nostrength", false);
    }

    public boolean isBatSpawningAllowed() {
        return this.get("global_settings.bat.spawning", false);
    }

    public int getArmorStandLimit() {
        return this.get("global_settings.armorstand.limit", 8);
    }

    public int getMinecartLimit() {
        return this.get("global_settings.minecart.limit", 4);
    }

    public int getBoatLimit() {
        return this.get("global_settings.boat.limit", 4);
    }

    public int getMobSpawnerLimit() {
        return this.get("global_settings.spawner.limit", 2);
    }

    public int getBeaconLimit() {
        return this.get("global_settings.beacon.limit", 2);
    }

    public int getHopperLimit() {
        return this.get("global_settings.hopper.limit", 64);
    }

    public int getVillagerLimit() {
        return this.get("global_settings.villager.limit", 4);
    }

    public int getWitherLimit() {
        return this.get("global_settings.wither.limit", 1);
    }

    public int getDroppedItemLimit() {
        return this.get("global_settings.dropped_item.limit", 300);
    }

    public int getTileEntityLimit() {
        return this.get("global_settings.tileentity.limit", 128);
    }
}
