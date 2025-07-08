package com.wdcftgg.carpentersblocks.network;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : wdcftgg
 * @create 2023/7/13 23:29
 */

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(CarpentersBlocks.MODID + "_1");
    public static int num = 0;

    public PacketHandler() {
    }

    public static void init() {
        INSTANCE.registerMessage(MessagePotDirt.class, MessagePotDirt.class, num++, Side.CLIENT);
        INSTANCE.registerMessage(MessageCustomRenderBase.class, MessageCustomRenderBase.class, num++, Side.CLIENT);
        INSTANCE.registerMessage(MessageGarageDoor.class, MessageGarageDoor.class, num++, Side.CLIENT);


//        INSTANCE.registerMessage(MessageSpacePhase0.class, MessageSpacePhase0.class, num++, Side.SERVER);
    }
}
