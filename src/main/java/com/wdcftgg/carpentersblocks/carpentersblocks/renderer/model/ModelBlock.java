package com.wdcftgg.carpentersblocks.carpentersblocks.renderer.model;

import com.google.common.collect.ImmutableSet;
import com.wdcftgg.carpentersblocks.carpentersblocks.renderer.bakedblock.BakedBlock;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.SpriteRegistry;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class ModelBlock implements IModel {
	
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptySet();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableSet.of(SpriteRegistry.RESOURCE_UNCOVERED_FULL);
    }
	
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new BakedBlock(state, format, bakedTextureGetter);
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }
    
}