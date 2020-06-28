package com.nik7.upgradecraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class SlimyPlanksBlock extends Block {

    public SlimyPlanksBlock() {
        super(Properties.create(Material.WOOD, MaterialColor.DIRT)
                .hardnessAndResistance(2.0F, 3.0F)
                .slipperiness(1.05F)
                .sound(SoundType.WOOD));
    }
}
