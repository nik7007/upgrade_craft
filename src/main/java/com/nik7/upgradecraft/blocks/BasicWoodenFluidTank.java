package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.AbstractFluidTankTileEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public interface BasicWoodenFluidTank {

    @OnlyIn(Dist.CLIENT)
    default void spanParticles(World worldIn, BlockPos pos, Random rand) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof AbstractFluidTankTileEntity) {
            if (((AbstractFluidTankTileEntity) tileEntity).isFluidHot()) {

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
    }
}
