package com.wdcftgg.carpentersblocks.proxy;

import com.wdcftgg.carpentersblocks.blocks.te.*;
import com.wdcftgg.carpentersblocks.carpentersblocks.CarpentersBlocksCachedResources;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.ModLogger;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.ConfigRegistry;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.SpriteRegistry;
import com.wdcftgg.carpentersblocks.client.render.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

public class ClientProxy extends CommonProxy {

	public ClientProxy() {
	}


	public void registerItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}

	public void onInit(FMLInitializationEvent event){
		super.onInit(event);
		CarpentersBlocksCachedResources.INSTANCE.init();
//		MinecraftForge.EVENT_BUS.register(new EventLossSpatialSense());
	}



	public void onPreInit(FMLPreInitializationEvent event) {
		super.onPreInit(event);

		MinecraftForge.EVENT_BUS.register(new SpriteRegistry());
		ModelLoaderRegistry.registerLoader(new com.wdcftgg.carpentersblocks.carpentersblocks.renderer.ModelLoader());

//		RenderingRegistry.registerEntityRenderingHandler(EntityTimeCrack.class, RenderTimeCrack::new);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySafe.class, new RenderBlockSafe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGarageDoor.class, new RenderBlockGarageDoor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGarageDoorBlock.class, new RenderBlockGarageDoorBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityButton.class, new RenderBlockButton());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBed.class, new RenderBlockBed());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDoor.class, new RenderBlockDoor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTorch.class, new RenderBlockTorch());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlowerPot.class, new RenderBlockPot());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStairs.class, new RenderBlockStairs());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySlab.class, new RenderBlockSlab());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLadder.class, new RenderBlockLadder());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLever.class, new RenderBlockLever());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDaylightSensor.class, new RenderBlockDaylightSensor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHatch.class, new RenderBlockHatch());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrier.class, new RenderBlockBarrier());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGate.class, new RenderBlockGate());
	}

	public void onPostInit(FMLPostInitializationEvent event) {
		super.onPostInit(event);
		if (FMLClientHandler.instance().hasOptifine()) {
			ModLogger.info("Optifine detected. Disabling custom vertexformat.");
			ConfigRegistry.enableOptifineCompatibility = true;
		}

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
