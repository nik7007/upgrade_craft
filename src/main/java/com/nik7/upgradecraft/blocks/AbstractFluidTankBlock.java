package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.state.properties.TankType;
import com.nik7.upgradecraft.tileentity.AbstractFluidTankTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
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

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.utils.UpgCFluidHelper.fillDrainWithItem;

public abstract class AbstractFluidTankBlock extends Block {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<TankType> TYPE = EnumProperty.create("type", TankType.class);
    public static final BooleanProperty MIXED = BooleanProperty.create("mixed");

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    protected AbstractFluidTankBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(TYPE, TankType.SINGLE).with(WATERLOGGED, false).with(MIXED, false));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        }
        return fillDrainWithItem(worldIn, pos, player, handIn) ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (state.get(TYPE) == TankType.SINGLE && !worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof AbstractFluidTankTileEntity) {
                ((AbstractFluidTankTileEntity) tileEntity).mergeTank();
            }
        }
    }

    @Deprecated
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock() && !worldIn.isRemote) {
            TankType tankType = state.get(TYPE);
            if (tankType != TankType.SINGLE) {
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if (tileEntity instanceof AbstractFluidTankTileEntity) {
                    ((AbstractFluidTankTileEntity) tileEntity).separateTank(tankType);
                }
            }
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, TYPE, MIXED);
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
        FluidState FluidState = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(WATERLOGGED, FluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof AbstractFluidTankTileEntity) {
            FluidStack fluid = ((AbstractFluidTankTileEntity) tileEntity).getFluid();
            int capacity = ((AbstractFluidTankTileEntity) tileEntity).getCapacity();

            float compareRaw = 15 * fluid.getAmount() / (float) capacity;
            if (compareRaw > 0 && compareRaw < 1) {
                compareRaw = 1;
            }

            return (int) compareRaw;
        }
        return 0;
    }
}
