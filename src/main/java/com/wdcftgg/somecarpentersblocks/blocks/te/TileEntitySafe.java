package com.wdcftgg.somecarpentersblocks.blocks.te;

import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import com.wdcftgg.somecarpentersblocks.network.MessageSafe;
import com.wdcftgg.somecarpentersblocks.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class TileEntitySafe extends TileEntityChest {

    private int block = 0;
    private int meta = 0;

    @Override
    public String getName()
    {
        return SomeCarpentersBlocks.MODID + ".container.safe";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        block = compound.getInteger("block");
        meta = compound.getInteger("meta");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("block", block);
        compound.setInteger("meta", meta);
        return super.writeToNBT(compound);
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
//            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
//
//            if (this.getChestType() == BlockChest.Type.TRAP)
//            {
//                this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
//            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        this.numPlayersUsing = type;
        return false;
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockChest)
        {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
//            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
//
//            if (this.getChestType() == BlockChest.Type.TRAP)
//            {
//                this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
//            }
        }
    }

    @Override
    public void update() {
        super.update();

        PacketHandler.INSTANCE.sendToAllAround(new MessageSafe(block, meta, this.pos), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), 256.0D));
    }

    @Override
    public void checkForAdjacentChests() {
        this.adjacentChestXNeg = null;
        this.adjacentChestXPos = null;
        this.adjacentChestZNeg = null;
        this.adjacentChestZPos = null;
    }

    public IBlockState getiBlockState() {
        return Block.getBlockById(block).getStateFromMeta(meta);
    }

    public void setBlockAndMeta(int block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    public boolean hasBlock(){
        return block != 0;
    }

    public int getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }
}
