package com.nik7.upgradecraft.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nik7.upgradecraft.client.renderer.Cuboid;
import com.nik7.upgradecraft.client.renderer.RenderTypeUpgC;
import com.nik7.upgradecraft.client.utils.RenderHelper;
import com.nik7.upgradecraft.tileentity.BaseFluidHandlerTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public abstract class BaseFluidHandlerRenderer<T extends BaseFluidHandlerTileEntity> extends TileEntityRenderer<T> {


    public BaseFluidHandlerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public static void renderFluidCuboid(FluidStack fluidStack,
                                         float fluidScale,
                                         Cuboid cuboid,
                                         MatrixStack matrixStackIn,
                                         IRenderTypeBuffer bufferIn,
                                         int combinedLightIn,
                                         Consumer<MatrixStack> preRenderOperation) {
        if (!fluidStack.isEmpty()) {
            Fluid fluid = fluidStack.getFluid();

            ResourceLocation stillTexture = fluid.getAttributes().getStillTexture(fluidStack);
            TextureAtlasSprite stillSprite = RenderHelper.locationToSprite(stillTexture);

            ResourceLocation flowingTexture = fluid.getAttributes().getFlowingTexture(fluidStack);
            TextureAtlasSprite flowingSprite = RenderHelper.locationToSprite(flowingTexture);

            int light = RenderHelper.calculateFluidGlowLight(combinedLightIn, fluidStack);
            int argb = RenderHelper.getColorARGB(fluidStack, fluidScale);

            cuboid.scaleY(fluidScale);
            IVertexBuilder buffer = bufferIn.getBuffer(RenderTypeUpgC.resizableCuboid());
            RenderHelper.renderCuboid(cuboid, matrixStackIn, buffer, light, argb,
                    face -> face == Direction.UP ? stillSprite : flowingSprite,
                    preRenderOperation);
        }
    }

    protected float getFluidScale(T tileEntityIn, FluidStack fluidStack) {
        return fluidStack.getAmount() / (float) tileEntityIn.getCapacity();
    }

}
