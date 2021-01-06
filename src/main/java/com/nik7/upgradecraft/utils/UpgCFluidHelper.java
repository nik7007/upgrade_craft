package com.nik7.upgradecraft.utils;

import com.nik7.upgradecraft.init.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public final class UpgCFluidHelper {
    private UpgCFluidHelper() {
    }

    public static boolean fillDrainWithItem(World worldIn, BlockPos pos, PlayerEntity player, Hand handIn) {
        if (worldIn.isRemote) {
            return true;
        }
        IFluidHandler tank = LazyOptionalHelper.getHandler(FluidUtil.getFluidHandler(worldIn, pos, null));
        if (tank == null) {
            return false;
        }
        ItemStack stack = player.getHeldItem(handIn);
        IFluidHandlerItem itemTank = LazyOptionalHelper.getHandler(FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(stack, 1)));
        if (itemTank == null) {
            return false;
        }


        FluidStack fluidInTank = tank.getFluidInTank(0);
        FluidStack fluidInItem;

        if (!fluidInTank.isEmpty()) {
            fluidInItem = itemTank.drain(new FluidStack(fluidInTank, Integer.MAX_VALUE), IFluidHandler.FluidAction.SIMULATE);
        } else {
            fluidInItem = itemTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
        }

        if (fluidInItem.isEmpty()) {
            IFluidHandler.FluidAction fluidAction = player.isCreative() ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
            int fill = itemTank.fill(fluidInTank, fluidAction);

            if (fill > 0) {
                tank.drain(fill, IFluidHandler.FluidAction.EXECUTE);
                if (!player.isCreative()) {
                    ItemStack container = itemTank.getContainer();
                    UpgCInventoryHelper.substituteItemToStack(stack, container, player, handIn);
                }
            }

        } else {

            int fill = tank.fill(fluidInItem, IFluidHandler.FluidAction.SIMULATE);
            if (fill == fluidInItem.getAmount()) {
                tank.fill(fluidInItem, IFluidHandler.FluidAction.EXECUTE);
                if (!player.isCreative()) {
                    itemTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
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
        return true;
    }

    public static void writeFluidInfoIntoItemStack(ItemStack itemStack, FluidStack fluidStack, int capacity) {
        CompoundNBT tag = itemStack.getTag();
        if (tag == null) {
            tag = new CompoundNBT();
            itemStack.setTag(tag);
        }
        tag.putInt("capacity", capacity);
        if (!fluidStack.isEmpty()) {
            FluidTank tank = new FluidTank(capacity * FluidAttributes.BUCKET_VOLUME);
            tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            CompoundNBT tankTag = new CompoundNBT();
            tank.writeToNBT(tankTag);
            tag.put("BlockEntityTag", tankTag);
        }
    }

    public static FluidTank readFluidTankFromItemStack(ItemStack itemStack) {
        CompoundNBT tag = itemStack.getTag();
        int capacity = getCapacityFromItemStack(itemStack);
        FluidTank tank = new FluidTank(capacity);
        if (tag != null) {
            tank.readFromNBT(tag.getCompound("BlockEntityTag"));
        }

        return tank;
    }

    public static int getCapacityFromItemStack(ItemStack itemStack) {
        int capacity;
        CompoundNBT tag = itemStack.getTag();
        if (tag != null) {
            capacity = tag.getInt("capacity");
        } else {
            capacity = Config.TANK_CAPACITY.get();
        }
        return capacity * FluidAttributes.BUCKET_VOLUME;
    }

    public static void writeTankInfoIntoTooltip(List<ITextComponent> tooltip, FluidTank tank) {
        writeTankInfoIntoTooltip(tooltip, tank.getFluid(), tank.getCapacity());
    }

    public static void writeTankInfoIntoTooltip(List<ITextComponent> tooltip, FluidStack fluidStack, int tankCapacity) {
        if (!fluidStack.isEmpty()) {
            tooltip.add(fluidStack.getDisplayName());
        } else {
            tooltip.add((new TranslationTextComponent("tooltip.upgradecraft.fluid.empty").mergeStyle(TextFormatting.ITALIC)));
        }
        tooltip.add(new StringTextComponent(fluidStack.getAmount() + "/" + tankCapacity + "mB"));
    }

}
