package com.wdcftgg.carpentersblocks.network;

import com.wdcftgg.carpentersblocks.blocks.te.TileEntityGarageDoor;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityGarageDoorBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGarageDoor implements IMessageHandler<MessageGarageDoor, IMessage>, IMessage  {

    public String block;
    public int meta;
    public int x;
    public int y;
    public int z;


    public MessageGarageDoor() {
    }

    public MessageGarageDoor(String block, int meta, BlockPos pos) {
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


    public IMessage onMessage(MessageGarageDoor message, MessageContext ctx) {
        BlockPos pos = new BlockPos(message.x, message.y, message.z);

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            World world = mc.player.world;
            if (world.getTileEntity(pos) != null) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof TileEntityGarageDoor) {
                    TileEntityGarageDoor te = (TileEntityGarageDoor) tileEntity;
                    te.setBlockAndMeta(message.block, message.meta);
                }
                if (tileEntity instanceof TileEntityGarageDoorBlock) {
                    TileEntityGarageDoorBlock te = (TileEntityGarageDoorBlock) tileEntity;
                    te.setBlockAndMeta(message.block, message.meta);
                }
            }
        }
        return null;
    }
}