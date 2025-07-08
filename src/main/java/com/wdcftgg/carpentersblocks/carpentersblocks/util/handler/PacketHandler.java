package com.wdcftgg.carpentersblocks.carpentersblocks.util.handler;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.carpentersblocks.network.ICarpentersPacket;
import com.wdcftgg.carpentersblocks.carpentersblocks.network.PacketActivateBlock;
import com.wdcftgg.carpentersblocks.carpentersblocks.network.PacketEnrichPlant;
import com.wdcftgg.carpentersblocks.carpentersblocks.network.PacketSlopeSelect;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.ModLogger;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketHandler {

    private final static List<Class> packetCarrier;
    static {
        packetCarrier = new ArrayList<Class>();
        packetCarrier.add(PacketActivateBlock.class);
        packetCarrier.add(PacketEnrichPlant.class);
        packetCarrier.add(PacketSlopeSelect.class);
    }

    @SubscribeEvent
    public void onServerPacket(ServerCustomPacketEvent event) throws IOException {
        ByteBufInputStream bbis = new ByteBufInputStream(event.getPacket().payload());
        EntityPlayer entityPlayer = ((NetHandlerPlayServer) event.getHandler()).player;
        int packetId = bbis.readInt();
        if (packetId < packetCarrier.size()) {
            try {
                ICarpentersPacket packetClass = (ICarpentersPacket) packetCarrier.get(packetId).newInstance();
                packetClass.processData(entityPlayer, bbis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ModLogger.log(Level.WARN, "Encountered out of range packet Id: " + packetId);
        }
        bbis.close();
    }

    public static void sendPacketToServer(ICarpentersPacket packet) {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        buffer.writeInt(packetCarrier.indexOf(packet.getClass()));
        try {
            packet.appendData(buffer);
        } catch (IOException e) { }
        CarpentersBlocks.channel.sendToServer(new FMLProxyPacket(new CPacketCustomPayload(CarpentersBlocks.MODID, buffer)));
    }

}
