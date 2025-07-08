package com.wdcftgg.carpentersblocks.carpentersblocks.renderer.bakedblock;

import com.wdcftgg.carpentersblocks.carpentersblocks.block.IStateImplementor;
import com.wdcftgg.carpentersblocks.carpentersblocks.block.state.Property;
import com.wdcftgg.carpentersblocks.carpentersblocks.renderer.AbstractBakedModel;
import com.wdcftgg.carpentersblocks.carpentersblocks.renderer.RenderPkg;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.BlockRegistry;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.states.StatePart;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.states.StateUtil;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.states.factory.AbstractState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class BakedPressurePlate extends AbstractBakedModel {

	private static List<BakedQuad> _inventoryBakedQuads;
	
    public BakedPressurePlate(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}
    
	@Override
	public List<BakedQuad> getInventoryQuads(RenderPkg renderPkg) {
		if (_inventoryBakedQuads == null) {
			renderPkg.addAll(((IStateImplementor)BlockRegistry.blockCarpentersPressurePlate).getStateMap().getInventoryQuads());
			_inventoryBakedQuads = renderPkg.getInventoryQuads();
		}
		return _inventoryBakedQuads;
	}
	
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		StateUtil util = new StateUtil();
		AbstractState state = (AbstractState) RenderPkg.getThreadedProperty(Property.CB_STATE);
		for (StatePart part : state.getStateParts()) {
			renderPkg.addAll(util.getQuads(part));
		}
	}
	
}