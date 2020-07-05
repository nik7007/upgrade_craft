package com.nik7.upgradecraft;

import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.init.RegisterItems;
import com.nik7.upgradecraft.init.RegisterTileEntity;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UpgradeCraft.MOD_ID)
public class UpgradeCraft {
    public final static String MOD_ID = "upgradecraft";
    public final static Logger LOGGER = LogManager.getLogger();

    public UpgradeCraft() {

        RegisterBlocks.init();
        RegisterItems.init();
        RegisterTileEntity.init();

        LOGGER.info("Hello from UpgradeCraft!");

    }

}
