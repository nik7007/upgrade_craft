package com.nik7.upgradecraft.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nik7.upgradecraft.client.BaseFluidHandlerRenderer;
import com.nik7.upgradecraft.container.FluidFurnaceContainer;
import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity.FLUID_TICK_DURATION;
import static net.minecraft.util.ColorHelper.PackedColor.*;

public class FluidFurnaceScreen extends ContainerScreen<FluidFurnaceContainer> {
    private final static ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/container/fluid_furnace_gui.png");

    public FluidFurnaceScreen(FluidFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();

        assert this.minecraft != null;
        this.titleX = (this.xSize - this.font.func_238414_a_(this.title)) / 2;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        FluidFurnaceTileEntity tileEntity = container.getTileEntity();
        if (tileEntity != null) {
            FluidStack fluid = tileEntity.getFluid();

            float scale = fluid.getAmount() / (float) tileEntity.getCapacity();
            int argb = BaseFluidHandlerRenderer.getColorARGB(fluid, scale);

            if (fluid.getFluid().getAttributes().isGaseous(fluid)) {
                scale = 1;
            }
            TextureAtlasSprite fluidSprite = BaseFluidHandlerRenderer.locationToSprite(fluid.getFluid().getAttributes().getStillTexture(fluid));

            float red = getRed(argb) / 255f;
            float green = getGreen(argb) / 255f;
            float blue = getBlue(argb) / 255f;
            float alpha = getAlpha(argb) / 255f;

            this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            RenderSystem.color4f(red, green, blue, alpha);
            blit(matrixStack, 15, 58 - (int) (32 * scale), 0, 16, (int) (32 * scale), fluidSprite);
        }
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
