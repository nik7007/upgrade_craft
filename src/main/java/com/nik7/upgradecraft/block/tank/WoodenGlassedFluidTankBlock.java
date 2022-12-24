package com.nik7.upgradecraft.block.tank;

import com.nik7.upgradecraft.block.tank.entity.WoodenGlassedFluidTankEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WoodenGlassedFluidTankBlock extends WoodenFluidTankBlock {

    public WoodenGlassedFluidTankBlock() {
        super();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack,
                                @Nullable BlockGetter blockGetter,
                                @NotNull List<Component> tooltip,
                                @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, blockGetter, tooltip, flagIn);
        tooltip.add((Component.translatable("tooltip.upgradecraft.tank.glassed")).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        BlockEntity blockEntity = blockGetter.getBlockEntity(pos);
        if (blockEntity instanceof WoodenGlassedFluidTankEntity tankEntity) {
            return tankEntity.getLuminosity();
        }
        return 0;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WoodenGlassedFluidTankEntity(blockPos, blockState);
    }

    public RenderShape getRenderShape(BlockState p_49753_) {
        return RenderShape.MODEL;
    }

}
