package com.wdcftgg.somecarpentersblocks.proxy;


import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntityGarageDoor;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntityGarageDoorBlock;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntitySafe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ServerProxy extends CommonProxy {

    public ServerProxy() {
    }

    public void onPreInit() {
        super.onPreInit();
    }

    public void onPostInit() {
        super.onPostInit();
    }

    public void onInit(){
        super.onInit();

        RegisterTileEntity();


//        MinecraftForge.EVENT_BUS.register(new EventModuleXP());
    }

    private static void RegisterTileEntity() {

        GameRegistry.registerTileEntity(TileEntityGarageDoorBlock.class, new ResourceLocation(SomeCarpentersBlocks.MODID, SomeCarpentersBlocks.MODID + "_GarageDoorBlock"));
        GameRegistry.registerTileEntity(TileEntityGarageDoor.class, new ResourceLocation(SomeCarpentersBlocks.MODID, SomeCarpentersBlocks.MODID + "_GarageDoor"));
        GameRegistry.registerTileEntity(TileEntitySafe.class, new ResourceLocation(SomeCarpentersBlocks.MODID, SomeCarpentersBlocks.MODID + "_Safe"));

//        GameRegistry.registerTileEntity(TileEntityDeBoomOrb.class, new ResourceLocation(MODID, "deboom_orb_basic"));

    }
}
