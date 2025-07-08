package com.wdcftgg.carpentersblocks.proxy;


import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.*;
import com.wdcftgg.carpentersblocks.carpentersblocks.tileentity.CbTileEntity;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.handler.DesignHandler;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.handler.EventHandler;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.handler.OverlayHandler;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.handler.PacketHandler;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.BlockRegistry;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.ConfigRegistry;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.ItemRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ServerProxy extends CommonProxy {

    public ServerProxy() {
    }

    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        ConfigRegistry.loadConfiguration(event);
        MinecraftForge.EVENT_BUS.register(new BlockRegistry());
        MinecraftForge.EVENT_BUS.register(new ItemRegistry());
        DesignHandler.preInit(event);
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        super.onPostInit(event);
    }

    public void onInit(FMLInitializationEvent event){
        super.onInit(event);

        RegisterTileEntity();
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        CarpentersBlocks.channel.register(new PacketHandler());

        // Initialize overlays
        if (ConfigRegistry.enableOverlays) {
            OverlayHandler.init();
        }

        // Register tile entities
    }

    private static void RegisterTileEntity() {

        GameRegistry.registerTileEntity(CbTileEntity.class,new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Carpenters"));
        GameRegistry.registerTileEntity(TileEntityGarageDoorBlock.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_GarageDoorBlock"));
        GameRegistry.registerTileEntity(TileEntityGarageDoor.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_GarageDoor"));
        GameRegistry.registerTileEntity(TileEntitySafe.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Safe"));
        GameRegistry.registerTileEntity(TileEntityTile.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Tile"));
        GameRegistry.registerTileEntity(TileEntityButton.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Button"));
        GameRegistry.registerTileEntity(TileEntityBed.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Bed"));
        GameRegistry.registerTileEntity(TileEntityDoor.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Door"));
        GameRegistry.registerTileEntity(TileEntityFlowerPot.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_FlowerPot"));
        GameRegistry.registerTileEntity(TileEntityTorch.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Torch"));
        GameRegistry.registerTileEntity(TileEntityStairs.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Stairs"));
        GameRegistry.registerTileEntity(TileEntitySlab.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Slab"));
        GameRegistry.registerTileEntity(TileEntityLadder.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Ladder"));
        GameRegistry.registerTileEntity(TileEntityLever.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Lever"));
        GameRegistry.registerTileEntity(TileEntityDaylightSensor.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_DaylightSensor"));
        GameRegistry.registerTileEntity(TileEntityHatch.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Hatch"));
        GameRegistry.registerTileEntity(TileEntityBarrier.class, new ResourceLocation(CarpentersBlocks.MODID, CarpentersBlocks.MODID + "_Barrier"));


    }
}
