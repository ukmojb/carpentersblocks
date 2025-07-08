package com.wdcftgg.carpentersblocks.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {


	public void registerItemRenderer(Item item, int meta, String id) {
		//Ignored
	}

	public void onPreInit(FMLPreInitializationEvent event) {
	}

	public void onPostInit(FMLPostInitializationEvent event) {
	}

	public void onInit(FMLInitializationEvent event){

	}

}
