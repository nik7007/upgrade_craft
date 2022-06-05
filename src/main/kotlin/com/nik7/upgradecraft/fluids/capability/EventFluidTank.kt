package com.nik7.upgradecraft.fluids.capability

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.templates.FluidTank
import java.util.function.Predicate

class EventFluidTank(capacity: Int, validator: Predicate<FluidStack>, private val onChange: (() -> Void)?) :
    FluidTank(capacity, validator) {

    constructor(capacity: Int, onChange: (() -> Void)?) : this(capacity, Predicate { true }, onChange) {
    }

    override fun onContentsChanged() {
        onChange?.invoke()
    }

}
