package dev._2lstudios.worldsentinel.region;

import java.util.Collection;
import java.util.Map;

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

    private void loadValue(final String key, final Collection<String> value) {
        this.flags.setCollection(key, value);
    }

    private void loadValue(final String key, final Vector value) {
        this.flags.setVector(key, value);
    }

    private void loadValue(final String key, final String value) {
        if (key.startsWith("position")) {
            String[] positions = ((String) value).split(",");

            if (positions.length > 2) {
                loadValue(key, new Vector(Float.parseFloat(positions[0]), Float.parseFloat(positions[1]), Float.parseFloat(positions[2])));
            }
        } else {
            this.flags.setString(key, value);
        }
    }

    private void loadValue(final String key, final int value) {
        this.flags.setInteger(key, value);
    }

    private void loadValue(final String key, final boolean value) {
        this.flags.setBoolean(key, value);
    }

    void load() {
        final String name = this.flags.getString("name");
        final YamlConfiguration config = this.configurationUtil
                .getConfiguration("%datafolder%/regions/" + name + ".yml");
        for (final String key : config.getKeys(false)) {
            if ("name".equals(key)) {
                continue;
            }
            final Object value = config.get(key);
            if (value instanceof Collection) {
                this.loadValue(key, (Collection<String>) value);
            } else if (value instanceof Vector) {
                this.loadValue(key, (Vector) value);
            } else if (value instanceof String) {
                this.loadValue(key, (String) value);
            } else if (value instanceof Integer) {
                this.loadValue(key, (int) value);
            } else {
                if (!(value instanceof Boolean)) {
                    continue;
                }
                this.loadValue(key, (boolean) value);
            }
        }
    }

    private void setValue(final YamlConfiguration config, final String path, final Boolean value) {
        if (Boolean.TRUE.equals(value)) {
            config.set(path, (Object) value);
        }
    }

    private void setValue(final YamlConfiguration config, final String path, final Collection<?> value) {
        if (value != null && !value.isEmpty()) {
            config.set(path, (Object) value);
        }
    }

    private void setValue(final YamlConfiguration config, final String path, final Object value) {
        if (value != null) {
            config.set(path, value);
        }
    }

    void save() {
        final String name = this.flags.getString("name");
        final YamlConfiguration config = new YamlConfiguration();
        for (final Map.Entry<String, Collection<String>> entry : this.flags.getCollections().entrySet()) {
            this.setValue(config, entry.getKey(), entry.getValue());
        }
        for (final Map.Entry<String, Vector> entry2 : this.flags.getVectors().entrySet()) {
            this.setValue(config, entry2.getKey(), entry2.getValue());
        }
        for (final Map.Entry<String, String> entry3 : this.flags.getStrings().entrySet()) {
            final String key = entry3.getKey();
            if ("name".equals(key)) {
                continue;
            }
            this.setValue(config, key, entry3.getValue());
        }
        for (final Map.Entry<String, Integer> entry4 : this.flags.getIntegers().entrySet()) {
            this.setValue(config, entry4.getKey(), entry4.getValue());
        }
        for (final Map.Entry<String, Boolean> entry5 : this.flags.getBooleans().entrySet()) {
            this.setValue(config, entry5.getKey(), entry5.getValue());
        }
        this.configurationUtil.saveConfigurationSync(config, "%datafolder%/regions/" + name + ".yml");
    }
}
