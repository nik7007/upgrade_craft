package com.nik7.upgradecraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nik7.upgradecraft.blockentity.FluidFurnaceEntity;
import com.nik7.upgradecraft.inventory.FluidFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.blockentity.FluidFurnaceEntity.FLUID_TICK_DURATION;

public class FluidFurnaceScreen extends FluidContainerScreen<FluidFurnaceMenu> {
    public final static ResourceLocation GUI = new ResourceLocation(MOD_ID, "textures/gui/screen/fluid_furnace_gui.png");

    public FluidFurnaceScreen(FluidFurnaceMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        FluidFurnaceEntity blockEntity = pMenu.getBlockEntity();

        this.addFluidTankSlot(
                new RenderFluidTankSlot(
                        () -> blockEntity != null ? blockEntity.getFluid() : FluidStack.EMPTY,
                        () -> blockEntity != null ? blockEntity.getCapacity() : 0,
                        15, 16, 58, 32)
        );
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        RenderSystem.setShaderTexture(0, GUI);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        int cookTimeTotal = menu.getCookTimeTotal();
        int cookTime = menu.getCookTime();

        int cookProgress = (int) ((cookTime / (float) cookTimeTotal) * 24);

        this.blit(pPoseStack, relX + 79, relY + 34, 176, 14, cookProgress + 1, 16);

        int burnTime = menu.getBurnTime();
        int burnProgress = (int) ((burnTime / (float) FLUID_TICK_DURATION) * 14);
        this.blit(pPoseStack, relX + 56, relY + 36 + 13 - burnProgress, 176, 13 - burnProgress, 14, burnProgress);

    }
}
