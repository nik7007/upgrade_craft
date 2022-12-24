package com.nik7.upgradecraft;

import com.mojang.logging.LogUtils;
import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.init.RegisterBlock;
import com.nik7.upgradecraft.init.RegisterBlockEntity;
import com.nik7.upgradecraft.init.RegisterItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(UpgradeCraft.MODID)
public class UpgradeCraft {
    public static final String MODID = "upgradecraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UpgradeCraft() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        RegisterBlock.register(modEventBus);
        RegisterItem.register(modEventBus);
        RegisterBlockEntity.register(modEventBus);

        // MinecraftForge.EVENT_BUS.register(ClientOnlyEventHandler.class);
    }

}
