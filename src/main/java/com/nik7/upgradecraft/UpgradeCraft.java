package com.nik7.upgradecraft;

import com.mojang.logging.LogUtils;
import com.nik7.upgradecraft.init.RegisterBlock;
import com.nik7.upgradecraft.init.RegisterItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(UpgradeCraft.MODID)
public class UpgradeCraft {
    public static final String MODID = "upgradecraft";
    private static final Logger LOGGER = LogUtils.getLogger();
    public UpgradeCraft() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the Deferred Register to the mod event bus so blocks get registered
        RegisterBlock.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        RegisterItem.register(modEventBus);
    }

}
