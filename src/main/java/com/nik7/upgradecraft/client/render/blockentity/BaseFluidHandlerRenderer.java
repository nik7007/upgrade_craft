package com.nik7.upgradecraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nik7.upgradecraft.block.entity.AbstractEntityFluidHandler;
import com.nik7.upgradecraft.client.render.Cuboid;
import com.nik7.upgradecraft.client.render.RenderTypeUpgC;
import com.nik7.upgradecraft.client.render.util.RenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public abstract class BaseFluidHandlerRenderer<T extends AbstractEntityFluidHandler> implements BlockEntityRenderer<T> {
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

            IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid);
            ResourceLocation stillTexture = fluidTypeExtensions.getStillTexture(fluidStack);
            TextureAtlasSprite stillSprite = RenderHelper.locationToSprite(stillTexture);

            ResourceLocation flowingTexture = fluidTypeExtensions.getFlowingTexture(fluidStack);
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
