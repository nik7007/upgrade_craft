package com.nik7.upgradecraft.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nik7.upgradecraft.container.FluidInfuserContainer;
import com.nik7.upgradecraft.tileentity.FluidInfuserTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class FluidInfuserScreen extends FluidContainerScreen<FluidInfuserContainer> {
    public final static ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/container/fluid_infuser_gui.png");


    public FluidInfuserScreen(FluidInfuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        FluidInfuserTileEntity tileEntity = screenContainer.getTileEntity();
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

        int dissolve = getDissolveTick();
        this.blit(matrixStack, relX + 52, relY + 52 - dissolve, 176, 13 - dissolve, 15, dissolve);

        int infuse = getInfuseTick();
        this.blit(matrixStack, relX + 105, relY + 37, 176, 14, infuse + 1, 16);

    }

    private int getDissolveTick() {
        return (int) (13 * (container.getDissolveTime() / (float) container.getTotalDissolveTime()));
    }

    private int getInfuseTick() {
        return (int) (24 * (container.getInfuseTime() / (float) container.getTotalInfuseTime()));
    }

}
