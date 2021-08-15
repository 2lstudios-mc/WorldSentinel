package dev._2lstudios.worldsentinel.region;

import java.util.HashSet;
import java.util.HashMap;
import org.bukkit.util.Vector;
import java.util.Collection;
import java.util.Map;

public class RegionFlags {
    private final Region region;
    private final Map<String, Collection<String>> collections;
    private final Map<String, Vector> vectors;
    private final Map<String, String> strings;
    private final Map<String, Integer> integers;
    private final Map<String, Boolean> booleans;

    RegionFlags(final Region region) {
        this.collections = new HashMap<String, Collection<String>>();
        this.vectors = new HashMap<String, Vector>();
        this.strings = new HashMap<String, String>();
        this.integers = new HashMap<String, Integer>();
        this.booleans = new HashMap<String, Boolean>();
        this.region = region;
    }

    public final Map<String, Collection<String>> getCollections() {
        return this.collections;
    }

    public final Map<String, Vector> getVectors() {
        return this.vectors;
    }

    public final Map<String, String> getStrings() {
        return this.strings;
    }

    public final Map<String, Integer> getIntegers() {
        return this.integers;
    }

    public final Map<String, Boolean> getBooleans() {
        return this.booleans;
    }

    public Collection<String> getCollection(final String key) {
        this.collections.computeIfAbsent(key, k -> this.collections.put(k, new HashSet<String>()));
        return this.collections.get(key);
    }

    public void setCollection(final String key, final Collection<String> value) {
        this.region.setChanged();
        this.collections.put(key, value);
    }

    public Vector getVector(final String key) {
        return this.vectors.getOrDefault(key, null);
    }

    public void setVector(final String key, final Vector value) {
        if (value == null || !value.equals((Object) this.getVector(key))) {
            this.region.setChanged();
            this.vectors.put(key, value);
            if (key.equals("position1") || key.equals("position2")) {
                this.region.updateChunks();
            }
        }
    }

    public String getString(final String key) {
        return this.strings.getOrDefault(key, null);
    }

    public void setString(final String key, final String value) {
        if (value == null || !value.equals(this.getString(key))) {
            this.region.setChanged();
            this.strings.put(key, value);
            if (key.equals("world")) {
                this.region.updateChunks();
            }
        }
    }

    public int getInteger(final String key) {
        return this.integers.getOrDefault(key, 0);
    }

    public void setInteger(final String key, final int value) {
        if (value != this.getInteger(key)) {
            this.region.setChanged();
            this.integers.put(key, value);
        }
    }

    public boolean getBoolean(final String key) {
        return this.booleans.getOrDefault(key, false);
    }

    public void setBoolean(final String key, final boolean value) {
        if (value != this.getBoolean(key)) {
            this.region.setChanged();
            this.booleans.put(key, value);
        }
    }
}
