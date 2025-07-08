package com.wdcftgg.carpentersblocks.blocks.te;

import com.wdcftgg.carpentersblocks.blocks.BlockButton;
import com.wdcftgg.carpentersblocks.network.MessageCustomRenderBase;
import com.wdcftgg.carpentersblocks.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileEntityButton extends TileEntity implements ITickable,ICustomRenderBase {

    private String block = "";
    private int meta = 0;

    @Override
    public void update() {
        PacketHandler.INSTANCE.sendToAllAround(new MessageCustomRenderBase(block, meta, this.pos), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), 256.0D));
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

    public IBlockState getiBlockState() {
        if (block.isEmpty()) {
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

    public boolean isPower() {
        IBlockState blockState = world.getBlockState(pos);
        Block block1 = world.getBlockState(pos).getBlock();

        if (block1 instanceof BlockButton) {
            return blockState.getValue(BlockButton.POWERED);
        }

        return false;
    }
}