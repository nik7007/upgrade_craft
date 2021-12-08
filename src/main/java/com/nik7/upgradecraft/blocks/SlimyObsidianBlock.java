package com.nik7.upgradecraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class SlimyObsidianBlock extends Block {

    public SlimyObsidianBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.BLACK)
                .hardnessAndResistance(50.0F, 1200.0F)
                .slipperiness(1.05F)
                .sound(SoundType.STONE));
    }
}
