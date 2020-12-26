package com.nik7.upgradecraft.items;

import com.nik7.upgradecraft.capabilities.FluidTankCapabilityProvider;
import com.nik7.upgradecraft.init.Config;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import static com.nik7.upgradecraft.utils.UpgCFluidHelper.getCapacityFromItemStack;
import static com.nik7.upgradecraft.utils.UpgCFluidHelper.readFluidTankFromItemStack;

public class TerracottaFluidTankBlockItem extends UpgCBlockItem {

    public TerracottaFluidTankBlockItem(Block blockIn) {
        super(blockIn);
    }

    public TerracottaFluidTankBlockItem(Block blockIn, Supplier<Callable<ItemStackTileEntityRenderer>> supplier) {
        super(blockIn, supplier);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        CompoundNBT tag = stack.getTag();
        if (tag == null) {
            tag = new CompoundNBT();
        }
        int capacity = Config.TANK_CAPACITY.get();
        tag.putInt("capacity", capacity);
        stack.setTag(tag);
        return new FluidTankCapabilityProvider(stack, capacity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        boolean sneakPressed = Screen.hasShiftDown();
        if (sneakPressed) {
            addedFluidInformation(stack, tooltip);
        } else {
            tooltip.add((new StringTextComponent("<SHIFT>").mergeStyle(TextFormatting.ITALIC)));
        }
    }

    private void addedFluidInformation(ItemStack stack, List<ITextComponent> tooltip) {
        FluidTank tank = readFluidTankFromItemStack(stack);
        int capacity = getCapacityFromItemStack(stack);

        FluidStack fluidStack = tank.getFluid();
        ITextComponent fluidName = fluidStack.getDisplayName();
        String fluidAmount = fluidStack.getAmount() + "/" + capacity + "mB";
        tooltip.add(new StringTextComponent(""));
        tooltip.add(fluidName);
        tooltip.add(new StringTextComponent(fluidAmount));
    }
}
