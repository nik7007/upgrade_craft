package com.nik7.upgradecraft.block.slimywood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class SlimyPlanksBlock extends Block {
    public static final int FLAMMABILITY = (int) (300 * 0.25);
    public static final int FIRE_SPREAD_SPEED = 60;

    public SlimyPlanksBlock() {
        super(Properties.of(Material.WOOD, MaterialColor.DIRT)
                .strength(2.0F, 3.0F)
                .friction(1.05F)
                .sound(SoundType.WOOD));
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FLAMMABILITY;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FIRE_SPREAD_SPEED;
    }

}
