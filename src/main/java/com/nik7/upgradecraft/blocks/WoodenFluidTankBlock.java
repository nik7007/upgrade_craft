package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.WoodenFluidTankTileEntity;
import com.nik7.upgradecraft.utils.LazyOptionalHelper;
import com.nik7.upgradecraft.utils.UpgCInventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class WoodenFluidTankBlock extends Block {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public WoodenFluidTankBlock() {
        super(Properties.create(Material.WOOD, MaterialColor.GREEN)
                .notSolid()
                .hardnessAndResistance(2.5F)
                .sound(SoundType.WOOD));
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        }
        IFluidHandler tank = LazyOptionalHelper.getHandler(FluidUtil.getFluidHandler(worldIn, pos, null));
        if (tank == null) {
            return ActionResultType.PASS;
        }
        ItemStack stack = player.getHeldItem(handIn);
        IFluidHandlerItem itemTank = LazyOptionalHelper.getHandler(FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(stack, 1)));
        if (itemTank == null) {
            return ActionResultType.PASS;
        }


        FluidStack fluidInTank = tank.getFluidInTank(0);
        FluidStack fluidInItem;

        if (!fluidInTank.isEmpty()) {
            fluidInItem = itemTank.drain(new FluidStack(fluidInTank, Integer.MAX_VALUE), FluidAction.SIMULATE);
        } else {
            fluidInItem = itemTank.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
        }

        if (fluidInItem.isEmpty()) {
            FluidAction fluidAction = player.isCreative() ? FluidAction.SIMULATE : FluidAction.EXECUTE;
            int fill = itemTank.fill(fluidInTank, fluidAction);

            if (fill > 0) {
                tank.drain(fill, FluidAction.EXECUTE);
                if (!player.isCreative()) {
                    ItemStack container = itemTank.getContainer();
                    UpgCInventoryHelper.substituteItemToStack(stack, container, player, handIn);
                }
            }

        } else {

            int fill = tank.fill(fluidInItem, FluidAction.SIMULATE);
            if (fill == fluidInItem.getAmount()) {
                tank.fill(fluidInItem, FluidAction.EXECUTE);
                if (!player.isCreative()) {
                    itemTank.drain(Integer.MAX_VALUE, FluidAction.EXECUTE);
                    ItemStack container = itemTank.getContainer();
                    if (!container.isEmpty()) {
                        UpgCInventoryHelper.substituteItemToStack(stack, container, player, handIn);
                    } else {
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            player.setHeldItem(handIn, ItemStack.EMPTY);
                        }
                    }
                }
            }

        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WoodenFluidTankTileEntity();
    }
}
