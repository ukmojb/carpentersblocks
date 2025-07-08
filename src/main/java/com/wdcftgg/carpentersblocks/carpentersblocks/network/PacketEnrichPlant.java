package com.wdcftgg.carpentersblocks.carpentersblocks.network;

import com.wdcftgg.carpentersblocks.carpentersblocks.tileentity.CbTileEntity;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.EntityLivingUtil;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.attribute.EnumAttributeLocation;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.attribute.EnumAttributeType;
import io.netty.buffer.ByteBufInputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketEnrichPlant extends TilePacket {

    private int hexColor;

    public PacketEnrichPlant() {}

    /**
     * For the server to examine plant color, since it's a client-side only property.
     * 
     * @param blockPos the block position
     * @param hexColor the integer color
     */
    public PacketEnrichPlant(BlockPos blockPos, int hexColor) {
        super(blockPos);
        this.hexColor = hexColor;
    }

    @Override
    public void processData(EntityPlayer entityPlayer, ByteBufInputStream bbis) throws IOException {
        super.processData(entityPlayer, bbis);
        World world = entityPlayer.getEntityWorld();
        hexColor = bbis.readInt();

        TileEntity tileEntity = world.getTileEntity(_blockPos);
        if (tileEntity != null && tileEntity instanceof CbTileEntity) {
        	CbTileEntity cbTileEntity = (CbTileEntity) tileEntity;
            if (hexColor != 16777215 && !cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.FERTILIZER)) {
            	cbTileEntity.addAttribute(EnumAttributeLocation.HOST, EnumAttributeType.FERTILIZER, new ItemStack(Items.DYE, 1, 15));
                EntityLivingUtil.decrementCurrentSlot(entityPlayer);
            }
        }
    }

    @Override
    public void appendData(PacketBuffer buffer) throws IOException {
        super.appendData(buffer);
        buffer.writeInt(hexColor);
    }

}
