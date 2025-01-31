package com.wdcftgg.somecarpentersblocks.network;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : wdcftgg
 * @create 2023/7/13 23:29
 */

import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE;
    public static int num = 0;

    public PacketHandler() {
    }

    public static void init() {
        INSTANCE.registerMessage(MessageSafe.class, MessageSafe.class, num++, Side.CLIENT);
        INSTANCE.registerMessage(MessageGarageDoor.class, MessageGarageDoor.class, num++, Side.CLIENT);


//        INSTANCE.registerMessage(MessageSpacePhase0.class, MessageSpacePhase0.class, num++, Side.SERVER);
    }

    static {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SomeCarpentersBlocks.MODID);
    }
}
