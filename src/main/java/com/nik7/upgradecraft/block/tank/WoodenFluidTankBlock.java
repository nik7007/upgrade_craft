package com.nik7.upgradecraft.block.tank;

import com.nik7.upgradecraft.block.tank.entity.WoodenFluidTankEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class WoodenFluidTankBlock extends AbstractFluidTankBlock {

    public WoodenFluidTankBlock() {
        super(Properties.of(Material.WOOD, DyeColor.GREEN)
                .noOcclusion()
                .strength(2.5F)
                .sound(SoundType.WOOD));
        // this.registerDefaultState(this.defaultBlockState().setValue(BURNING, false));

    }


    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.getValue(WATERLOGGED)) {
            return 0;
        } else {
            return (int) (300 * 0.10);
        }
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.getValue(WATERLOGGED)) {
            return 0;
        } else {
            return 60;
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WoodenFluidTankEntity(blockPos, blockState);
    }
}
