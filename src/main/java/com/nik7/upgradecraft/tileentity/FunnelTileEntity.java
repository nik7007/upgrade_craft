package com.nik7.upgradecraft.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.blocks.FunnelBlock.ENABLED;
import static com.nik7.upgradecraft.blocks.FunnelBlock.FACING;
import static com.nik7.upgradecraft.init.RegisterTileEntity.FUNNEL_TILE_ENTITY_TYPE;
import static com.nik7.upgradecraft.utils.LazyOptionalHelper.getHandler;

public class FunnelTileEntity extends TileFluidHandler implements ITickableTileEntity {
    private static final int FLUID_TRANSFER_QUANTITY = 5;


    public FunnelTileEntity() {
        super(FUNNEL_TILE_ENTITY_TYPE.get());
        this.tank = new FluidTank(5 * FluidAttributes.BUCKET_VOLUME);
    }


    @Override
    public void tick() {
        if (world == null || world.isRemote()) {
            return;
        }
        if (!getBlockState().get(ENABLED)) {
            return;
        }

        fill(world);
        drain(world);
    }

    private void fill(World world) {
        if (this.tank.getSpace() == 0) {
            return;
        }
        BlockPos up = getPos().up();
        IFluidHandler source = getFluidHandler(world, up, Direction.DOWN);
        if (source != null) {
            transferFluid(source, this.tank);
        }
    }

    @Nullable
    private IFluidHandler getFluidHandler(World world, BlockPos pos, Direction direction) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            LazyOptional<IFluidHandler> capability = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction);
            return getHandler(capability);
        }
        return null;
    }

    private void transferFluid(IFluidHandler source, IFluidHandler destination) {
        FluidStack fluidStack = source.drain(FLUID_TRANSFER_QUANTITY, FluidAction.SIMULATE);
        if (fluidStack.isEmpty()) {
            return;
        }
        int fill = destination.fill(fluidStack, FluidAction.EXECUTE);
        if (fill > 0) {
            source.drain(fill, FluidAction.EXECUTE);
        }
    }

    private void drain(World world) {
        if (this.tank.isEmpty()) {
            return;
        }

        Direction direction = getBlockState().get(FACING);
        BlockPos otherPosition = getPos().offset(direction);
        IFluidHandler destination = getFluidHandler(world, otherPosition, direction.getOpposite());
        if (destination != null) {
            transferFluid(this.tank, destination);
        }

    }
}
