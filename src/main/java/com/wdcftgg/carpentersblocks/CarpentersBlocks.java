package com.wdcftgg.carpentersblocks;

import com.wdcftgg.carpentersblocks.init.RegistryHandler;
import com.wdcftgg.carpentersblocks.network.PacketHandler;
import com.wdcftgg.carpentersblocks.proxy.CommonProxy;
import com.wdcftgg.carpentersblocks.proxy.ServerProxy;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = CarpentersBlocks.MODID, name = CarpentersBlocks.NAME, version = CarpentersBlocks.VERSION)
public class CarpentersBlocks {
    public static final String MODID = "carpentersblocks";
    public static final String NAME = "CarpentersBlocks";
    public static final String VERSION = "1.0.4";
    public static Logger logger;

    @Mod.Instance
    public static CarpentersBlocks instance;
    public static FMLEventChannel channel;

    @SidedProxy(serverSide = "com.wdcftgg.carpentersblocks.proxy.CommonProxy", clientSide = "com.wdcftgg.carpentersblocks.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static ServerProxy serverProxy = new ServerProxy();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OBJLoader.INSTANCE.addDomain(MODID);

        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(MODID);

        logger = event.getModLog();
        proxy.onPreInit(event);
        serverProxy.onPreInit(event);

        RegistryHandler.preInitRegistries(event);

    }


    @Mod.EventHandler
    public static void Init(FMLInitializationEvent event) {
        proxy.onInit(event);
        serverProxy.onInit(event);

        PacketHandler.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        RegistryHandler.postInitReg();
        proxy.onPostInit(event);
        serverProxy.onPostInit(event);
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
