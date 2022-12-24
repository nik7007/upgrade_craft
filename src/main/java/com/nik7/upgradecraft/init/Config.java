package com.nik7.upgradecraft.init;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {

    public static final ForgeConfigSpec SERVER_CONFIG;

    public static final String CATEGORY_SERVER_GENERAL = "server_general";
    public static final String SUBCATEGORY_FLUID_TANK = "fluid_tank";

    public static ForgeConfigSpec.IntValue TANK_CAPACITY;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        SERVER_BUILDER.comment("General server setting").push(CATEGORY_SERVER_GENERAL);
        // Server Config
        createFluidTankConfig(SERVER_BUILDER);
        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    private static void createFluidTankConfig(ForgeConfigSpec.Builder configBuilder) {
        configBuilder.comment("Fluid tank setting").push(SUBCATEGORY_FLUID_TANK);
        TANK_CAPACITY = configBuilder
                .defineInRange("Base tank capacity", 27, 0, Integer.MAX_VALUE);
        configBuilder.pop();
    }

    private Config() {
    }
}
