package com.nik7.upgradecraft.client.renderer;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTypeUpgC extends RenderType {
    private static final AlphaState CUBOID_ALPHA = new RenderState.AlphaState(0.1F);

    public RenderTypeUpgC(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType resizableCuboid() {
        RenderType.State.Builder stateBuilder = preset(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
                .lightmap(LIGHTMAP_ENABLED)
                .alpha(CUBOID_ALPHA);
        return makeType("resizable_cuboid", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 256, true, true,
                stateBuilder.build(true));
    }

    private static RenderType.State.Builder preset(ResourceLocation resourceLocation) {
        return RenderType.State.getBuilder()
                .texture(new RenderState.TextureState(resourceLocation, false, false))
                .cull(CULL_ENABLED)
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .shadeModel(SHADE_ENABLED);
    }
}
