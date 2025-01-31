package com.wdcftgg.somecarpentersblocks.network;

import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntitySafe;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSafe implements IMessageHandler<MessageSafe, IMessage>, IMessage  {

    public int block;
    public int meta;
    public int x;
    public int y;
    public int z;


    public MessageSafe() {
    }

    public MessageSafe(int block, int meta, BlockPos pos) {
        this.block = block;
        this.meta = meta;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();

    }

    public void fromBytes(ByteBuf buf) {
        this.block = buf.readInt();
        this.meta = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(block);
        buf.writeInt(meta);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }


    public IMessage onMessage(MessageSafe message, MessageContext ctx) {
        BlockPos pos = new BlockPos(message.x, message.y, message.z);
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            World world = player.world;;
            if (world.getTileEntity(pos) != null) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof TileEntitySafe) {
                    TileEntitySafe tileEntitySafe = (TileEntitySafe) tileEntity;
                    tileEntitySafe.setBlockAndMeta(message.block, message.meta);
                }
            }
        }
        return null;
    }
}