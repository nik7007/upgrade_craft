package com.nik7.upgradecraft.client.render.blockentity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nik7.upgradecraft.blockentity.BaseFluidHandlerEntity;
import com.nik7.upgradecraft.client.render.Cuboid;
import com.nik7.upgradecraft.client.render.RenderTypeUpgC;
import com.nik7.upgradecraft.client.render.utils.RenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public abstract class BaseFluidHandlerRenderer<T extends BaseFluidHandlerEntity> implements BlockEntityRenderer<T> {
    protected final BlockEntityRendererProvider.Context context;

    protected BaseFluidHandlerRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    public static void renderFluidCuboid(FluidStack fluidStack,
                                         float fluidScale,
                                         Cuboid cuboid,
                                         PoseStack poseStack,
                                         MultiBufferSource bufferIn,
                                         int combinedLightIn,
                                         Consumer<PoseStack> preRenderOperation) {
        if (!fluidStack.isEmpty()) {
            Fluid fluid = fluidStack.getFluid();

            ResourceLocation stillTexture = fluid.getAttributes().getStillTexture(fluidStack);
            TextureAtlasSprite stillSprite = RenderHelper.locationToSprite(stillTexture);

            ResourceLocation flowingTexture = fluid.getAttributes().getFlowingTexture(fluidStack);
            TextureAtlasSprite flowingSprite = RenderHelper.locationToSprite(flowingTexture);

            int light = RenderHelper.calculateFluidGlowLight(combinedLightIn, fluidStack);
            int argb = RenderHelper.getColorARGB(fluidStack, fluidScale);

            cuboid.scaleY(fluidScale);
            VertexConsumer buffer = bufferIn.getBuffer(RenderTypeUpgC.resizableCuboid());
            RenderHelper.renderCuboid(cuboid, poseStack, buffer, light, argb,
                    face -> face == Direction.UP ? stillSprite : flowingSprite,
                    preRenderOperation);
        }
    }

    protected float getFluidScale(T tileEntityIn, FluidStack fluidStack) {
        return fluidStack.getAmount() / (float) tileEntityIn.getCapacity();
    }

}
