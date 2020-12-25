package com.nik7.upgradecraft.items;

import com.nik7.upgradecraft.capabilities.FluidTankCapabilityProvider;
import com.nik7.upgradecraft.init.Config;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.List;

public class TerracottaFluidTankBlockItem extends UpgCBlockItem {

    public TerracottaFluidTankBlockItem(Block blockIn) {
        super(blockIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidTankCapabilityProvider(stack, Config.TANK_CAPACITY.get());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        FluidTank tank = new FluidTank(Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME);
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            tank.readFromNBT(tag);
        }

        boolean sneakPressed = Screen.hasShiftDown();
        if (sneakPressed) {

            FluidStack fluidStack = tank.getFluid();
            int capacity = tank.getCapacity();

            ITextComponent fluidName = fluidStack.getDisplayName();
            String fluidAmount = fluidStack.getAmount() + "/" + capacity + "mB";
            tooltip.add(new StringTextComponent(""));
            tooltip.add(fluidName);
            tooltip.add(new StringTextComponent(fluidAmount));
        } else {
            tooltip.add((new StringTextComponent("<SHIFT>").mergeStyle(TextFormatting.ITALIC)));
        }
    }
}
