package com.nik7.upgradecraft.fluids.capability

import net.minecraft.nbt.CompoundTag
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction
import net.minecraftforge.fluids.capability.templates.FluidTank
import java.util.function.Predicate
import javax.annotation.Nonnull

class WrapperFluidTank<T : FluidTank>(var internalTank: T) : FluidTank(internalTank.capacity) {

    override fun setCapacity(capacity: Int): FluidTank {
        return internalTank.setCapacity(capacity)
    }

    override fun setValidator(validator: Predicate<FluidStack>): FluidTank {
        return internalTank.setValidator(validator)
    }

    override fun readFromNBT(nbt: CompoundTag): FluidTank {
        return internalTank.readFromNBT(nbt)
    }

    override fun writeToNBT(nbt: CompoundTag): CompoundTag {
        return internalTank.writeToNBT(nbt)
    }

    @Nonnull
    override fun getFluid(): FluidStack {
        return internalTank.fluid
    }

    override fun getFluidAmount(): Int {
        return internalTank.fluidAmount
    }

    override fun getCapacity(): Int {
        return internalTank.capacity
    }

    override fun isFluidValid(stack: FluidStack): Boolean {
        return internalTank.isFluidValid(stack)
    }

    override fun getTanks(): Int {
        return internalTank.tanks
    }

    @Nonnull
    override fun getFluidInTank(tank: Int): FluidStack {
        return internalTank.getFluidInTank(tank)
    }

    override fun getTankCapacity(tank: Int): Int {
        return internalTank.getTankCapacity(tank)
    }

    override fun isFluidValid(tank: Int, @Nonnull stack: FluidStack): Boolean {
        return internalTank.isFluidValid(tank, stack)
    }

    override fun fill(resource: FluidStack, action: FluidAction): Int {
        return internalTank.fill(resource, action)
    }

    @Nonnull
    override fun drain(resource: FluidStack, action: FluidAction): FluidStack {
        return internalTank.drain(resource, action)
    }

    @Nonnull
    override fun drain(maxDrain: Int, action: FluidAction): FluidStack {
        return internalTank.drain(maxDrain, action)
    }

    override fun isEmpty(): Boolean {
        return internalTank.isEmpty
    }

    override fun getSpace(): Int {
        return internalTank.space
    }
}
