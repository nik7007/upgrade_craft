package com.nik7.upgradecraft.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nik7.upgradecraft.container.FluidFurnaceContainer;
import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity.FLUID_TICK_DURATION;

public class FluidFurnaceScreen extends FluidContainerScreen<FluidFurnaceContainer> {
    public final static ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/container/fluid_furnace_gui.png");

    public FluidFurnaceScreen(FluidFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        FluidFurnaceTileEntity tileEntity = screenContainer.getTileEntity();
        this.addFluidTankSlot(
                new RenderFluidTankSlot(
                        () -> tileEntity != null ? tileEntity.getFluid() : FluidStack.EMPTY,
                        () -> tileEntity != null ? tileEntity.getCapacity() : 0,
                        15, 16, 58, 32)
        );
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);

        int cookTimeTotal = container.getCookTimeTotal();
        int cookTime = container.getCookTime();

        int cookProgress = (int) ((cookTime / (float) cookTimeTotal) * 24);

        this.blit(matrixStack, relX + 79, relY + 34, 176, 14, cookProgress + 1, 16);

        int burnTime = container.getBurnTime();
        int burnProgress = (int) ((burnTime / (float) FLUID_TICK_DURATION) * 14);
        this.blit(matrixStack, relX + 56, relY + 36 + 13 - burnProgress, 176, 13 - burnProgress, 14, burnProgress);
    }
}
