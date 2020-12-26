package com.nik7.upgradecraft.client.renderer.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nik7.upgradecraft.client.renderer.Cuboid;
import com.nik7.upgradecraft.client.renderer.tileentity.BaseFluidHandlerRenderer;
import com.nik7.upgradecraft.client.utils.BakedModelProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.HashMap;
import java.util.Map;

import static com.nik7.upgradecraft.utils.UpgCFluidHelper.getCapacityFromItemStack;
import static com.nik7.upgradecraft.utils.UpgCFluidHelper.readFluidTankFromItemStack;

public class FluidTankItemStackRender extends ItemStackTileEntityRenderer {

    private static final Map<ResourceLocation, BakedModelProxy> MODELS = new HashMap<>();
    private static ItemRenderer renderer = null;

    public static void registerModelProxy(Map<ResourceLocation, IBakedModel> modelRegistry, ResourceLocation registryName) {
        ResourceLocation location = getResourceLocation(registryName);
        IBakedModel bakedModel = modelRegistry.get(location);
        BakedModelProxy modelProxy = new BakedModelProxy(bakedModel);
        modelRegistry.put(location, modelProxy);
        MODELS.put(location, modelProxy);
    }

    private static ResourceLocation getResourceLocation(ResourceLocation registryName) {
        return new ModelResourceLocation(registryName, "inventory");
    }

    @Override
    public void func_239207_a_(ItemStack itemStack, ItemCameraTransforms.TransformType type, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (renderer == null) {
            renderer = Minecraft.getInstance().getItemRenderer();
        }

        matrixStack.pop();

        ResourceLocation resourceLocation = getResourceLocation(itemStack.getItem().getRegistryName());
        BakedModelProxy modelProxy = MODELS.get(resourceLocation);
        IBakedModel baseModel = null;
        ItemCameraTransforms.TransformType transformType = null;
        if (modelProxy != null) {
            baseModel = modelProxy.getBaseModel();
            transformType = modelProxy.getTransformType();

            renderItemModel(itemStack, matrixStack, buffer, combinedLight, combinedOverlay, baseModel, transformType);
        }

        matrixStack.push();
        if (baseModel != null && transformType != null) {
            baseModel.handlePerspective(transformType, matrixStack);
            if (transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
            }
        }
        matrixStack.translate(-.5, -.5, -.5);

        renderFluid(itemStack, matrixStack, buffer, combinedLight);

        matrixStack.pop();
    }

    private void renderFluid(ItemStack itemStack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight) {
        FluidTank tank = readFluidTankFromItemStack(itemStack);
        int capacity = getCapacityFromItemStack(itemStack);
        FluidStack fluidStack = tank.getFluid();
        float fluidScale = fluidStack.getAmount() / (float) capacity;
        Cuboid cuboid = new Cuboid(0.15, 0.10, 0.15, 0.85, 0.90, 0.85);
        BaseFluidHandlerRenderer.renderFluidCuboid(fluidStack, fluidScale, cuboid, matrixStack, buffer, combinedLight, null);
    }

    private void renderItemModel(ItemStack itemStack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay, IBakedModel baseModel, ItemCameraTransforms.TransformType transformType) {
        boolean leftHand = false;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            leftHand = player.getHeldItemOffhand() == itemStack;
        }
        renderer.renderItem(itemStack, transformType, leftHand, matrixStack, buffer, combinedLight, combinedOverlay, baseModel);
    }
}
