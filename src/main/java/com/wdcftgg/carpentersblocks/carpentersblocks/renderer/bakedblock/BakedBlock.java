package com.wdcftgg.carpentersblocks.carpentersblocks.renderer.bakedblock;

import com.wdcftgg.carpentersblocks.carpentersblocks.renderer.AbstractBakedModel;
import com.wdcftgg.carpentersblocks.carpentersblocks.renderer.RenderPkg;
import com.wdcftgg.carpentersblocks.carpentersblocks.renderer.helper.RenderHelper;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.SpriteRegistry;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

import java.util.List;
import java.util.function.Function;

public class BakedBlock extends AbstractBakedModel {
	
	private static List<BakedQuad> _inventoryBakedQuads;
	
    public BakedBlock(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}
    
	@Override
	public List<BakedQuad> getInventoryQuads(RenderPkg renderPkg) {
		if (_inventoryBakedQuads == null) {
			fillQuads(renderPkg);
			_inventoryBakedQuads = renderPkg.getInventoryQuads();
		}
		return _inventoryBakedQuads;
	}
    
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadYPos(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadZPos(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadXPos(SpriteRegistry.sprite_uncovered_quartered));
    }
	
}
