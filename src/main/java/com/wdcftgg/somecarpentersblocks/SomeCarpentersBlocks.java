package com.wdcftgg.somecarpentersblocks;

import com.wdcftgg.somecarpentersblocks.init.RegistryHandler;
import com.wdcftgg.somecarpentersblocks.network.PacketHandler;
import com.wdcftgg.somecarpentersblocks.proxy.CommonProxy;
import com.wdcftgg.somecarpentersblocks.proxy.ServerProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = SomeCarpentersBlocks.MODID, name = SomeCarpentersBlocks.NAME, version = SomeCarpentersBlocks.VERSION)
public class SomeCarpentersBlocks {
    public static final String MODID = "somecarpentersblocks";
    public static final String NAME = "SomeCarpentersBlocks";
    public static final String VERSION = "1.0.2";
    public static Logger logger;

    @Mod.Instance
    public static SomeCarpentersBlocks instance;

    @SidedProxy(serverSide = "com.wdcftgg.somecarpentersblocks.proxy.CommonProxy", clientSide = "com.wdcftgg.somecarpentersblocks.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static ServerProxy serverProxy = new ServerProxy();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.onPreInit();
        serverProxy.onPreInit();

        RegistryHandler.preInitRegistries(event);

    }


    @Mod.EventHandler
    public static void Init(FMLInitializationEvent event) {
        proxy.onInit();
        serverProxy.onInit();

        PacketHandler.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        RegistryHandler.postInitReg();
        proxy.onPostInit();
        serverProxy.onPostInit();
    }


    @Mod.EventHandler
    public static void serverInit(FMLServerStartingEvent event) {
        RegistryHandler.serverRegistries(event);
    }

    public static void LogWarning(String str, Object... args) {
        logger.warn(String.format(str, args));
    }

    public static void LogWarning(String str) {
        logger.warn(str);
    }

    public static void Log(String str) {
        logger.info(str);
    }

    public static void Log(String str, Object... args) {
        logger.info(String.format(str, args));
    }
}
