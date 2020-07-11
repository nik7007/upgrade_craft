package com.nik7.upgradecraft;

import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.init.RegisterItems;
import com.nik7.upgradecraft.init.RegisterTileEntity;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UpgradeCraft.MOD_ID)
public class UpgradeCraft {
    public final static String MOD_ID = "upgradecraft";
    public final static Logger LOGGER = LogManager.getLogger();

    public UpgradeCraft() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);


        RegisterBlocks.init();
        RegisterItems.init();
        RegisterTileEntity.init();

        LOGGER.info("Hello from UpgradeCraft!");

    }

}
