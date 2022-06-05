package com.nik7.upgradecraft.util

import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraftforge.fluids.capability.templates.FluidTank

interface ModBlockEntityTicker {
    fun tick()
}


fun loadTanks(tagList: ListTag, tanks: NonNullList<FluidTank>) {
    for (i in 0 until tagList.size) {
        val fluidTag: CompoundTag = tagList.getCompound(i)
        val slot = fluidTag.getInt("Slot")
        if (slot >= 0 && slot < tanks.size) {
            tanks[slot].readFromNBT(fluidTag)
        }
    }
}

fun writeTanks(tanks: NonNullList<FluidTank>): ListTag {
    val nbtTagList = ListTag()

    tanks.forEachIndexed { index, fluidTank ->
        val fluidTag = CompoundTag()
        fluidTag.putInt("Slot", index)
        fluidTank.writeToNBT(fluidTag)
        nbtTagList.add(fluidTag)
    }

    return nbtTagList;
}
