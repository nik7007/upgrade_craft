package com.nik7.upgradecraft.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nik7.upgradecraft.client.utils.RenderHelper;
import com.nik7.upgradecraft.container.FluidFurnaceContainer;
import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        FluidFurnaceTileEntity tileEntity = container.getTileEntity();
        if (tileEntity != null) {
            FluidStack fluid = tileEntity.getFluid();
            int capacity = tileEntity.getCapacity();
            renderFluid(matrixStack, fluid, capacity, x, y);
        }
    }

    private void renderFluid(MatrixStack matrixStack, FluidStack fluidStack, int capacity, int x, int y) {
        float scale = fluidStack.getAmount() / (float) capacity;
        int argb = RenderHelper.getColorARGB(fluidStack, scale);

        if (fluidStack.getFluid().getAttributes().isGaseous(fluidStack)) {
            scale = 1;
        }
        TextureAtlasSprite fluidSprite = RenderHelper.locationToSprite(fluidStack.getFluid().getAttributes().getStillTexture(fluidStack));

        final float red = getRed(argb) / 255f;
        final float green = getGreen(argb) / 255f;
        final float blue = getBlue(argb) / 255f;
        final float alpha = getAlpha(argb) / 255f;

        final int baseTankX = 15;
        final int tankWidth = 16;
        final int baseTankY = 58;
        final int tankHeight = 32;

        this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        RenderSystem.color4f(red, green, blue, alpha);
        blit(matrixStack, baseTankX, baseTankY - (int) (tankHeight * scale), 0, tankWidth, (int) (tankHeight * scale), fluidSprite);

        RenderSystem.color4f(1f, 1f, 1f, 1f);

        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;

        if (x > baseTankX + xOffset && x < baseTankX + tankWidth + xOffset && y > baseTankY - tankHeight + yOffset && y < baseTankY + yOffset) {

            ITextComponent fluidName = fluidStack.getDisplayName();
            int amount = fluidStack.getAmount();
            String fluidAmount = amount + "/" + capacity + "mB";
            List<ITextComponent> tooltip = Arrays.asList(fluidName, new StringTextComponent(fluidAmount));

            this.func_243308_b(matrixStack, tooltip, x - xOffset, y - yOffset);
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
