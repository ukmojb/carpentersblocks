package com.wdcftgg.carpentersblocks.blocks.te;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.network.MessageCustomRenderBase;
import com.wdcftgg.carpentersblocks.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileEntitySafe extends TileEntityChest implements ICustomRenderBase {

    private String block = "";
    private int meta = 0;

    @Override
    public String getName()
    {
        return CarpentersBlocks.MODID + ".container.safe";
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        block = compound.getString("block");
        meta = compound.getInteger("meta");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("block", block);
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

        PacketHandler.INSTANCE.sendToAllAround(new MessageCustomRenderBase(block, meta, this.pos), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), 256.0D));
    }

    @Override
    public void checkForAdjacentChests() {
        this.adjacentChestXNeg = null;
        this.adjacentChestXPos = null;
        this.adjacentChestZNeg = null;
        this.adjacentChestZPos = null;
    }

    public IBlockState getiBlockState() {
        if (block == "") {
            return Blocks.AIR.getDefaultState();
        }
        return Block.getBlockFromName(block).getStateFromMeta(meta);
    }

    public void setBlockAndMeta(String block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    public boolean hasBlock(){
        return block != "";
    }

    public String getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }
}
