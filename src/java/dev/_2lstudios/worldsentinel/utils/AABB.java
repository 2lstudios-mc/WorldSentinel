// 
// Decompiled by Procyon v0.5.36
// 

package dev._2lstudios.worldsentinel.utils;

import org.bukkit.util.Vector;

public class AABB {
    private final Vector a;
    private final Vector b;

    public AABB(final Vector a, final Vector b) {
        this.a = a;
        this.b = b;
    }

    public double getMinX() {
        return Math.min(this.a.getX(), this.b.getX());
    }

    public double getMaxX() {
        return Math.max(this.a.getX(), this.b.getX());
    }

    public double getMinY() {
        return Math.min(this.a.getY(), this.b.getY());
    }

    public double getMaxY() {
        return Math.max(this.a.getY(), this.b.getY());
    }

    public double getMinZ() {
        return Math.min(this.a.getZ(), this.b.getZ());
    }

    public double getMaxZ() {
        return Math.max(this.a.getZ(), this.b.getZ());
    }

    public boolean isNotIntersecting(final AABB other) {
        return this.getMaxX() < other.getMinX() || this.getMinX() > other.getMaxX() || this.getMaxY() < other.getMinY()
                || this.getMinY() > other.getMaxY() || this.getMaxZ() < other.getMinZ()
                || this.getMinZ() > other.getMaxZ();
    }

    public boolean isIntersecting(final AABB other) {
        return !this.isNotIntersecting(other);
    }
}
