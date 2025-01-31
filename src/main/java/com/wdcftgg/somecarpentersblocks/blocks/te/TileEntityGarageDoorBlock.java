package com.wdcftgg.somecarpentersblocks.blocks.te;

import com.wdcftgg.somecarpentersblocks.network.MessageGarageDoor;
import com.wdcftgg.somecarpentersblocks.network.MessageSafe;
import com.wdcftgg.somecarpentersblocks.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileEntityGarageDoorBlock extends TileEntity implements ITickable {

    private int block = 0;
    private int meta = 0;

    @Override
    public void update() {
        PacketHandler.INSTANCE.sendToAllAround(new MessageGarageDoor(block, meta, this.pos), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), 256.0D));
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
