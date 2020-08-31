package com.nik7.upgradecraft.client.gui.container;

import net.minecraftforge.fluids.FluidStack;

import java.util.function.Supplier;

public class RenderFluidTankSlot {
    private final Supplier<FluidStack> fluidStack;
    private final Supplier<Integer> capacity;

    private final int baseTankX;
    private final int tankWidth;
    private final int baseTankY;
    private final int tankHeight;

    public RenderFluidTankSlot(Supplier<FluidStack> fluidStack,
                               Supplier<Integer> capacity,
                               int baseTankX,
                               int tankWidth,
                               int baseTankY,
                               int tankHeight) {
        this.fluidStack = fluidStack;
        this.capacity = capacity;
        this.baseTankX = baseTankX;
        this.tankWidth = tankWidth;
        this.baseTankY = baseTankY;
        this.tankHeight = tankHeight;
    }

    public FluidStack getFluidStack() {
        return fluidStack.get();
    }

    public int getCapacity() {
        return capacity.get();
    }

    public int getBaseTankX() {
        return baseTankX;
    }

    public int getTankWidth() {
        return tankWidth;
    }

    public int getBaseTankY() {
        return baseTankY;
    }

    public int getTankHeight() {
        return tankHeight;
    }

    public boolean isHovered(int mouseX, int mouseY, int guiLeft, int guiTop) {
        mouseX -= guiLeft;
        mouseY -= guiTop;
        return mouseX >= baseTankX && mouseX < baseTankX + tankWidth && mouseY >= baseTankY - tankHeight && mouseY < baseTankY;
    }

    public float getFluidScale() {
        return fluidStack.get().getAmount() / (float) getCapacity();
    }

}
