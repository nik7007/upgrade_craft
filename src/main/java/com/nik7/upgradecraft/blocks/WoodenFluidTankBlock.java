package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.WoodenFluidTankTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class WoodenFluidTankBlock extends AbstractFluidTankBlock implements BasicWooFluidTank {

    public WoodenFluidTankBlock() {
        super(Properties.create(Material.WOOD, MaterialColor.GREEN)
                .notSolid()
                .hardnessAndResistance(2.5F)
                .sound(SoundType.WOOD));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        spanParticles(worldIn, pos, rand);
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return (int) (300 * 0.20);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 60;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WoodenFluidTankTileEntity();
    }
}
