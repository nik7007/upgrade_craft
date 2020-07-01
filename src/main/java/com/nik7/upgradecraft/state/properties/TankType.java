package com.nik7.upgradecraft.state.properties;

import net.minecraft.util.IStringSerializable;

public enum TankType implements IStringSerializable {
    SINGLE("single"),
    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    TankType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
