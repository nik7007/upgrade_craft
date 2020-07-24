package com.nik7.upgradecraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class SlimyPlanksBlock extends Block {

    public SlimyPlanksBlock() {
        super(Properties.create(Material.WOOD, MaterialColor.DIRT)
                .hardnessAndResistance(2.0F, 3.0F)
                .slipperiness(1.05F)
                .sound(SoundType.WOOD));
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return (int) (300 * 0.60);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 10;
    }
}
