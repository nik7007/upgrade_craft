package com.nik7.upgradecraft.util;

import net.minecraftforge.fluids.FluidStack;

public final class FluidHelper {
    private FluidHelper() {
    }

    public static boolean isFluidHot(FluidStack fluid) {
        int temperature = fluid.getFluid().getFluidType().getTemperature(fluid);
        return temperature > 250 + 273;
    }
}
