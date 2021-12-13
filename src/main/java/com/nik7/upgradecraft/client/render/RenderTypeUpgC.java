package com.nik7.upgradecraft.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RenderTypeUpgC extends RenderType {

    public RenderTypeUpgC(String nameIn, VertexFormat vertexFormat, VertexFormat.Mode mode, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, vertexFormat, mode, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType resizableCuboid() {
        RenderType.CompositeState.CompositeStateBuilder stateBuilder = preset()
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(OVERLAY)
                ;
        return create(MOD_ID + ":resizable_cuboid", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true,
                stateBuilder.createCompositeState(true));
    }

    private static RenderType.CompositeState.CompositeStateBuilder preset() {
        return RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTextureState(BLOCK_SHEET_MIPPED)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                ;
    }
}
