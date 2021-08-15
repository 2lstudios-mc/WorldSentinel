package dev._2lstudios.worldsentinel.utils;

import org.bukkit.util.Vector;

public class RegionUtil {
    private static boolean isPointBetween(final double x, final double y, final double point) {
        return point >= Math.min(x, y) && point <= Math.max(x, y);
    }

    public static boolean isPointInAABB(final Vector pos1, final Vector pos2, final Vector point) {
        return pos1 == null || pos2 == null
                || (isPointBetween(pos1.getX(), pos2.getX(), point.getX())
                        && isPointBetween(pos1.getY(), pos2.getY(), point.getY())
                        && isPointBetween(pos1.getZ(), pos2.getZ(), point.getZ()));
    }

    public static boolean isAABBInAABB(final Vector aabbPos1, final Vector aabbPos2, final Vector aabb1Pos1,
            final Vector aabb1Pos2) {
        return new AABB(aabbPos1, aabbPos2).isIntersecting(new AABB(aabb1Pos1, aabb1Pos2));
    }
}
