package com.wdcftgg.somecarpentersblocks.proxy;

import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntityGarageDoor;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntityGarageDoorBlock;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntitySafe;
import com.wdcftgg.somecarpentersblocks.client.render.RenderBlockGarageDoor;
import com.wdcftgg.somecarpentersblocks.client.render.RenderBlockGarageDoorBlock;
import com.wdcftgg.somecarpentersblocks.client.render.RenderBlockSafe;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.List;

public class ClientProxy extends CommonProxy {

	public ClientProxy() {
	}


	public void registerItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}

	public void onInit(){
		super.onInit();
//		MinecraftForge.EVENT_BUS.register(new EventLossSpatialSense());
	}



	public void onPreInit() {
		super.onPreInit();

//		RenderingRegistry.registerEntityRenderingHandler(EntityTimeCrack.class, RenderTimeCrack::new);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySafe.class, new RenderBlockSafe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGarageDoor.class, new RenderBlockGarageDoor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGarageDoorBlock.class, new RenderBlockGarageDoorBlock());
	}

	public void onPostInit() {
		super.onPostInit();

	}

	public static List<LayerRenderer<EntityLivingBase>> getLayerRenderers(RenderPlayer instance) {
		return (List)getPrivateValue(RenderLivingBase.class, instance, "layerRenderers");
	}

	private static <T> Object getPrivateValue(Class<T> clazz, T instance, String name) {
		try {
			return ObfuscationReflectionHelper.getPrivateValue(clazz, instance, name);
		} catch (Exception var4) {
			return null;
		}
	}

}
