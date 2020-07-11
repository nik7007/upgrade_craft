package com.nik7.upgradecraft.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class Config {
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;

    public static final String CATEGORY_CLIENT_GENERAL = "client_general";
    public static final String CATEGORY_COMMON_GENERAL = "common_general";
    public static final String CATEGORY_SERVER_GENERAL = "server_general";
    public static final String SUBCATEGORY_FLUID_TANK = "fluid_tank";

    public static IntValue TANK_CAPACITY;


    static {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General client setting").push(CATEGORY_CLIENT_GENERAL);
        // Client Config
        CLIENT_BUILDER.pop();

        COMMON_BUILDER.comment("General common setting").push(CATEGORY_COMMON_GENERAL);
        // Common config
        createFluidTankConfig(COMMON_BUILDER);
        COMMON_BUILDER.pop();

        SERVER_BUILDER.comment("General server setting").push(CATEGORY_SERVER_GENERAL);
        // Server Config
        SERVER_BUILDER.pop();

        CLIENT_CONFIG = CLIENT_BUILDER.build();
        COMMON_CONFIG = COMMON_BUILDER.build();
        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    private static void createFluidTankConfig(ForgeConfigSpec.Builder configBuilder) {
        configBuilder.comment("Fluid tank setting").push(SUBCATEGORY_FLUID_TANK);
        TANK_CAPACITY = configBuilder
                .defineInRange("Base tank capacity", 27, 0, Integer.MAX_VALUE);
        configBuilder.pop();
    }

}
