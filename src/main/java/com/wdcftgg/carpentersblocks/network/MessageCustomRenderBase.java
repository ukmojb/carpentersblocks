package com.wdcftgg.carpentersblocks.network;

import com.wdcftgg.carpentersblocks.blocks.te.ICustomRenderBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCustomRenderBase implements IMessageHandler<MessageCustomRenderBase, IMessage>, IMessage  {

    public String block;
    public int meta;
    public int x;
    public int y;
    public int z;


    public MessageCustomRenderBase() {
    }

    public MessageCustomRenderBase(String block, int meta, BlockPos pos) {
        this.block = block;
        this.meta = meta;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();

    }

    public void fromBytes(ByteBuf buf) {
        this.block = ByteBufUtils.readUTF8String(buf);
        this.meta = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, block);
        buf.writeInt(meta);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }


    public IMessage onMessage(MessageCustomRenderBase message, MessageContext ctx) {
        BlockPos pos = new BlockPos(message.x, message.y, message.z);
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            World world = mc.player.world;
            if (world.getTileEntity(pos) != null) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof ICustomRenderBase) {
                    ICustomRenderBase customRenderBase = (ICustomRenderBase) tileEntity;
                    customRenderBase.setBlockAndMeta(message.block, message.meta);
                }
            }
        }
        return null;
    }
}