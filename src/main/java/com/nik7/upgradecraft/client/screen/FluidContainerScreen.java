package com.nik7.upgradecraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.nik7.upgradecraft.client.render.utils.RenderHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static com.nik7.upgradecraft.utils.UpgCFluidHelper.writeTankInfoIntoTooltip;
import static net.minecraft.util.FastColor.ARGB32.*;

@OnlyIn(Dist.CLIENT)
public abstract class FluidContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected final List<RenderFluidTankSlot> fluidTankSlots = new ArrayList<>();


    public FluidContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
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
        float minU = icon.getU0();
        float maxU = icon.getU1();
        float minV = icon.getV0();
        float maxV = icon.getV1();

        float u = minU + (maxU - minU) * width / 16F;
        float v = minV + (maxV - minV) * height / 16F;

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(x, y + height, 0).uv(minU, v).endVertex();
        buffer.vertex(x + width, y + height, 0).uv(u, v).endVertex();
        buffer.vertex(x + width, y, 0).uv(u, minV).endVertex();
        buffer.vertex(x, y, 0).uv(minU, minV).endVertex();
        Tesselator.getInstance().end();
    }

    @Override
    protected void init() {
        super.init();

        assert this.minecraft != null;
        this.titleLabelX = (this.imageHeight - this.font.width(this.title)) / 2;
    }

    protected boolean addFluidTankSlot(RenderFluidTankSlot fluidTankSlot) {
        return fluidTankSlots.add(fluidTankSlot);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);

        for (RenderFluidTankSlot fluidTankSlot : fluidTankSlots) {
            this.fluidTooltip(pPoseStack, fluidTankSlot, pMouseX, pMouseY);
        }
    }

    protected void fluidTooltip(PoseStack pPoseStack, RenderFluidTankSlot fluidTankSlot, int mouseX, int mouseY) {
        if (fluidTankSlot.isHovered(mouseX, mouseY, leftPos, topPos)) {
            List<Component> tooltip = new ArrayList<>();
            writeTankInfoIntoTooltip(tooltip, fluidTankSlot.getFluidStack(), fluidTankSlot.getCapacity());

            this.renderComponentTooltip(pPoseStack, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int x, int y) {
        super.renderLabels(poseStack, x, y);
        for (RenderFluidTankSlot fluidTankSlot : fluidTankSlots) {
            renderFluid(poseStack, fluidTankSlot);
        }
    }

    protected void renderFluid(PoseStack poseStack, RenderFluidTankSlot fluidTankSlot) {
        if (this.minecraft != null) {
            FluidStack fluidStack = fluidTankSlot.getFluidStack();
            float scale = fluidTankSlot.getFluidScale();
            int argb = RenderHelper.getColorARGB(fluidTankSlot.getFluidStack(), scale);

            if (fluidStack.getFluid().getAttributes().isGaseous(fluidStack)) {
                scale = 1;
            }
            TextureAtlasSprite fluidSprite = RenderHelper.locationToSprite(fluidStack.getFluid().getAttributes().getStillTexture(fluidStack));


            final float red = red(argb) / 255f;
            final float green = green(argb) / 255f;
            final float blue = blue(argb) / 255f;
            final float alpha = alpha(argb) / 255f;

            final int baseTankX = fluidTankSlot.getBaseTankX();
            final int tankWidth = fluidTankSlot.getTankWidth();
            final int baseTankY = fluidTankSlot.getBaseTankY();
            final int tankHeight = fluidTankSlot.getTankHeight();


            this.minecraft.getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);
            poseStack.pushPose();
            RenderSystem.clearColor(red, green, blue, alpha);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            float fluidHeight = tankHeight * scale;
            drawTiledTexture(baseTankX, baseTankY - fluidHeight, tankWidth, fluidHeight, fluidSprite);

            RenderSystem.clearColor(1f, 1f, 1f, 1f);
            poseStack.popPose();
        }
    }

}
