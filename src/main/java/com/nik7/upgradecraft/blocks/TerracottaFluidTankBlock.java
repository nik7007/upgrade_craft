package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.AbstractFluidTankTileEntity;
import com.nik7.upgradecraft.tileentity.TerracottaFluidTankTileEntity;
import com.nik7.upgradecraft.utils.UpgCFluidHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class TerracottaFluidTankBlock extends AbstractFluidTankBlock {

    public TerracottaFluidTankBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.ADOBE)
                .setRequiresTool()
                .hardnessAndResistance(1.25F, 4.2F));
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        ItemStack pickBlock = super.getPickBlock(state, target, world, pos, player);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof AbstractFluidTankTileEntity) {
            AbstractFluidTankTileEntity fluidTankTileEntity = (AbstractFluidTankTileEntity) tileEntity;
            FluidStack fluidStack = fluidTankTileEntity.calculateFluidForSeparateTank();
            int initialCapacity = fluidTankTileEntity.getRawInitialCapacity();
            UpgCFluidHelper.writeFluidInfoIntoItemStack(pickBlock, fluidStack, initialCapacity);
        }

        return pickBlock;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerracottaFluidTankTileEntity();
    }
}
