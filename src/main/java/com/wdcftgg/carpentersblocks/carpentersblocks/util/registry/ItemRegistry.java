package com.wdcftgg.carpentersblocks.carpentersblocks.util.registry;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.carpentersblocks.item.ItemCarpentersChisel;
import com.wdcftgg.carpentersblocks.carpentersblocks.item.ItemCarpentersHammer;
import com.wdcftgg.carpentersblocks.init.ModCreativeTab;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemRegistry {

    public static Item itemCarpentersHammer;
    public static Item itemCarpentersChisel;
    public static Item itemCarpentersDoor;
    public static Item itemCarpentersBed;
    public static Item itemCarpentersTile;
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
    	if (ConfigRegistry.enableHammer) {
			itemCarpentersHammer = new ItemCarpentersHammer()
				.setRegistryName("carpenters_hammer")
				.setTranslationKey("itemCarpentersHammer")
				.setCreativeTab(ModCreativeTab.Tab)
				.setMaxStackSize(1);
			if (ConfigRegistry.itemCarpentersToolsDamageable) {
				itemCarpentersHammer.setMaxDamage(ConfigRegistry.itemCarpentersToolsUses);
			}
    		event.getRegistry().register(this.itemCarpentersHammer);
    	}
    	if (ConfigRegistry.enableChisel) {
			itemCarpentersChisel = new ItemCarpentersChisel()
				.setRegistryName("carpenters_chisel")
				.setTranslationKey("itemCarpentersChisel")
				.setCreativeTab(ModCreativeTab.Tab)
				.setMaxStackSize(1);
			if (ConfigRegistry.itemCarpentersToolsDamageable) {
				itemCarpentersChisel.setMaxDamage(ConfigRegistry.itemCarpentersToolsUses);
			}
    		event.getRegistry().register(this.itemCarpentersChisel);
    	}
    	// TODO: Register other items
    }
    
    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        if (ConfigRegistry.enableHammer) {
        	ModelLoader.setCustomModelResourceLocation(itemCarpentersHammer, 0, new ModelResourceLocation(CarpentersBlocks.MODID + ":" + "carpenters_hammer"));
        }
        if (ConfigRegistry.enableChisel) {
            ModelLoader.setCustomModelResourceLocation(itemCarpentersChisel, 0, new ModelResourceLocation(CarpentersBlocks.MODID + ":" + "carpenters_chisel"));
        }
        /*
        if (enableTile) {
            itemCarpentersTile = new ItemCarpentersTile().setUnlocalizedName("itemCarpentersTile");
            GameRegistry.registerItem(itemCarpentersTile, "itemCarpentersTile");
        }
        if (BlockRegistry.enableDoor) {
            itemCarpentersDoor = new ItemCarpentersDoor().setUnlocalizedName("itemCarpentersDoor");
            GameRegistry.registerItem(itemCarpentersDoor, "itemCarpentersDoor");
        }
        if (BlockRegistry.enableBed) {
            itemCarpentersBed = new ItemCarpentersBed().setUnlocalizedName("itemCarpentersBed");
            GameRegistry.registerItem(itemCarpentersBed, "itemCarpentersBed");
        }*/
    }
    
}
