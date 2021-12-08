package com.nik7.upgradecraft.client.model;

import com.google.common.collect.ImmutableList;
import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlimyObsidianModel extends BakedModelWrapper<IBakedModel> {

    public static final ModelProperty<Direction[]> CONNECTED_DIRECTIONS = new ModelProperty<>();

    public SlimyObsidianModel(IBakedModel originalModel) {
        super(originalModel);
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        builder.withInitial(CONNECTED_DIRECTIONS, new Direction[0]);
        ModelDataMap modelDataMap = builder.build();

        List<Direction> connectedDirections = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            if (world.getBlockState(pos.offset(direction)).getBlock() == RegisterBlocks.SLIMY_OBSIDIAN_BLOCK.get()) {
                connectedDirections.add(direction);
            }
        }

        if (!connectedDirections.isEmpty()) {
            modelDataMap.setData(CONNECTED_DIRECTIONS, connectedDirections.toArray(new Direction[0]));
        }
        return modelDataMap;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> quads = originalModel.getQuads(state, side, rand);
        if (side != null || !extraData.hasProperty(CONNECTED_DIRECTIONS)) {
            return quads;
        }
        Direction[] data = extraData.getData(CONNECTED_DIRECTIONS);
        quads = new ArrayList<>(quads);
        VertexFormat block = DefaultVertexFormats.BLOCK;
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        return null;
    }


}
