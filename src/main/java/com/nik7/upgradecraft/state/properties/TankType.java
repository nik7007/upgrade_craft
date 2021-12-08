package com.nik7.upgradecraft.state.properties;


import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TankType implements StringRepresentable {
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
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public abstract TankType getOpposite();

}
