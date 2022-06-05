package com.nik7.upgradecraft.blockentity

import com.nik7.upgradecraft.init.ModBlockEntities
import com.nik7.upgradecraft.util.ModBlockEntityTicker
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.items.wrapper.CombinedInvWrapper

class FluidFurnaceEntity(pWorldPosition: BlockPos, pBlockState: BlockState) :
    BlockEntity(ModBlockEntities.FLUID_FURNACE_ENTITY_TYPE.get(), pWorldPosition, pBlockState), ModBlockEntityTicker {

    private val inputsHandler: ItemStackHandler = ItemStackHandler()
    private val outputsHandler: ItemStackHandler = ItemStackHandler()

    private val inputs: LazyOptional<IItemHandler> = LazyOptional.of { inputsHandler }
    private val outputs: LazyOptional<IItemHandler> = LazyOptional.of { outputsHandler }

    private val combinedHandler = LazyOptional.of { createCombinedItemHandler() }

    override fun load(pTag: CompoundTag) {
        super.load(pTag)
        if (pTag.contains("inputsHandler")) {
            inputsHandler.deserializeNBT(pTag.getCompound("inputsHandler"))
        }
        if (pTag.contains("outputsHandler")) {
            outputsHandler.deserializeNBT(pTag.getCompound("outputsHandler"))
        }
    }

    override fun saveAdditional(pTag: CompoundTag) {
        super.saveAdditional(pTag)
        pTag.put("inputsHandler", inputsHandler.serializeNBT())
        pTag.put("outputsHandler", outputsHandler.serializeNBT())
    }

    override fun tick() {

    }

    private fun createCombinedItemHandler(): IItemHandler {
        return object : CombinedInvWrapper(inputsHandler, outputsHandler) {
            override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
                return ItemStack.EMPTY
            }

            override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
                return stack
            }
        }
    }

    private fun getItemHandler(facing: Direction?): LazyOptional<IItemHandler> {
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

    override fun setRemoved() {
        super.setRemoved()
        this.inputs.invalidate()
        this.outputs.invalidate()
    }

    override fun <T> getCapability(capability: Capability<T>, facing: Direction?): LazyOptional<T> {
        if (capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            val itemHandler = getItemHandler(facing)
            return itemHandler.cast()
        }
        return super.getCapability(capability, facing)
    }

}
