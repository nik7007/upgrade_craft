package com.nik7.upgradecraft.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nik7.upgradecraft.client.utils.RenderHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static com.nik7.upgradecraft.utils.UpgCFluidHelper.writeTankInfoIntoTooltip;
import static net.minecraft.util.ColorHelper.PackedColor.*;

public abstract class FluidContainerScreen<T extends Container> extends ContainerScreen<T> {

    protected final List<RenderFluidTankSlot> fluidTankSlots = new ArrayList<>();

    public FluidContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    protected static void drawTiledTexture(float x, float y, float width, float height, TextureAtlasSprite icon) {

        for (int w = 0; w < width; w += 16) {
            for (int h = 0; h < height; h += 16) {
                float drawWidth = Math.min(width - w, 16);
                float drawHeight = Math.min(height - h, 16);
                drawScaledTexturedModalRectFromSprite(x + w, y + h, drawWidth, drawHeight, icon);
            }
        }

    }

    protected static void drawScaledTexturedModalRectFromSprite(float x, float y, float width, float height, TextureAtlasSprite icon) {

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
    protected void init() {
        super.init();

        assert this.minecraft != null;
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
    }

    protected boolean addFluidTankSlot(RenderFluidTankSlot fluidTankSlot) {
        return fluidTankSlots.add(fluidTankSlot);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        for (RenderFluidTankSlot fluidTankSlot : fluidTankSlots) {
            this.fluidTooltip(matrixStack, fluidTankSlot, mouseX, mouseY);
        }
    }

    protected void fluidTooltip(MatrixStack matrixStack, RenderFluidTankSlot fluidTankSlot, int mouseX, int mouseY) {
        if (fluidTankSlot.isHovered(mouseX, mouseY, guiLeft, guiTop)) {
            List<ITextComponent> tooltip = new ArrayList<>();
            writeTankInfoIntoTooltip(tooltip, fluidTankSlot.getFluidStack(), fluidTankSlot.getCapacity());

            this.func_243308_b(matrixStack, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        for (RenderFluidTankSlot fluidTankSlot : fluidTankSlots) {
            renderFluid(fluidTankSlot);
        }
    }

    protected void renderFluid(RenderFluidTankSlot fluidTankSlot) {
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

            float fluidHeight = tankHeight * scale;
            drawTiledTexture(baseTankX, baseTankY - fluidHeight, tankWidth, fluidHeight, fluidSprite);

            RenderSystem.color4f(1f, 1f, 1f, 1f);
            GL11.glPopMatrix();
        }
    }
}
