package dev._2lstudios.worldsentinel.region;

import java.util.Collection;
import java.util.Collections;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import dev._2lstudios.worldsentinel.utils.ConfigurationUtil;

class RegionFlagsManager {
    private final ConfigurationUtil configurationUtil;
    private final RegionFlags flags;

    RegionFlagsManager(final ConfigurationUtil configurationUtil, final RegionFlags flags) {
        this.configurationUtil = configurationUtil;
        this.flags = flags;
    }

    void load() {
        final String name = this.flags.getString("name");
        final YamlConfiguration config = this.configurationUtil
                .getConfiguration("%datafolder%/regions/" + name + ".yml");

        for (final String key : config.getKeys(false)) {
            if (!key.equals("name")) {
                final Object value = config.get(key);

                flags.set(key, value);
            }
        }
    }

    private void setValue(final YamlConfiguration config, final String path, final Object value) {
        if (value != null) {
            if (value instanceof Collection<?>) {
                final Collection<?> collection = (Collection<?>) value;

                if (!collection.isEmpty()) {
                    config.set(path, collection);
                }
            } else if (value instanceof Integer) {
                config.set(path, (int) value);
            } else if (value instanceof Vector) {
                config.set(path, (Vector) value);
            } else {
                config.set(path, value);
            }
        }
    }

    void save() {
        final String name = flags.getString("name");
        final YamlConfiguration config = new YamlConfiguration();

        for (final String key : flags.getFlagNames()) {
            if (!key.equals("name")) {
                final Object value = flags.get(key);

                this.setValue(config, key, value);
            }
        }

        this.configurationUtil.saveConfigurationSync(config, "%datafolder%/regions/" + name + ".yml");
    }
}
