package com.nik7.upgradecraft.state.properties;

import net.minecraft.util.IStringSerializable;

public enum TankType implements IStringSerializable {
    SINGLE("single") {
        @Override
        public TankType getOpposite() {
            return SINGLE;
        }
    },
    TOP("top") {
        @Override
        public TankType getOpposite() {
            return BOTTOM;
        }
    },
    BOTTOM("bottom") {
        @Override
        public TankType getOpposite() {
            return TOP;
        }
    };

    private final String name;

    TankType(String name) {
        this.name = name;
    }

    @Override
    public String func_176610_l() {
        return this.name;
    }

    public abstract TankType getOpposite();

}
