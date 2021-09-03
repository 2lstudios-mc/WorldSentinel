package dev._2lstudios.worldsentinel.region;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.util.Vector;

public class RegionFlags {
    private final Region region;
    private final Map<String, Object> flags;

    RegionFlags(final Region region) {
        this.flags = new ConcurrentHashMap<String, Object>();
        this.region = region;
    }

    private float parseFloat(final String value) {
        try {
            return Float.parseFloat(value);
        } catch (final NumberFormatException ex) {
            // Ignored
        }

        return 0;
    }

    private int parseInt(final String value) {
        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException ex) {
            // Ignored
        }

        return 0;
    }

    public Collection<String> getFlagNames() {
        return flags.keySet();
    }

    public Collection<Object> getFlags() {
        return flags.values();
    }

    public Object get(final String key) {
        return flags.getOrDefault(key, null);
    }

    public void remove(String args2) {
        flags.remove(args2);
    }

    public void set(final String key, final Object value) {
        if (value.equals("null")) {
            remove(key);
        } else if (value == null || !value.equals(get(key))) {
            flags.put(key, value);
            region.setChanged();

            if (key.equals("world") || key.equals("position1") || key.equals("position2")) {
                region.updateChunks();
            }
        }
    }

    public Vector getVector(final String key) {
        final Object value = get(key);

        if (value instanceof Vector) {
            return (Vector) value;
        }

        if (value instanceof String) {
            String[] positions = ((String) value).split(",");

            if (positions.length > 2) {
                return new Vector(parseFloat(positions[0]), parseFloat(positions[1]), parseFloat(positions[2]));
            }
        }

        return null;
    }

    public Collection<String> getCollection(final String key) {
        final Object value = get(key);

        if (value instanceof Collection) {
            return (Collection<String>) value;
        }

        return ConcurrentHashMap.newKeySet();
    }

    public String getString(final String key) {
        final Object value = get(key);

        if (value instanceof String) {
            return (String) value;
        }

        return String.valueOf(value);
    }

    public int getInteger(final String key) {
        final Object value = get(key);

        if (value instanceof Integer) {
            return (int) value;
        }

        return parseInt(String.valueOf(value));
    }

    public boolean getBoolean(final String key) {
        final Object value = get(key);

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        return String.valueOf(value).equals("true");
    }
}
