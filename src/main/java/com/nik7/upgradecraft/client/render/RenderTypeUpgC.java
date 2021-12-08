package com.nik7.upgradecraft.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class RenderTypeUpgC extends RenderType {
    // private static final AlphaState CUBOID_ALPHA = new RenderState.AlphaState(0.1F);

    public RenderTypeUpgC(String nameIn, VertexFormat vertexFormat, VertexFormat.Mode mode, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, vertexFormat, mode, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType resizableCuboid() {
        RenderType.CompositeState.CompositeStateBuilder stateBuilder = preset(InventoryMenu.BLOCK_ATLAS)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                // .alpha(CUBOID_ALPHA)
                ;
        return create("resizable_cuboid", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, true,
                stateBuilder.createCompositeState(true));
    }

    private static RenderType.CompositeState.CompositeStateBuilder preset(ResourceLocation resourceLocation) {
        return RenderType.CompositeState.builder()
                .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                .setCullState(RenderStateShard.CULL)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                // .shadeModel(SHADE_ENABLED)
                ;
    }
}
