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

    private Integer parseInteger(final String string, final Integer def) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            // Ignored
        }

        return def;
    }

    private void loadValue(final String key, final Object value) {
        if (value instanceof String && (key.equals("members") || key.equals("owners"))) {
            flags.set(key, Collections.singleton((String) value));
        } else if (key.startsWith("position") && value instanceof String) {
            String[] positions = ((String) value).split(",");

            if (positions.length > 2) {
                flags.set(key, new Vector(Float.parseFloat(positions[0]), Float.parseFloat(positions[1]),
                        Float.parseFloat(positions[2])));
            }
        } else if (key.startsWith("priority") && !(value instanceof Integer)) {
            flags.set(key, parseInteger(String.valueOf(value), 0));
        } else if (!(value instanceof Integer) && value.equals("true") || value.equals("false")) {
            flags.set(key, Boolean.valueOf(String.valueOf(value)));
        } else {
            flags.set(key, value);
        }
    }

    void load() {
        final String name = this.flags.getString("name");
        final YamlConfiguration config = this.configurationUtil
                .getConfiguration("%datafolder%/regions/" + name + ".yml");

        for (final String key : config.getKeys(false)) {
            if (!key.equals("name")) {
                final Object value = config.get(key);

                loadValue(key, value);
            }
        }
    }

    private void setValue(final YamlConfiguration config, final String path, final Object value) {
        if (value != null) {
            if (value instanceof Collection) {
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
