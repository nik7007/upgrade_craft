package com.nik7.upgradecraft.blockentity

import com.nik7.upgradecraft.util.ModBlockEntityTicker
import com.nik7.upgradecraft.util.loadTanks
import com.nik7.upgradecraft.util.writeTanks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.items.wrapper.CombinedInvWrapper
import javax.annotation.Nonnull

abstract class BaseFluidMachineEntity(
    pType: BlockEntityType<BaseFluidMachineEntity>,
    protected var tanks: NonNullList<FluidTank>,
    protected val inputsHandler: ItemStackHandler,
    protected val outputsHandler: ItemStackHandler,
    pWorldPosition: BlockPos,
    pBlockState: BlockState
) : BlockEntity(pType, pWorldPosition, pBlockState), ModBlockEntityTicker {

    protected var tickCont: Int = 0;

    protected val fluidsHolder: List<LazyOptional<IFluidHandler>> = tanks.map { LazyOptional.of { it } }

    protected val inputs: LazyOptional<IItemHandler> = LazyOptional.of { inputsHandler }
    protected val outputs: LazyOptional<IItemHandler> = LazyOptional.of { outputsHandler }

    protected val combinedHandler = LazyOptional.of { createCombinedItemHandler() }

    override fun load(pTag: CompoundTag) {
        super.load(pTag)
        if (pTag.contains("tanks")) {
            val nbtTanks = pTag.getList("tanks", Tag.TAG_COMPOUND.toInt())
            loadTanks(nbtTanks, tanks)
        }

        if (pTag.contains("inputsHandler")) {
            inputsHandler.deserializeNBT(pTag.getCompound("inputsHandler"))
        }
        if (pTag.contains("outputsHandler")) {
            outputsHandler.deserializeNBT(pTag.getCompound("outputsHandler"))
        }

        tickCont = pTag.getInt("tickCont")
    }

    override fun saveAdditional(pTag: CompoundTag) {
        super.saveAdditional(pTag)
        pTag.put("tanks", writeTanks(tanks))

        pTag.put("inputsHandler", inputsHandler.serializeNBT())
        pTag.put("outputsHandler", outputsHandler.serializeNBT())

        pTag.putInt("tickCont", tickCont)
    }

    override fun tick() {
        tickCont++
        tickCont %= Int.MAX_VALUE
    }

    override fun setRemoved() {
        super.setRemoved()
        this.fluidsHolder.forEach { it.invalidate() }
        this.inputs.invalidate()
        this.outputs.invalidate()
    }

    protected fun createCombinedItemHandler(): IItemHandler {
        return object : CombinedInvWrapper(inputsHandler, outputsHandler) {
            override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
                return ItemStack.EMPTY
            }

            override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
                return stack
            }
        }
    }

    abstract fun selectTankForCapability(facing: Direction?): LazyOptional<IFluidHandler>

    protected fun getItemHandler(facing: Direction?): LazyOptional<IItemHandler> {
        return when (facing) {
            null -> {
                combinedHandler.cast()
            }
            Direction.DOWN -> {
                outputs.cast()
            }
            else -> {
                inputs.cast()
            }
        }
    }

    @Nonnull
    override fun <T> getCapability(@Nonnull capability: Capability<T>, facing: Direction?): LazyOptional<T> {
        return when {
            capability === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY -> {
                selectTankForCapability(facing).cast()
            }
            capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY -> {
                getItemHandler(facing).cast()
            }
            else -> super.getCapability(capability, facing)
        }
    }

}
