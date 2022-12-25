package com.nik7.upgradecraft.client.render;

import com.google.common.base.MoreObjects;

public class Cuboid {
    private final double minX;
    private final double minY;
    private final double minZ;
    private final double x;
    private final double z;
    private double y;

    public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.x = maxX - minX;
        this.y = maxY - minY;
        this.z = maxZ - minZ;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void scaleY(double scale) {
        this.y *= scale;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("minX", minX)
                .add("minY", minY)
                .add("minZ", minZ)
                .add("x", x)
                .add("z", z)
                .add("y", y)
                .toString();
    }
}
