package com.nik7.upgradecraft.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nik7.upgradecraft.client.utils.RenderHelper;
import com.nik7.upgradecraft.container.FluidFurnaceContainer;
import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity.FLUID_TICK_DURATION;
import static net.minecraft.util.ColorHelper.PackedColor.*;

public class FluidFurnaceScreen extends ContainerScreen<FluidFurnaceContainer> {
    public final static ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/container/fluid_furnace_gui.png");
    private final RenderFluidTankSlot fluidTankSlot;

    public FluidFurnaceScreen(FluidFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        FluidFurnaceTileEntity tileEntity = screenContainer.getTileEntity();
        this.fluidTankSlot = new RenderFluidTankSlot(
                () -> tileEntity != null ? tileEntity.getFluid() : FluidStack.EMPTY,
                () -> tileEntity != null ? tileEntity.getCapacity() : 0,
                15, 16, 58, 32);
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
        this.fluidTooltip(matrixStack, fluidTankSlot, mouseX, mouseY);
    }

    protected static void drawTiledTexture(int x, int y, int width, int height, TextureAtlasSprite icon) {

        for (int w = 0; w < width; w += 16) {
            for (int h = 0; h < height; h += 16) {
                int drawWidth = Math.min(width - w, 16);
                int drawHeight = Math.min(height - h, 16);
                drawScaledTexturedModalRectFromSprite(x + w, y + h, drawWidth, drawHeight, icon);
            }
        }

    }

    protected static void drawScaledTexturedModalRectFromSprite(int x, int y, int width, int height, TextureAtlasSprite icon) {

        if (icon == null) {
            return;
        }
        float minU = icon.getMinU();
        float maxU = icon.getMaxU();
        float minV = icon.getMinV();
        float maxV = icon.getMaxV();

        float u = minU + (maxU - minU) * width / 16F;
        float v = minV + (maxV - minV) * height / 16F;

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0).tex(minU, v).endVertex();
        buffer.pos(x + width, y + height, 0).tex(u, v).endVertex();
        buffer.pos(x + width, y, 0).tex(u, minV).endVertex();
        buffer.pos(x, y, 0).tex(minU, minV).endVertex();
        Tessellator.getInstance().draw();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        renderFluid(fluidTankSlot);
    }

    private void renderFluid(RenderFluidTankSlot fluidTankSlot) {
        if (this.minecraft != null) {
            FluidStack fluidStack = fluidTankSlot.getFluidStack();
            float scale = fluidTankSlot.getFluidScale();
            int argb = RenderHelper.getColorARGB(fluidTankSlot.getFluidStack(), scale);

            if (fluidStack.getFluid().getAttributes().isGaseous(fluidStack)) {
                scale = 1;
            }
            TextureAtlasSprite fluidSprite = RenderHelper.locationToSprite(fluidStack.getFluid().getAttributes().getStillTexture(fluidStack));


            final float red = getRed(argb) / 255f;
            final float green = getGreen(argb) / 255f;
            final float blue = getBlue(argb) / 255f;
            final float alpha = getAlpha(argb) / 255f;

            final int baseTankX = fluidTankSlot.getBaseTankX();
            final int tankWidth = fluidTankSlot.getTankWidth();
            final int baseTankY = fluidTankSlot.getBaseTankY();
            final int tankHeight = fluidTankSlot.getTankHeight();


            this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            GL11.glPushMatrix();
            RenderSystem.color4f(red, green, blue, alpha);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            int fluidHeight = scale == 0 ? 0 : Math.max((int) (tankHeight * scale), 1);
            drawTiledTexture(baseTankX, baseTankY - fluidHeight, tankWidth, fluidHeight, fluidSprite);

            RenderSystem.color4f(1f, 1f, 1f, 1f);
            GL11.glPopMatrix();
        }
    }


    private void fluidTooltip(MatrixStack matrixStack, RenderFluidTankSlot fluidTankSlot, int mouseX, int mouseY) {
        if (fluidTankSlot.isHovered(mouseX, mouseY, guiLeft, guiTop)) {
            FluidStack fluidStack = fluidTankSlot.getFluidStack();
            ITextComponent fluidName = fluidStack.getDisplayName();
            int amount = fluidStack.getAmount();
            String fluidAmount = amount + "/" + fluidTankSlot.getCapacity() + "mB";
            List<ITextComponent> tooltip = Arrays.asList(fluidName, new StringTextComponent(fluidAmount));

            this.func_243308_b(matrixStack, tooltip, mouseX, mouseY);
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
