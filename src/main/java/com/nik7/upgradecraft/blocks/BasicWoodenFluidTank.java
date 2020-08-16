package com.nik7.upgradecraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public abstract class BasicWoodenFluidTank extends AbstractFluidTankBlock {

    public static final BooleanProperty BURNING = BooleanProperty.create("burning");

    protected BasicWoodenFluidTank() {
        super(Properties.create(Material.WOOD, MaterialColor.GREEN)
                .notSolid()
                .hardnessAndResistance(2.5F)
                .sound(SoundType.WOOD));

        this.setDefaultState(this.getDefaultState().with(BURNING, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(BURNING);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(BURNING)) {
            if (rand.nextInt(24) == 0) {
                worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
            }
            for (int j = 0; j < 2; ++j) {
                double x = (double) pos.getX() + rand.nextDouble();
                double y = (double) pos.getY() + rand.nextDouble();
                double z = (double) pos.getZ() + rand.nextDouble();
                worldIn.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        if (state.get(WATERLOGGED)) {
            return 0;
        } else {
            return (int) (300 * 0.10);
        }
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        if (state.get(WATERLOGGED)) {
            return 0;
        } else {
            return 60;
        }
    }
}
