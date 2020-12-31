package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.FunnelTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.utils.UpgCFluidHelper.fillDrainWithItem;

public class FunnelBlock extends AbstractFluidContainerBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING_EXCEPT_UP;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    private static final VoxelShape INPUT_SHAPE = Block.makeCuboidShape(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape MIDDLE_SHAPE = Block.makeCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 10.0D, 12.0D);
    private static final VoxelShape BASE_SHAPE = VoxelShapes.or(MIDDLE_SHAPE, INPUT_SHAPE);

    private static final VoxelShape DOWN_SHAPE = VoxelShapes.or(BASE_SHAPE, Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D));
    private static final VoxelShape EAST_SHAPE = VoxelShapes.or(BASE_SHAPE, Block.makeCuboidShape(12.0D, 4.0D, 6.0D, 16.0D, 8.0D, 10.0D));
    private static final VoxelShape NORTH_SHAPE = VoxelShapes.or(BASE_SHAPE, Block.makeCuboidShape(6.0D, 4.0D, 0.0D, 10.0D, 8.0D, 4.0D));
    private static final VoxelShape SOUTH_SHAPE = VoxelShapes.or(BASE_SHAPE, Block.makeCuboidShape(6.0D, 4.0D, 12.0D, 10.0D, 8.0D, 16.0D));
    private static final VoxelShape WEST_SHAPE = VoxelShapes.or(BASE_SHAPE, Block.makeCuboidShape(0.0D, 4.0D, 6.0D, 4.0D, 8.0D, 10.0D));

    public FunnelBlock() {
        super(Properties.create(Material.IRON, MaterialColor.STONE)
                .setRequiresTool()
                .hardnessAndResistance(3.0F, 4.8F)
                .sound(SoundType.METAL)
                .notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.DOWN).with(ENABLED, true));
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.isIn(state.getBlock())) {
            this.updateState(worldIn, pos, state);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        this.updateState(worldIn, pos, state);
    }

    private void updateState(World worldIn, BlockPos pos, BlockState state) {
        boolean flag = !worldIn.isBlockPowered(pos);
        if (flag != state.get(ENABLED)) {
            worldIn.setBlockState(pos, state.with(ENABLED, flag), Constants.BlockFlags.DEFAULT_AND_RERENDER);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        }
        return fillDrainWithItem(worldIn, pos, player, handIn) ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
            case DOWN:
                return DOWN_SHAPE;
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            case EAST:
                return EAST_SHAPE;
            default:
                return BASE_SHAPE;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getFace().getOpposite();
        return this.getDefaultState().with(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction).with(ENABLED, true);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FunnelTileEntity();
    }
}
