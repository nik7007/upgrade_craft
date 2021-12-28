package com.nik7.upgradecraft.blockentity;

import com.nik7.upgradecraft.init.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FluidFurnaceEntity extends AbstractMachineEntity {
    private static final Map<ResourceLocation, FuelInfo> FUEL_CACHE = new HashMap<>();
    private static final FuelInfo LAVA_INFO;

    static {
        float rawBurnAmount = ForgeHooks.getBurnTime(new ItemStack(Items.LAVA_BUCKET), RecipeType.BLASTING);
        float rawTickDuration = 1 / rawBurnAmount;
        int fluidTickDuration = rawTickDuration < 1 ? 1 : (int) (rawTickDuration + 0.5);
        int burnAmount = rawBurnAmount < 1 ? 1 : (int) (rawBurnAmount + 0.5);
        LAVA_INFO = new FuelInfo(fluidTickDuration, burnAmount, 1);
    }

    public FluidFurnaceEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState,
                Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME,
                null, null);
    }

    @Override
    public void tick() {

    }


    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int windowsIs, Inventory inventory, Player player) {
        return null;
    }

    private FuelInfo getOrCreateFuelInfo(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        ResourceLocation name = fluid.delegate.name();
        return FUEL_CACHE.computeIfAbsent(name, resourceLocation -> {

            Item bucket = fluid.getBucket();
            float rawBurnAmount = ForgeHooks.getBurnTime(new ItemStack(bucket), RecipeType.BLASTING);
            int fluidTickDuration;
            int burnAmount;
            if (rawBurnAmount == 0) {
                fluidTickDuration = LAVA_INFO.tickDuration();
                burnAmount = LAVA_INFO.burnAmount();
            } else {
                float rawTickDuration = 1 / rawBurnAmount;
                fluidTickDuration = rawTickDuration < 1 ? 1 : (int) (rawTickDuration + 0.5);
                burnAmount = rawBurnAmount < 1 ? 1 : (int) (rawBurnAmount + 0.5);
            }

            float cookTimeScale = Fluids.LAVA.getAttributes().getTemperature() / (float) fluid.getAttributes().getTemperature(fluidStack);
            return new FuelInfo(fluidTickDuration, burnAmount, cookTimeScale);
        });
    }

    private record FuelInfo(int tickDuration, int burnAmount, float cookTimeScale) {
    }

}
