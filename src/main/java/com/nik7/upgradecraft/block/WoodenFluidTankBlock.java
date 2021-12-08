package com.nik7.upgradecraft.block;

import com.nik7.upgradecraft.blockentity.WoodenFluidTankEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class WoodenFluidTankBlock extends AbstractFluidTankBlock {

    public static final BooleanProperty BURNING = BooleanProperty.create("burning");


    public WoodenFluidTankBlock() {
        super(Properties.of(Material.WOOD, DyeColor.GREEN)
                .noOcclusion()
                .strength(2.5F)
                .sound(SoundType.WOOD));
        this.registerDefaultState(this.defaultBlockState().setValue(BURNING, false));

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        super.createBlockStateDefinition(blockStateBuilder);
        blockStateBuilder.add(BURNING);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        if (blockState.getValue(BURNING)) {
            if (random.nextInt(24) == 0) {
                level.playLocalSound((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
            for (int j = 0; j < 2; ++j) {
                double x = (double) blockPos.getX() + random.nextDouble();
                double y = (double) blockPos.getY() + random.nextDouble();
                double z = (double) blockPos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            }

        }

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WoodenFluidTankEntity(blockPos, blockState);
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
}
